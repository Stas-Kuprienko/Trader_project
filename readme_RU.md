[EN](readme.md)

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


## Сервис биржевой торговли, обеспечивающий поддержку автоматических сделок на основе торговых стратегий.
### Возможности сервиса:
- Подключение брокерских счетов с помощью API-ключей
- Доступ к различным биржевым котировкам
- Возможность совершать сделки вручную
- Подписка на автоматические торговые стратегии
- Настройка риск-профиля или выбор из значений по умолчанию
- Доступ к торговой истории в XLSX-файлах
- Управление с помощью Telegram-бота
***
## Стек технологий:
+ #### Java 21
+ #### Spring Framework
+ #### Gradle
+ #### Postgresql
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
  Также позволяет торговать финансовыми инструментами на основе выбранных стратегий.
  Используемые технологии:
  + Spring Boot 3
  + Spring gRPC starter
  + gRPC Framework
  + Postgresql database
***
+ ### Telegram Bot:
+ Приложение Telegram бота. Обеспечивает простое управление сервисами.
  Используемые технологии:
  + Spring Boot 3
  + Telegram bots 6
  + Redis storage
  + Apache Kafka
***
+ ### Notification Service:
+ Приложение, отвечающее за рассылку уведомлений пользователям.
  Используемые технологии:
  + Spring Boot 3
  + Flyway migration
  + Postgresql database
  + Spring Mail
  + Apache Kafka
***
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

## Структура проекта

### Контекст приложений

<img src="project_files/project_RU.png">

### Core Service

<img src="project_files/core-service_RU.png">

### Smart Service

<img src="project_files/smart-service_RU.png">

### UI Service

<img src="project_files/ui-service_RU.png">

### Telegram Bot

<img src="project_files/telegram-bot_RU.png">

***
 
//TODO
