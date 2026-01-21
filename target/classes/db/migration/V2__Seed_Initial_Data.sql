-- V2__Seed_Initial_Data.sql
-- Seed reference data

-- ============================================
-- BANKS (Reference Data)
-- ============================================

INSERT INTO banks (name, code, country) VALUES
('Bank Misr', 'BANKMSR', 'EGY'),
('Arabi Bank', 'ARABI', 'EGY'),
('National Bank of Egypt', 'NBE', 'EGY'),
('Commercial International Bank', 'CIB', 'EGY'),
('Banque Du Caire', 'BDC', 'EGY');

-- ============================================
-- DEFAULT CATEGORIES FUNCTION
-- ============================================
-- This function will be called after user registration to create default categories

CREATE OR REPLACE FUNCTION create_default_categories(p_user_id BIGINT)
RETURNS VOID AS $$
BEGIN
    -- Expense Categories (idempotent inserts)
    INSERT INTO categories (user_id, name, type) VALUES
    (p_user_id, 'Food & Dining', 'EXPENSE'),
    (p_user_id, 'Transport', 'EXPENSE'),
    (p_user_id, 'Bills & Utilities', 'EXPENSE'),
    (p_user_id, 'Entertainment', 'EXPENSE'),
    (p_user_id, 'Shopping', 'EXPENSE'),
    (p_user_id, 'Healthcare', 'EXPENSE'),
    (p_user_id, 'Education', 'EXPENSE'),
    (p_user_id, 'Investment', 'EXPENSE'),
    (p_user_id, 'Other', 'EXPENSE')
    ON CONFLICT (user_id, name, type) DO NOTHING;

    -- Income Categories (idempotent inserts)
    INSERT INTO categories (user_id, name, type) VALUES
    (p_user_id, 'Salary', 'INCOME'),
    (p_user_id, 'Freelance', 'INCOME'),
    (p_user_id, 'Investment Returns', 'INCOME'),
    (p_user_id, 'Gift', 'INCOME'),
    (p_user_id, 'Other', 'INCOME')
    ON CONFLICT (user_id, name, type) DO NOTHING;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION create_default_categories IS 'Creates default income/expense categories for new users';