# SafeBank API

RESTful Banking API, entwickelt mit Spring Boot.

Dieses Projekt implementiert ein einfaches Banksystem mit Kontoverwaltung, Transaktionen und Geldüberweisungen zwischen Konten.

## Features

* Bankkonto erstellen
* Geld einzahlen (Deposit)
* Geld abheben (Withdraw)
* Geld zwischen Konten transferieren
* Konto sperren / entsperren
* Transaktionshistorie anzeigen
* Validierung und Fehlerbehandlung

## Technologien

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Datenbank
* Maven
* JUnit & MockMvc

## API Endpoints

### Konto erstellen

POST /accounts

```json
{
  "id": "AC-DE-2026-01",
  "owner": "Peter Parker"
}
```

---

### Einzahlung

POST /accounts/{id}/deposit

```json
{
  "amount": 100
}
```

---

### Auszahlung

POST /accounts/{id}/withdraw

```json
{
  "amount": 50
}
```

---

### Geldüberweisung

POST /accounts/transfer

```json
{
  "fromAccount": "AC-DE-2026-01",
  "toAccount": "AC-DE-2026-02",
  "amount": 100
}
```

---

### Kontostand abrufen

GET /accounts/{id}/balance

---

### Transaktionshistorie

GET /accounts/{id}/transactions

---

## Beispiel: Transaktionsantwort

```json
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

## Projektstruktur

```
controller
service
repository
domain
dto
exception
```

Das Projekt folgt einer **Layered Architecture**, bei der API, Business-Logik und Datenzugriff voneinander getrennt sind.

---

## Projekt starten

```bash
mvn spring-boot:run
```

Die Anwendung läuft anschließend unter:

```
http://localhost:8080
```

---

## Tests

Integrationstests wurden mit **JUnit und MockMvc** implementiert.

Tests ausführen:

```bash
mvn test
```

---

## API testen

Alle Endpoints können über die Datei **request.http** getestet werden, die im Projekt enthalten ist.
