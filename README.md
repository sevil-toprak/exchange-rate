# 💱 Exchange Rate API


---

## 🐳 Running with Docker

```bash
git clone https://github.com/sevil-toprak/exchange-rate.git
cd exchange-rate
docker-compose up --build
```
---

## 🚀 Project Purpose

This service provides Restful endpoints for:

- Retrieving current exchange rates between two currencies
- Converting a specific amount to another currency
- Performing bulk conversions via CSV file
- Tracking and retrieving history of exchange transactions

---

## 🛠️ Technologies Used

| Technology | Description |
|------------|-------------|
| **Java 21** | Modern language features and performance |
| **Spring Boot 3.4.5** | Dependency management and application setup |
| **H2 Database** | In-memory database for local development and testing |
| **Docker + Docker Compose** | Containerization and orchestration |

---

## 🌱 Git Workflow

This project follows the **Git Flow** branching strategy.

### Branches:
- `master`: Production-ready code
- `develop`: Active development branch
- `release`: Staging branch before deployment
- `feature/Task-*`: Feature branches created from `develop`
- `hotfix/*`: Emergency bugfix branches created from `master`, then merged into `develop`, `release`, and `master` respectively

* Task Descriptions: Three different branches were used to simulate the Git Flow process.
---

## 💾 H2 Database Configuration

| Field          | Value |
|----------------|-------|
| Console URL    | `http://localhost:8080/h2-console` |
| JDBC URL       | `jdbc:h2:mem:exchange-rate` |
| Driver Class   | `org.h2.Driver` |
| Username       | `sa` |
| Password       | (empty) |

---


## 📘 Available Endpoints

| Method | Endpoint                         | Description                               |
|--------|----------------------------------|-------------------------------------------|
| GET    | `/api/exchange-rate/exchange`    | Get exchange rate between two currencies  |
| GET    | `/api/exchange-rate/convert`     | Convert a specific amount                 |
| POST   | `/api/exchange-rate/bulk-convert`| Convert multiple rows via CSV upload      |
| GET    | `/api/exchange-rate/history`     | View historical exchange transactions     |

