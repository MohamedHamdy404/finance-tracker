-- V1__Create_Initial_Schema.sql
-- Personal Finance Tracker - Initial Database Schema

-- ============================================
-- ENUMS (PostgreSQL)
-- ============================================

CREATE TYPE account_type AS ENUM ('CHECKING', 'SAVINGS');
CREATE TYPE currency_type AS ENUM ('EGP', 'USD');
CREATE TYPE transaction_type AS ENUM ('INCOME', 'EXPENSE', 'TRANSFER', 'ADJUSTMENT');
CREATE TYPE transfer_direction AS ENUM ('IN', 'OUT');
CREATE TYPE category_type AS ENUM ('INCOME', 'EXPENSE');
CREATE TYPE allocation_status AS ENUM ('ACTIVE', 'MATURED', 'CLOSED');
CREATE TYPE container_type AS ENUM ('ACCOUNT_BASED', 'EXTERNAL');

-- ============================================
-- TABLES
-- ============================================

-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    base_currency currency_type NOT NULL DEFAULT 'EGP',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Banks Table (Lookup/Reference)
CREATE TABLE banks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(50) NOT NULL UNIQUE,
    country VARCHAR(3) NOT NULL DEFAULT 'EGY',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Accounts Table
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    bank_id BIGINT NOT NULL REFERENCES banks(id),
    name VARCHAR(255) NOT NULL,
    account_type account_type NOT NULL,
    currency currency_type NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Categories Table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    type category_type NOT NULL,
    icon VARCHAR(50),
    color VARCHAR(7),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_category_name_type UNIQUE (user_id, name, type)
);

-- Transactions Table
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    transaction_type transaction_type NOT NULL,
    transfer_direction transfer_direction,
    transfer_group_id UUID,
    amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
    currency currency_type NOT NULL,
    transaction_date DATE NOT NULL,
    description VARCHAR(500) NOT NULL,
    fx_rate_to_base DECIMAL(10, 6),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Constraints
    CONSTRAINT chk_transfer_direction CHECK (
        (transaction_type = 'TRANSFER' AND transfer_direction IS NOT NULL AND transfer_group_id IS NOT NULL)
        OR (transaction_type != 'TRANSFER' AND transfer_direction IS NULL AND transfer_group_id IS NULL)
    )
);

-- Allocations Table
CREATE TABLE allocations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    account_id BIGINT REFERENCES accounts(id) ON DELETE SET NULL,
    container_type container_type NOT NULL,
    name VARCHAR(255) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
    currency currency_type NOT NULL,
    start_date DATE NOT NULL,
    maturity_date DATE,
    allocation_status allocation_status NOT NULL DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Constraints
    CONSTRAINT chk_allocation_account CHECK (
        (container_type = 'ACCOUNT_BASED' AND account_id IS NOT NULL)
        OR (container_type = 'EXTERNAL' AND account_id IS NULL)
    ),
    CONSTRAINT chk_maturity_date CHECK (maturity_date IS NULL OR maturity_date >= start_date)
);

-- Refresh Tokens Table
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(500) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- INDEXES
-- ============================================

-- Users
CREATE INDEX idx_users_email ON users(email);

-- Accounts
CREATE INDEX idx_accounts_user_id ON accounts(user_id);
CREATE INDEX idx_accounts_bank_id ON accounts(bank_id);

-- Categories
CREATE INDEX idx_categories_user_id ON categories(user_id);

-- Transactions (Critical for performance)
CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_category_id ON transactions(category_id);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transactions_user_date ON transactions(user_id, transaction_date);
CREATE INDEX idx_transactions_type ON transactions(transaction_type);
CREATE INDEX idx_transactions_transfer_group ON transactions(transfer_group_id) WHERE transfer_group_id IS NOT NULL;

-- Allocations
CREATE INDEX idx_allocations_user_id ON allocations(user_id);
CREATE INDEX idx_allocations_account_id ON allocations(account_id);
CREATE INDEX idx_allocations_status ON allocations(allocation_status);

-- Refresh Tokens
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_expiry ON refresh_tokens(expiry_date);

-- ============================================
-- NOTE: updated_at is managed by JPA @UpdateTimestamp
-- No database triggers needed
-- ============================================

-- ============================================
-- COMMENTS
-- ============================================

COMMENT ON TABLE users IS 'System users with authentication credentials';
COMMENT ON TABLE banks IS 'Reference table for bank institutions';
COMMENT ON TABLE accounts IS 'User bank accounts (checking/savings)';
COMMENT ON TABLE categories IS 'User-defined transaction categories';
COMMENT ON TABLE transactions IS 'All financial transactions (income, expense, transfer, adjustment)';
COMMENT ON TABLE allocations IS 'Locked/allocated money (certificates, investments)';
COMMENT ON TABLE refresh_tokens IS 'JWT refresh tokens for authentication';

COMMENT ON COLUMN transactions.transfer_group_id IS 'Links two TRANSFER transactions together';
COMMENT ON COLUMN transactions.fx_rate_to_base IS 'Exchange rate to user base currency at transaction time';
COMMENT ON COLUMN allocations.container_type IS 'ACCOUNT_BASED (linked to account) or EXTERNAL (independent)';