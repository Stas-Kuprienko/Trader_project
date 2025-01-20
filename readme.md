[RU](readme_RU.md)

# TRADER PROJECT
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Spring_Framework-green?style=flat-square&logo=Spring&logoColor=green&label=%7C)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Gradle-blue?style=flat-square&logo=Gradle&logoColor=darkgreen&label=%7C&labelColor=white&color=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Docker-blue?style=flat-square&logo=Docker&logoColor=%233399ff&label=%7C&labelColor=white&color=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-PostgreSQL-blue?style=flat-square&logo=postgresql&logoColor=white&label=%7C&labelColor=blue&color=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Hibernate-steelblue?style=flat-square&logo=Hibernate&logoColor=yellow&label=%7C&labelColor=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-gRPC-mediumturquoise?style=flat-square&logo=java&logoColor=mediumturquoise&label=%3C-%3E|&labelColor=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Telegram_API-blue?logo=telegram&label=%7C)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Apache_Kafka-blue?style=flat-square&logo=Apache%20Kafka&logoColor=black&label=%7C&labelColor=white&color=darkblue)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Redis-blue?style=flat-square&logo=redis&logoColor=white&label=%7C&labelColor=red&color=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Keycloak-blue?style=flat-square&logo=keycloak&logoColor=%233399ff&label=%7C&labelColor=white&color=grey)


## The exchange trading service that provides support for automated transactions based on trading strategies.
### Service features:
- Connection of brokerage accounts by API keys
- Access to a variety of stock quotes
- Ability to execute trades manually
- Subscription to automatic trading strategies
- Setting up a custom risk profile or choosing from the defaults
- Trading history access in XLSX files
- Management via Telegram bot
***
## Technology stack:
+ #### Java 21
+ #### Spring Framework
+ #### Gradle
+ #### Postgresql
+ #### Redis
+ #### Kafka
+ #### Keycloak
+ #### Docker
***

## Project structure:
### The components of the project is 4 main applications (so-called microservices):
***
+ ### Core Service:
+ The main application of the project. There is orchestrator between the other components.
  Performs the basic logic of the service, except market analytics and trading automation.
  The main tasks -
  service administration,
  user authentication,
  user data management,
  access to market data,
  execution of trade transactions,
  saving trade history in xlsx files,
  sending notifications about transactions.
  Technologies used:
  + Spring Boot 3
      + WebFlux
      + Web Security OAuth2
      + Data R2DBC
      + Data Redis Reactive
  + Flyway migration
  + Postgresql database
  + Apache Kafka
  + gRPC Framework
  + Apache POI API
  + Redis storage
  + Keycloak
***
+ ### Smart Service:
+ The application for analytics and automated trading.
  There is responsible for market analysis, identifying potential opportunities, and storing analytical data.
  It also enables the trading of financial instruments based on predefined strategies.
  Technologies used:
  + Spring Boot 3
  + Spring gRPC starter
  + gRPC Framework
  + Postgresql database
***
+ ### Telegram Bot:
+ The telegram bot application. Provides easy service management.
  Technologies used:
  + Spring Boot 3
  + Telegram bots 6
  + Redis storage
  + Apache Kafka
***
+ ### Notification Service:
+ The application responsible for sending notifications to users.
  Technologies used:
  + Spring Boot 3
  + Flyway migration
  + Postgresql database
  + Spring Mail
  + Apache Kafka
***
***
+ ### UI Service
+ The frontend application for browser access.
  There has a simple interface and functionality.
  Technologies used:
  + Spring Boot 3
    + WebMVC
    + Web Security OAuth2
    + Thymeleaf
    + Data Redis
  + Keycloak
  + Redis storage
***

## Project structure

### Applications context

<img src="project_files/project_EN.png">

### Core Service

<img src="project_files/core-service_EN.png">

### Smart Service

<img src="project_files/smart-service_EN.png">

### UI Service

<img src="project_files/ui-service_EN.png">

### Telegram Bot

<img src="project_files/telegram-bot_EN.png">

***
 
//TODO
