<a href=https://github.com/Stas-Kuprienko/Anastasia_trader_project/blob/master/readme.md>EN</a>
# ANASTASIA TRADER PROJECT
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Spring_Framework-green?style=flat-square&logo=Spring&logoColor=green&label=%7C)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Gradle-blue?style=flat-square&logo=Gradle&logoColor=darkgreen&label=%7C&labelColor=white&color=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Docker-blue?style=flat-square&logo=Docker&logoColor=%233399ff&label=%7C&labelColor=white&color=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-PostgreSQL-blue?style=flat-square&logo=postgresql&logoColor=white&label=%7C&labelColor=blue&color=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Hibernate-steelblue?style=flat-square&logo=Hibernate&logoColor=yellow&label=%7C&labelColor=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-MySQL-lightblue?style=flat-square&logo=mysql&logoSize=auto&logoColor=white&label=%7C&labelColor=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-gRPC-mediumturquoise?style=flat-square&logo=java&logoColor=mediumturquoise&label=%3C-%3E|&labelColor=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Telegram_API-blue?logo=telegram&label=%7C)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Apache_Kafka-blue?style=flat-square&logo=Apache%20Kafka&logoColor=black&label=%7C&labelColor=white&color=darkblue)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Redis-blue?style=flat-square&logo=redis&logoColor=white&label=%7C&labelColor=red&color=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Keycloak-blue?style=flat-square&logo=keycloak&logoColor=%233399ff&label=%7C&labelColor=white&color=grey)


<img src="project_files/hello.webp" style="max-width: 96px; width: 96px;">

## Сервис биржевой торговли, обеспечивающий поддержку автоматических сделок на основе торговых стратегий.
### Возможности сервиса:
- Подключение брокерских счетов с помощью API-ключей
- Доступ к различным биржевым котировкам
- Возможность совершать сделки вручную
- Подписка на автоматические торговые стратегии
- Доступ к торговой истории в XLSX-файлах
- Управление с помощью Telegram-бота
***
## Стек технологий:
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
## Структура проекта:
### Компоненты проекта - это 4 основных приложения (так называемые микросервисы):
***
+ ### Core Service:
+ Основное приложение проекта. Является оркестратором между остальными компонентами.
  Выполняет основную логику работы сервиса, за исключением анализа рынка и автоматизации торговли.
  Основные задачи -
  администрирование сервиса,
  аутентификация пользователей,
  управление пользовательскими данными,
  доступ к рыночным данным,
  выполнение торговых операций,
  сохранение истории торговли в xlsx-файлах,
  отправка уведомлений о сделках.
  Используемые технологии:
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
+ Приложение для аналитики и автоматической торговли.
  Отвечает за анализ рынка, выявление потенциальных направлений и хранение аналитических данных.
  Оно также позволяет торговать финансовыми инструментами на основе выбранных стратегий.
  Используемые технологии:
  + Spring Boot 3
  + Spring gRPC starter
  + gRPC Framework
  + Redis storage
***
+ ### Telegram Bot:
+ Приложение Telegram бота. Обеспечивает простое управление сервисами.
  Используемые технологии:
  + Spring Boot 3
  + Telegram bots 6
  + Spring JDBC
  + MySQL 8
  + Apache Kafka
***
+ ### UI Service
+ Интерфейсное приложение для доступа к браузеру.
  Имеет простой дизайн и функциональность.
  Используемые технологии:
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
