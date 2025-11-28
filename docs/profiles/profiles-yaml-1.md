# **Spring Profiles YAML Setup**

Below is a clean, professional **Spring Boot YAML profile setup** showing:

- A **common base config** (in `application.yml`)
- **Environment-specific profiles** (`application-dev.yml`, `application-test.yml`, `application-prod.yml`)
- Each profile **extends** the common configuration using `spring.config.import`

> ✅ _Only config files are shown — no Java code._

---

# **📄 application.yml (Common Base Config)**

```yaml
# application.yml
spring:
  application:
    name: sample-app

  datasource:
    url: jdbc:mysql://localhost:3306/common_db
    username: common_user
    password: common_pass
    driver-class-name: com.mysql.cj.jdbc.Driver

server:
  port: 8080

logging:
  level:
    root: INFO
    com.example: INFO
```

---

# **📄 application-dev.yml (Development Profile)**

```yaml
# application-dev.yml

spring:
  config:
    import: application.yml

  datasource:
    url: jdbc:mysql://localhost:3306/dev_db
    username: dev_user
    password: dev_pass

server:
  port: 8081

logging:
  level:
    com.example: DEBUG
```

---

# **📄 application-test.yml (Test Profile)**

```yaml
# application-test.yml

spring:
  config:
    import: application.yml

spring:
  datasource:
    url: jdbc:h2:mem:test_db
    username: sa
    password:
    driver-class-name: org.h2.Driver

server:
  port: 8082

logging:
  level:
    com.example: TRACE
```

---

# **📄 application-prod.yml (Production Profile)**

```yaml
# application-prod.yml

spring:
  config:
    import: application.yml

spring:
  datasource:
    url: jdbc:mysql://prod-db:3306/prod_db
    username: ${DB_USER}
    password: ${DB_PASS}

server:
  port: 80

logging:
  level:
    root: WARN
    com.example: INFO
```

---

# **How to Activate Profiles**

### **1️⃣ From application.yml**

```yaml
spring:
  profiles:
    active: dev
```

### **2️⃣ From command line**

```
java -jar app.jar --spring.profiles.active=prod
```

### **3️⃣ From environment variable**

```
export SPRING_PROFILES_ACTIVE=test
```
