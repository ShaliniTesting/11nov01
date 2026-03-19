# Age Calculator

A Java console application that calculates a user's exact age in years, months, and days based on their Date of Birth (DOB) entered in `DD/MM/YYYY` format. Built with the modern `java.time` API (`LocalDate`, `Period`, `DateTimeFormatter`) for accurate date handling — no deprecated `Date` or `Calendar` classes are used. The application features robust input validation (invalid dates, future dates, wrong format), graceful exception handling, and a repeat-calculation loop for performing multiple calculations in a single session.

## Prerequisites

- **Java 21 LTS** (OpenJDK 21 or later)
- **Apache Maven 3.9+** (3.9.12 recommended)

Verify your installations:

```bash
java -version
mvn -version
```

## Project Structure

```
age-calculator/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Main.java
│   │       └── AgeCalculator.java
│   └── test/
│       └── java/
│           └── AgeCalculatorTest.java
```

| File | Description |
|------|-------------|
| `pom.xml` | Maven project descriptor with Java 21 compiler configuration, JUnit 5 dependency, and build plugins |
| `Main.java` | Application entry point with Scanner-based input loop, DOB prompt, age output formatting, and exception handling |
| `AgeCalculator.java` | Core business logic for DOB parsing, date validation, and age computation using `java.time.LocalDate`, `java.time.Period`, and `java.time.format.DateTimeFormatter` |
| `AgeCalculatorTest.java` | JUnit 5 unit tests covering DOB parsing, age computation correctness, leap-year handling, and error cases |

## Build Instructions

All commands should be run from the project root directory.

**Compile the project:**

```bash
mvn compile
```

**Run the unit tests:**

```bash
mvn test
```

**Package the application into an executable JAR:**

```bash
mvn package
```

**Run the application:**

```bash
java -jar target/age-calculator-1.0-SNAPSHOT.jar
```

## Usage Examples

### Valid Input

```
Welcome to the Age Calculator!
Enter your Date of Birth to calculate your exact age.
Type "exit" or "quit" to stop.

Enter your Date of Birth (DD/MM/YYYY): 15/08/1998
Your age is 27 years, 6 months, and 15 days.
```

### Invalid Date

```
Enter your Date of Birth (DD/MM/YYYY): 31/02/2020
Invalid date. Please enter a valid date in DD/MM/YYYY format.
```

### Future Date

```
Enter your Date of Birth (DD/MM/YYYY): 15/08/2030
Date of birth cannot be in the future. Please enter a valid past date.
```

### Wrong Format

```
Enter your Date of Birth (DD/MM/YYYY): abc
Invalid date. Please enter a valid date in DD/MM/YYYY format.
```

### Exiting the Program

```
Enter your Date of Birth (DD/MM/YYYY): exit
Thank you for using the Age Calculator. Goodbye!
```

## Features

- **Exact age calculation** using `java.time.LocalDate` and `java.time.Period` — computes years, months, and days dynamically
- **Date-of-birth input** — accepts DOB in `DD/MM/YYYY` format using `DateTimeFormatter` with strict resolution
- **Comprehensive input validation** — rejects invalid dates (e.g., `31/02/2020`), future dates, and non-date input with clear error messages
- **Exception handling** — catches `DateTimeParseException` for invalid formats and `IllegalArgumentException` for future dates
- **Leap-year support** — correctly handles leap-year dates like `29/02/2000` using the native `java.time` API
- **Repeated calculations** — allows the user to calculate multiple ages without restarting the program
- **JUnit 5 unit test coverage** — comprehensive test suite validating DOB parsing, age computation, leap-year handling, and error cases

## Testing

Run the full test suite with:

```bash
mvn test
```

The project uses **JUnit 5.14.2** (Jupiter API) for unit testing. The test suite (`AgeCalculatorTest.java`) covers the following scenarios:

| Test Category | Description |
|---------------|-------------|
| Normal DOB computation | Verifies correct age for typical date inputs |
| Leap-year DOB | Validates correct handling of `29/02/2000` |
| Invalid date rejection | Confirms that impossible dates like `31/02/2020` are rejected |
| Future date rejection | Confirms that future dates are rejected |
| Wrong format rejection | Validates that non-date strings are rejected |

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 LTS | Programming language and runtime |
| Apache Maven | 3.9.12 | Build automation and dependency management |
| JUnit | 5.14.2 | Unit testing framework (Jupiter API) |
