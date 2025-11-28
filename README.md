# Spring 4 starter

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Boot Starter Web
- Spring JPA
- MySQL
- H2 Database
- Lombok
- Actuator
- Spring Boot DevTools
- Logback
- VSCode Ready

## Description

Spring 4 starter project vscode ready with Lombok, Actuator, Logback, Spring Boot DevTools, Spring Boot Starter Web, Spring JPA, MySQL, H2 Database

## Howto Activate profiles

1. From `application.yml` file:

```yaml
spring:
  profiles:
    active: dev
```

2. From command line:

```bash
java -jar target/learn-spring-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

3. From IDE (VSCode):
   add args to `launch.json`

```json
 "args": "--spring.profiles.active=dev",
```

## Fixes

- Mockito warning fixed in testing (`settings.json`) --FIXED--

## Notes

> **_NOTE:_** Please create environment variables `DB_USER` and `DB_PASSWORD` in `~/.zshrc` or `~/.bashrc` file as below:

```bash
export DB_USER="temp"
export DB_PASSWORD="temp"
```
