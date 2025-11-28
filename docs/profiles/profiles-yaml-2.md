# **Spring Profiles YAML Setup**

Below is **the complete, extended Spring Boot YAML configuration setup** including:

- Common config
- Dev, Test, Production profiles
- Property-map merging examples
- Multiple `import` hierarchy
- Secrets isolation (`application-secrets.yml`)
- Full microservice-style config structure

⚠ **ONLY YAML config files** — exactly as requested.

---

# ✅ **1. Common Base Configuration — `application.yml`**

```yaml
# application.yml
spring:
  application:
    name: sample-app

  profiles:
    active: dev  # default profile (can be overridden)

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: common_user
    password: common_pass
    url: jdbc:mysql://localhost:3306/common_db

server:
  port: 8080

logging:
  level:
    root: INFO
    com.sample: INFO

app:
  name: Sample Application
  features:
    cache: false
    metrics: true

# Import secrets (not committed to Git)
spring:
  config:
    import: optional:application-secrets.yml
```

---

# ✅ **2. Dev Profile — `application-dev.yml`**

```yaml
# application-dev.yml
spring:
  config:
    import:
      - application.yml
      - optional:application-secrets.yml

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dev_db
    username: dev_user
    password: dev_pass

server:
  port: 8081

logging:
  level:
    com.sample: DEBUG

app:
  features:
    cache: false  # override base
    debug-mode: true
```

---

# ✅ **3. Test Profile — `application-test.yml`**

```yaml
# application-test.yml
spring:
  config:
    import:
      - application.yml

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
    com.sample: TRACE

app:
  features:
    cache: false
    metrics: false  # override for test
```

---

# ✅ **4. Prod Profile — `application-prod.yml`**

```yaml
# application-prod.yml
spring:
  config:
    import:
      - application.yml
      - optional:application-secrets.yml

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
    com.sample: INFO

app:
  features:
    cache: true
    metrics: true
    audit: true
```

---

# ✅ **5. Secrets Isolation — `application-secrets.yml`**

> ⚠ **Do NOT commit this file to Git. Put it in `.gitignore`.**

```yaml
# application-secrets.yml
spring:
  datasource:
    username: ${SECRET_DB_USER}
    password: ${SECRET_DB_PASS}

jwt:
  secret: ${JWT_SECRET_KEY}

api:
  key: ${EXTERNAL_API_KEY}
```

---

# ✅ **6. Multiple Import Hierarchy Example**

A profile can import multiple layers:

```
application.yml  →  application-common.yml  →  application-dev.yml
```

### `application.yml`:

```yaml
spring:
  config:
    import:
      - application-common.yml
      - optional:application-secrets.yml
```

### `application-common.yml`:

```yaml
server:
  forward-headers-strategy: framework
  error:
    include-stacktrace: on_param
```

### `application-dev.yml`:

```yaml
spring:
  config:
    import:
      - application.yml
      - optional:application-secrets.yml

debug: true
```

---

# ✅ **7. Property Map Merging Example**

Base file:

```yaml
app:
  mail:
    host: smtp.example.com
    ports:
      - 587
      - 465
```

Dev override (adds new port, merges list):

```yaml
app:
  mail:
    ports:
      - 2525
```

**Merged output during runtime:**

```yaml
app.mail.ports = [587, 465, 2525]
```

---

# ✅ **8. Microservice-Friendly Structure (Recommended)**

```
config/
 ├── application.yml
 ├── application-common.yml
 ├── application-dev.yml
 ├── application-test.yml
 ├── application-prod.yml
 ├── application-secrets.yml
 └── application-monitoring.yml
```

Example `application-monitoring.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

Imported from prod:

```yaml
spring:
  config:
    import:
      - application.yml
      - application-monitoring.yml
      - optional:application-secrets.yml
```

---

# ✅ **9. How to Activate Profiles**

### CLI

```
java -jar app.jar --spring.profiles.active=prod
```

### Environment variable

```
export SPRING_PROFILES_ACTIVE=test
```

### IDE Run Config

```
-Dspring.profiles.active=dev
```
