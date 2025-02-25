# Мини-банк

## Описание проекта

В рамках нашей практики мы разрабатываем "Мини-банк", систему, состоящую из трёх ключевых компонентов: frontend, middle-слой и backend. Эта система представляет собой комплексное решение для обработки и автоматизации банковских операций.

## Компоненты системы

### Frontend

- **Технологии**: Telegram-bot на Java/Kotlin
- **Функционал**: Выступает как клиентское приложение, инициирующее запросы пользователей.

### Middle-слой

- **Технологии**: Сервис на Java/Kotlin
- **Функционал**: Принимает запросы от frontend, обеспечивает валидацию и авторизацию запросов, направляет их в backend.

### Backend

- **Технологии**: Сервис на Java/Kotlin
- **Функционал**: Обрабатывает банковские транзакции, управляет клиентскими данными и т.д.

## Схема взаимодействия компонентов

@startuml
actor Client
participant Frontend
participant Middle
participant Backend

Client -> Frontend: команда start
Frontend -> Client: список команд
Client -> Frontend: валидная команда
Frontend -> Middle: HTTP-запрос
alt Запрос валиден
Middle -> Backend: HTTP-запрос
else Запрос невалиден
Middle -> Frontend: Ошибка
end
Backend -> Middle: Данные
Middle -> Frontend: Обработанные данные
Frontend -> Client: Текстовый ответ
@enduml

## Запуск проекта

Для запуска проекта следуйте следующим шагам:
1. Клонируйте репозиторий с GitHub.
2. Настройте необходимые параметры в конфигурационных файлах.
3. Запустите сервисы frontend, middle и backend согласно инструкциям в соответствующих директориях.

