# LangStory — Backend

> 🌐 **Platform:** [https://www.lang-story.com](https://www.lang-story.com)

---

## 📖 About

**LangStory** is an AI-powered language learning platform where users can learn languages — with a strong focus on **German** and **English**, alongside other languages like Spanish and more.

The idea is simple — instead of boring drills and flashcards, users learn through **interactive stories**. Reading and practicing through a story makes vocabulary and grammar stick naturally because it gives words real context and makes them easier to remember.

Once the user feels ready, they can take **real standardized exams** — like **Goethe-Zertifikat** for German, **IELTS** for English, or equivalent exams for other languages — directly on the platform. These exams simulate the real experience as closely as possible.

After submitting, the exam is **graded instantly by AI**, which also provides a detailed result and personalized suggestions on what to improve. The user can then go back, practice more through stories, and retake the exam as many times as needed to reach their goal.

Access to the platform requires a **subscription**, which unlocks full access to stories, exams, and AI feedback.

---

## 📦 Services

| Service | Description |
|---|---|
| `api-gateway` | Central entry point — routing & authentication filters |
| `auth-service` | User registration, login, JWT, Refresh Token, OAuth2 |
| `config-server` | Centralized configuration (GitHub-backed) |
| `discovery-service` | Eureka — service registration & discovery |
| `user-service` | User profiles & management |

---

## 🛠️ Tech Stack

| Technology | Version / Detail |
|---|---|
| **Java** | 21 |
| **Spring Boot** | 4.0.4 |
| **Spring Cloud** | 2025.1.1 |
| **Spring Security + JWT** | Authentication & Authorization |
| **Refresh Token** | Secure session management |
| **OAuth2** | Social login |
| **Spring Cloud Gateway** | API Gateway & routing |
| **Eureka** | Service discovery |
| **OpenFeign** | Inter-service communication |
| **Resilience4J** | Circuit breaker, retry, rate limiting |
| **Spring Cloud Config** | Centralized config management |
| **Zipkin + Micrometer** | Distributed tracing *(planned)* |
| **ELK Stack** | Centralized logging *(planned)* |
| **MySQL** | Database |
| **Lombok** | Boilerplate reduction |
| **Docker** | Containerization |
| **JUnit 5 + Mockito** | Unit & integration testing |

---

## 📊 Project Status

### ✅ Infrastructure Setup
- [x] Microservice project structure
- [x] Eureka service discovery (`discovery-service`)
- [x] Spring Cloud API Gateway (`api-gateway`)
- [x] Centralized config server (`config-server`)

### 🔄 Currently Working On
- [ ] User registration (email & password)
- [ ] OAuth2 social login
- [ ] JWT + Refresh Token

### 📋 Planned
- [ ] Course & Story Service
- [ ] AI Exam Grading Service
- [ ] Payment & Subscription Service
- [ ] Notification Service
- [ ] Unit & Integration Tests
- [ ] Distributed Tracing (Zipkin + Micrometer)
- [ ] Centralized Logging (ELK Stack)
- [ ] Docker Compose full-stack setup
- [ ] CI/CD (GitHub Actions)

---

## 🌿 Branch Strategy

```
main
 └── develop
      ├── feature/auth-service
      ├── feature/user-service
      └── feature/...
```

| Branch | Purpose |
|---|---|
| `main` | Final production-ready merges only |
| `develop` | Integration & testing of feature branches |
| `feature/*` | One branch per feature |

---

## 🚀 Getting Started

### Prerequisites
- Java 21
- Maven 3.8+
- Docker & Docker Compose
- MySQL

### Clone & Run

```bash
git clone https://github.com/mahdiandrovo/lang-story.git
cd lang-story
```

```bash
# Run a specific service
cd auth-service
mvn spring-boot:run
```

---

## 📬 Contact

**LinkedIn** — [https://www.linkedin.com/in/mahdiandrovo](https://www.linkedin.com/in/mahdiandrovo/)
