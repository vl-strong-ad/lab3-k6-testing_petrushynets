# 📦 Лабораторна робота №3  
## Backend + Docker + k6 Load Testing

---

## 📌 Опис проєкту

Цей проєкт являє собою backend-додаток на **Spring Boot**, який працює з базою даних **MySQL**, запущеною в **Docker**.

Також у проєкті реалізовано **навантажувальне тестування** за допомогою інструменту **k6**.

---

## 🛠️ Технології

- Java 17+
- Spring Boot 4
- MySQL 8 (Docker)
- Maven
- k6 (load testing)

---

## 🚀 Запуск проєкту

### 1. Запуск бази даних (Docker)

```bash
docker run -d \
  --name mysql-laba \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=laba3_db \
  -p 3306:3306 \
  mysql:8
2. Запуск backend
./mvnw spring-boot:run

Сервер буде доступний за адресою:

http://localhost:8080
⚙️ Конфігурація (application.properties)
spring.datasource.url=jdbc:mysql://localhost:3306/laba3_db
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
🧪 Навантажувальне тестування (k6)
🔧 Запуск тестів
/d/k6/k6.exe run k6-tests/smoke-test.js
/d/k6/k6.exe run k6-tests/load-test.js
/d/k6/k6.exe run k6-tests/stress-test.js
📊 Типи тестів
🟢 Smoke Test
Перевіряє базову працездатність системи
Очікування: 100% успішних запитів
🟡 Load Test
Імітує нормальне навантаження (до 10 користувачів)
Перевіряє стабільність роботи
🔴 Stress Test
Імітує велике навантаження (до 30 користувачів)
Визначає межу системи
📈 Результати тестування
Тест	Результат
Smoke	✅ 100% success
Load	⚠️ ~20% помилок
Stress	❌ система не витримує
🔗 Архітектура
k6 → Spring Boot → MySQL (Docker)
🎯 Висновки
Система працює коректно при базових умовах
При збільшенні навантаження з’являються помилки
При високому навантаженні система стає нестабільною
📌 Автор

Студент: Петрушинець В.І.
Група: КІ-221
