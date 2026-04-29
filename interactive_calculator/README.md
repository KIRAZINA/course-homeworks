# Interactive Calculator

A simple calculator with a frontend (HTML/CSS/JavaScript) and a Spring Boot backend.
It can work with or without the server.

## Features

* Basic operations: `+ - * /`
* Decimals and parentheses
* Keyboard support
* Backspace and clear
* Error handling (e.g. division by zero)
* Calculation history (when backend is running)

## How to run

### Without backend (easiest way)

Open this file in a browser:

```
src/main/resources/static/index.html
```

### With backend

Requirements: Java 21, Maven

```bash
mvn spring-boot:run
```

Then open:

```
http://localhost:8080
```

## Tech stack

* Frontend: HTML, CSS, JavaScript (no frameworks)
* Backend: Spring Boot, Java 21
* Build tool: Maven

## How it works

* Frontend handles input and can calculate on its own
* If backend is available, it sends expressions to the server
* Backend parses expressions and stores last 10 results

## API

**POST /api/calculate**

```json
{ "expression": "2 + 2 * 2" }
```

**GET /api/history**
Returns recent calculations

## Notes

* Works even if backend is down
* Uses a custom parser (no eval)
* Uses BigDecimal for better precision