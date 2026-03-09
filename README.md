# SafeBank API

RESTful Banking API built with Spring Boot.

This project demonstrates a simple banking system with account management, transactions, and money transfers.

## Features

* Create bank accounts
* Deposit money
* Withdraw money
* Transfer money between accounts
* Lock / unlock accounts
* Transaction history
* Validation and error handling

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Database
* Maven
* JUnit & MockMvc

## API Endpoints

### Create Account

POST /accounts

```
{
  "id": "AC-DE-2026-01",
  "owner": "Peter Parker"
}
```

---

### Deposit

POST /accounts/{id}/deposit

```
{
  "amount": 100
}
```

---

### Withdraw

POST /accounts/{id}/withdraw

```
{
  "amount": 50
}
```

---

### Transfer Money

POST /accounts/transfer

```
{
  "fromAccount": "AC-DE-2026-01",
  "toAccount": "AC-DE-2026-02",
  "amount": 100
}
```

---

### Get Balance

GET /accounts/{id}/balance

---

### Transaction History

GET /accounts/{id}/transactions

---

## Example Transaction Response

```
[
  {
    "id": 1,
    "type": "DEPOSIT",
    "amount": 100,
    "balanceAfter": 100,
    "timestamp": "2026-03-09T14:10:00"
  }
]
```

---

## Project Structure

```
controller
service
repository
domain
dto
exception
```

The project follows a layered architecture separating business logic, persistence, and API models.

---

## Running the Project

```
mvn spring-boot:run
```

Application will start on:

```
http://localhost:8080
```

---

## Tests

Integration tests are implemented using MockMvc.

Run tests with:

```
mvn test
```
or

You can test all endpoints using the included request.http file.
