# 📚 Library Management System

This is a **Spring Boot-based Library Management System** deployed using **Docker Compose**. It manages **admins, librarians, students**, and library operations with:

* **MySQL** for persistence
* **Nginx** as a reverse proxy for load balancing

---

## 🛠️ Prerequisites

Ensure the following are installed on your system:

* [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/)
* [Maven](https://maven.apache.org/install.html) (for building the project)
* Get [PostMark](https://postmarkapp.com/) API keys and Email.
* Environment variables configured:

    * `SPRING_DATASOURCE_PASSWORD`
    * `POSTMARK_API_KEY`
    * `POSTMARK_FROM_EMAIL`

---

## ⚙️ Initial Setup

### 👤 Initial Admin

Upon first startup, the application automatically creates an initial admin:

```text
Username: admin
Password: admin
```

This user can log in and manage other admins, librarians, and students through the API.

---

## 🔐 Environment Variables

Create a `.env` file in the project root with the following:

```env
SPRING_DATASOURCE_PASSWORD=your_secure_password
POSTMARK_API_KEY=your_postmark_api_key
POSTMARK_FROM_EMAIL=your_email@domain.com
```

> Replace the placeholders with actual credentials.
> `SPRING_DATASOURCE_PASSWORD` must match the password in the MySQL service.

---

## 🏗️ Build Instructions

### 1. Build the Project

Navigate to the root project directory and run:

```bash
./mvnw package -DskipTests
```

This creates a JAR file at:

```
target/library-0.0.1-SNAPSHOT.jar
```

### 2. Verify the Build

* Ensure the build completes without errors.
* Confirm the JAR file exists in the `target/` directory.

---

## 🚀 Running the Application

### 1. Start the Services

Ensure your `.env` file is ready, then run:

```bash
docker compose up --build 
```

* 📦 MySQL database (`mysql`)
* ⚙️ Spring Boot app instances (`app`)
* 🌐 Nginx reverse proxy (`nginx`)
### 2. Additional Notes

* **Wait for services to initialize:** This may take **1–2 minutes**.

* **Monitor logs:**

  ```bash
  docker compose logs
  ```

  Look for:

  ```text
  Initial admin created: A_admin
  ```

* **Access the application:**

  ```
  http://localhost:8080
  ```

---

## 🔁 Scaling & Reset

* To run a different number of app instances:

  ```bash
  docker compose up --build --scale app=3
  ```

* To reset the MySQL database:

  ```bash
  docker compose down --volumes
  docker compose up --build --scale app=3
  ```

---
## 📘 Full API Reference
For a complete list of available routes and their request/response structure:

➡️ **Check the `controllers` and `auth` package** in the source code:

```
src/main/java/com/eshan/library/controllers/
src/main/java/com/eshan/library/auths/
```

---
## 🔄 Testing Load Balancing

Run this loop to test:

```bash
for i in {1..30}; do
  curl -s -o /dev/null -w "%{http_code}\n" \
    -X POST http://localhost:8080/auth/authenticate \
    -H "Content-Type: application/json" \
    -d '{"username":"admin", "password":"admin"}'
done
```

Then check Nginx logs:

```bash
docker compose logs nginx
```

Look for upstream IPs like:

```text
172.18.0.3:8080
172.18.0.4:8080
172.18.0.5:8080
```

---

## 🧾 Configuration Files

### 📄 `docker-compose.yml`

* Defines services: `mysql`, `app`, `nginx`
* Uses named volume: `mysql-data`
* Includes networking, volumes, and health checks

### 📄 `Dockerfile`

* Based on: `openjdk:17-jdk-slim`
* Includes:

    * Spring Boot JAR setup
    * `netcat` for health checks
    * Environment variable setup

### 📄 `nginx.conf`

* Configures Nginx for:

    * Reverse proxy
    * Load balancing
    * Rate-limiting `/auth/forgot-password`


