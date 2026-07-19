# E-Bank — Bank Account Management (Part 1: Backend)

Spring Boot backend for a bank account management app. A `Customer` owns
several `BankAccount`s; each account is either a `CurrentAccount` (with an
overdraft limit) or a `SavingAccount` (with an interest rate); each account
has a history of `AccountOperation`s (`DEBIT` / `CREDIT`).

## What's implemented here (Part 1 of the brief)

- [x] Spring Boot 3 project (Java 17, Maven)
- [x] JPA entities: `Customer`, `BankAccount` (abstract, `SINGLE_TABLE`
      inheritance), `CurrentAccount`, `SavingAccount`, `AccountOperation`
- [x] Spring Data JPA repositories: `CustomerRepository`,
      `BankAccountRepository`, `AccountOperationRepository`
- [x] DAO layer tests with `@DataJpaTest` + H2 (in-memory)
- [x] Service layer (`BankAccountService` / `BankAccountServiceImpl`) with
      DTOs and a manual mapper (`BankAccountMapperImpl`)
- [x] Service layer unit tests with Mockito
- [x] `@RestController`s for customers and accounts (CRUD, search,
      debit/credit/transfer, operation history)
- [x] Global exception handler → clean JSON error responses (404/400)
      instead of default Spring stack traces
- [x] Swagger / OpenAPI 3 via `springdoc-openapi-starter-webmvc-ui:2.1.0`
- [x] Dev data seeder (`CommandLineRunner`) so the API has data on first run
- [x] `.http` file with example requests for manual testing

## Not yet implemented (see the rest of the project brief)

- Part 2 — Angular client
- Part 3 — Spring Security + JWT authentication (this is *why* every
  entity already has a `createdBy` / `performedBy` string field ready to
  be filled from `SecurityContextHolder` once auth is wired in — don't
  need to touch the schema again for that)
- Part 4 — RAG chatbot + Telegram bot
- Part 5 — Chatbot integration into the account management app
- Dashboard with Chart.js / ng-chart
- User account management + password change

## Running it

```bash
mvn spring-boot:run
```

- API base URL: `http://localhost:8085`
- Swagger UI: `http://localhost:8085/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8085/v3/api-docs`
- H2 console: `http://localhost:8085/h2-console` (JDBC URL `jdbc:h2:mem:ebank-db`, user `sa`, empty password)

To run against MySQL instead of the in-memory H2 database:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
(edit `src/main/resources/application-mysql.properties` with your local
DB credentials first — create the schema, don't commit real creds).

## Running the tests

```bash
mvn test
```

## Main endpoints

| Method | Path                              | Description                         |
|--------|------------------------------------|--------------------------------------|
| GET    | `/customers`                       | list all customers                   |
| GET    | `/customers/search?keyword=`       | search customers by name             |
| GET    | `/customers/{id}`                  | get one customer                     |
| POST   | `/customers`                       | create a customer                    |
| PUT    | `/customers/{id}`                  | update a customer                    |
| DELETE | `/customers/{id}`                  | delete a customer                    |
| GET    | `/accounts`                        | list all bank accounts               |
| GET    | `/accounts/{id}`                   | get one account                      |
| POST   | `/accounts/current/{customerId}`   | open a current account for a customer|
| POST   | `/accounts/saving/{customerId}`    | open a saving account for a customer |
| POST   | `/accounts/debit`                  | debit an account                     |
| POST   | `/accounts/credit`                 | credit an account                    |
| POST   | `/accounts/transfer`               | transfer between two accounts        |
| GET    | `/accounts/{id}/operations`        | operation history for an account     |

## A note on secrets

There's no API key or secret checked into this repo. When Part 4 (the AI
chatbot) gets built, the OpenAI key must be read from an environment
variable (`OPENAI_API_KEY`) or a git-ignored `application-secrets.properties`
file — never hardcoded and never committed. If a key was ever pasted into a
chat, email, or shared doc, treat it as burned and regenerate it before
using it for anything real.

## Design notes / things worth comparing against your team's version

- Inheritance strategy: `SINGLE_TABLE` was chosen for `BankAccount` for
  simplicity (one `bank_account` table with a `TYPE` discriminator column).
  `JOINED` is the other common choice — normalizes better but adds a join
  on every account read. Worth discussing with your team which one they used.
- Account IDs are UUID strings rather than auto-increment longs, closer to
  a real account/IBAN-style identifier.
- DTO mapping is done by hand (`BeanUtils.copyProperties`) rather than
  MapStruct, to keep the dependency list minimal — swap in MapStruct if
  your course expects it.
- `createdBy` / `performedBy` fields are on the entities now (nullable,
  unused until Part 3) so the JWT integration later doesn't require a
  schema migration.
