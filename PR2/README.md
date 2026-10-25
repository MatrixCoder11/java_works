# Фінансовий аналізатор

Maven-проєкт (Lombok, JUnit 5). Читає транзакції з файлу `data/sample.csv` і виводить звіти.

- `mvn test` — тести
- `mvn exec:java` — запуск (з кореня проєкту)

Класи: `Transaction`, `ExpenseRange` (дані); `DataSource`, `FileDataSource` (джерела);
`TransactionCSVReader`, `TransactionAnalyzer`, `TransactionReportGenerator` (abstract, static-методи); `Main`.
