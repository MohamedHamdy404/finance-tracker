# 💰 Finance Tracker - Personal Expense Manager

A production-ready, multi-currency personal finance tracker built with **Spring Boot 3** and **PostgreSQL**. Track expenses, manage multiple bank accounts, analyze spending patterns, and monitor investments.

## 🎯 Features

### ✅ Phase 1 (Current)
- **Multi-currency Support** (EGP, USD)
- **Account Management** - Multiple bank accounts (checking/savings)
- **Transaction Tracking** - Income, expenses, transfers, adjustments
- **Category Management** - Customizable income/expense categories
- **Locked Allocations** - Track certificates, investments, locked funds
- **Analytics & Reports** - Monthly reports, spending analysis
- **RESTful API** - Well-documented REST endpoints
- **JWT Authentication** - Secure user authentication

### 🚀 Upcoming (Phase 2)
- Recurring transactions
- Budget limits per category
- Partial allocation withdrawals
- Advanced analytics dashboard
- Mobile app (Android)

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| **Backend** | Spring Boot 3.2.1 |
| **Security** | Spring Security + JWT |
| **Database** | PostgreSQL 15+ |
| **ORM** | JPA / Hibernate |
| **Migrations** | Flyway |
| **Mapping** | MapStruct |
| **Validation** | Jakarta Validation |
| **Documentation** | Swagger / OpenAPI 3 |
| **Build Tool** | Maven |

---

## 📋 Prerequisites

- **Java 17** or higher
- **PostgreSQL 15** or higher
- **Maven 3.8+**
- **Git**

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/finance-tracker.git
cd finance-tracker
```

### 2️⃣ Setup Database

Create PostgreSQL database:

```sql
CREATE DATABASE finance_tracker;
CREATE USER finance_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE finance_tracker TO finance_user;
```

### 3️⃣ Configure Application

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/finance_tracker
    username: finance_user
    password: your_password
```

**Important:** Set JWT secret for production:

```yaml
app:
  jwt:
    secret: your-secure-256-bit-secret-key-here
```

### 4️⃣ Build & Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 5️⃣ Access Swagger UI

Open browser: `http://localhost:8080/swagger-ui.html`

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/finance/tracker/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST Controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA Entities
│   │   ├── exception/       # Custom Exceptions
│   │   ├── mapper/          # MapStruct Mappers
│   │   ├── repository/      # Spring Data Repositories
│   │   └── service/         # Business Logic
│   └── resources/
│       ├── application.yml  # Configuration
│       └── db/migration/    # Flyway SQL Scripts
└── test/                    # Unit & Integration Tests
```

---

## 🔐 Authentication

### Register New User

```bash
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123",
  "firstName": "John",
  "lastName": "Doe",
  "baseCurrency": "EGP"
}
```

### Login

```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

### Use Token

```bash
GET /api/accounts
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

---

## 📚 API Endpoints

### 🏦 Banks (Reference Data)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/banks` | Get all banks |
| GET | `/api/banks/{id}` | Get bank by ID |

### 💳 Accounts
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/accounts` | Create new account |
| GET | `/api/accounts` | Get all user accounts |
| GET | `/api/accounts/active` | Get active accounts |
| GET | `/api/accounts/{id}` | Get account by ID |
| PUT | `/api/accounts/{id}` | Update account |
| DELETE | `/api/accounts/{id}` | Soft delete account |

### 🏷️ Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/categories` | Create new category |
| GET | `/api/categories` | Get all user categories |
| GET | `/api/categories/active` | Get active categories |
| GET | `/api/categories/type/{type}` | Get by type (INCOME/EXPENSE) |
| GET | `/api/categories/{id}` | Get category by ID |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Soft delete category |

### 💸 Transactions *(Coming Soon)*
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/transactions` | Create transaction |
| GET | `/api/transactions` | Get all transactions |
| GET | `/api/transactions/{id}` | Get transaction by ID |

### 📊 Dashboard *(Coming Soon)*
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/dashboard` | Get overview stats |
| GET | `/api/dashboard/monthly` | Monthly summary |

---

## 💾 Database Schema

### Core Tables

```
users
├── id (PK)
├── email (unique)
├── password (bcrypt)
├── base_currency
└── is_active

banks (reference data)
├── id (PK)
├── name
└── code

accounts
├── id (PK)
├── user_id (FK → users)
├── bank_id (FK → banks)
├── name
├── account_type (CHECKING/SAVINGS)
├── currency (EGP/USD)
└── is_active

categories
├── id (PK)
├── user_id (FK → users)
├── name
├── type (INCOME/EXPENSE)
├── icon
└── color

transactions
├── id (PK)
├── user_id (FK → users)
├── account_id (FK → accounts)
├── category_id (FK → categories)
├── transaction_type (INCOME/EXPENSE/TRANSFER/ADJUSTMENT)
├── transfer_direction (IN/OUT)
├── transfer_group_id (UUID)
├── amount
├── currency
├── transaction_date
├── description
└── fx_rate_to_base

allocations
├── id (PK)
├── user_id (FK → users)
├── account_id (FK → accounts, nullable)
├── container_type (ACCOUNT_BASED/EXTERNAL)
├── name
├── amount
├── currency
├── start_date
├── maturity_date
└── allocation_status
```

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report
```

---

## 🐳 Docker Setup (Optional)

### Docker Compose

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: finance_tracker
      POSTGRES_USER: finance_user
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/finance_tracker
      SPRING_DATASOURCE_USERNAME: finance_user
      SPRING_DATASOURCE_PASSWORD: postgres

volumes:
  postgres_data:
```

Run:
```bash
docker-compose up -d
```

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

---

## 🙏 Acknowledgments

- Spring Boot Team
- PostgreSQL Community
- MapStruct Contributors

---

## 📞 Support

For issues and questions:
- Create an [Issue](https://github.com/yourusername/finance-tracker/issues)
- Email: support@yourapp.com

---

**⭐ Star this repo if you find it helpful!**