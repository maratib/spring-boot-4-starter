# **Spring Profiles YAML Setup**

Below is a **complete version of the Spring Boot configuration using Spring Cloud Config Server**, including:

- **Config Server setup**
- **Externalized YAML files in a Git repo**
- **Client-side bootstrap config**
- **Profiles (dev, test, prod)**
- **Shared/common config**
- **Secrets file**
- **Hierarchical imports**

✔ **Only configuration files**
✔ **No Java code**

---

# 🌐 **1. Config Server — `application.yml`**

This file lives **in the Config Server JAR** (not the client apps).

```yaml
# Config Server: application.yml
server:
  port: 8888

spring:
  application:
    name: config-server

  cloud:
    config:
      server:
        git:
          uri: https://github.com/your-org/your-config-repo.git
          clone-on-start: true
          search-paths:
            - configs
```

---

# 📁 **2. Folder Structure in Git (Config Repo)**

```
your-config-repo/
└── configs/
    ├── sample-app.yml
    ├── sample-app-dev.yml
    ├── sample-app-test.yml
    ├── sample-app-prod.yml
    ├── sample-app-secrets.yml
    ├── sample-app-common.yml
    └── sample-app-monitoring.yml
```

> These YAML files are **fetched dynamically** by Spring Cloud Config Clients.

---

# 📄 **3. Common Config – `sample-app.yml`**

```yaml
# sample-app.yml (default, shared for all profiles)
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
    com.sample: INFO

app:
  features:
    metrics: true
    cache: false

spring:
  config:
    import:
      - optional:sample-app-common.yml
      - optional:sample-app-secrets.yml
```

---

# 🧩 **4. Common Shared Config – `sample-app-common.yml`**

```yaml
# sample-app-common.yml
server:
  forward-headers-strategy: framework

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

---

# 🔐 **5. Secrets File (Do NOT commit to public GitHub)**

### `sample-app-secrets.yml`

```yaml
# sample-app-secrets.yml
spring:
  datasource:
    username: ${SECRET_DB_USER}
    password: ${SECRET_DB_PASS}

jwt:
  secret: ${JWT_SECRET_KEY}
```

---

# 🟦 **6. Dev Profile – `sample-app-dev.yml`**

```yaml
# sample-app-dev.yml
spring:
  config:
    import:
      - sample-app.yml
      - optional:sample-app-secrets.yml

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
    debug-mode: true
    cache: false
```

---

# 🟧 **7. Test Profile – `sample-app-test.yml`**

```yaml
# sample-app-test.yml
spring:
  config:
    import:
      - sample-app.yml

spring:
  datasource:
    url: jdbc:h2:mem:test_db
    driver-class-name: org.h2.Driver
    username: sa
    password:

logging:
  level:
    com.sample: TRACE

server:
  port: 8082

app:
  features:
    metrics: false
```

---

# 🟥 **8. Production Profile – `sample-app-prod.yml`**

```yaml
# sample-app-prod.yml
spring:
  config:
    import:
      - sample-app.yml
      - optional:sample-app-secrets.yml
      - optional:sample-app-monitoring.yml

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
    metrics: true
    audit: true
    cache: true
```

---

# 📈 **9. Monitoring Add-on File – `sample-app-monitoring.yml`**

```yaml
# sample-app-monitoring.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus

management:
  metrics:
    export:
      prometheus:
        enabled: true
```

---

# 🚀 **10. Client App — `bootstrap.yml`**

Every **client application** needs this file to load config **from the Config Server**.

```yaml
# bootstrap.yml (client app)
spring:
  application:
    name: sample-app

  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
      retry:
        max-attempts: 5
        initial-interval: 2000

# Optional: fallback local settings
spring:
  config:
    import:
      - optional:application-local.yml
```

---

# 🛠 **11. Client App Profile Activation (`application.yml`)**

Clients still use **local profile activation**, but config loads from server.

```yaml
# application.yml (client app)
spring:
  profiles:
    active: dev
```

---

# 🟢 How Each Client App Loads Config

Example:

### **When profile = dev**

Client fetches:

```
GET /sample-app/dev
```

Config from:

1. `sample-app.yml`
2. `sample-app-dev.yml`
3. `sample-app-common.yml`
4. `sample-app-secrets.yml`
5. (optional) monitoring file
