# ANASTASIA TRADER PROJECT
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Spring_Framework_6-green?style=flat-square&logo=Spring&logoColor=green&label=%7C)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Hibernate_6-steelblue?style=flat-square&logo=Hibernate&logoColor=yellow&label=%7C&labelColor=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-MySQL_8-lightblue?style=flat-square&logo=mysql&logoSize=auto&logoColor=white&label=%7C&labelColor=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-gRPC-mediumturquoise?style=flat-square&logo=java&logoColor=mediumturquoise&label=%3C-%3E|&labelColor=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Telegram_API-blue?logo=telegram&label=%7C)

<img src="project_files/hello.webp" style="max-width: 96px; width: 96px;">

## The exchange trading service that provides support for automated transactions based on trading strategies.
### Service features:
- Connection of brokerage accounts by API keys
- Access to a variety of stock quotes
- Ability to execute trades manually
- Subscription to automatic trading strategies
- Trading history access in XLSX files
- Management via Telegram bot
***
## Technology stack:
+ #### Java 21
+ #### Spring Framework
+ #### Gradle
+ #### Postgresql
+ #### MySQL
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
  + Redis storage
***
+ ### Telegram Bot:
+ The telegram bot application. Provides easy service management.
  Technologies used:
  + Spring Boot 3
  + Telegram bots 6
  + Spring JDBC
  + MySQL 8
  + Apache Kafka
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
<img src="project_files/laptop.webp" style="max-width: 96px; width: 96px;">

***
 
//TODO
