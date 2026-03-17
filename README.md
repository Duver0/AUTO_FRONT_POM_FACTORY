# AUTO_FRONT_POM_FACTORY

## Project Description

Front-end automated test suite for the locally running web application at `http://localhost:3001/`. The project follows the **Page Object Model (POM)** pattern with **Page Factory** (`@FindBy` annotations), built on top of **Serenity BDD** and driven by **Cucumber** (Gherkin) scenarios executed with **JUnit**.

---

## Prerequisites

| Requirement | Version |
|---|---|
| Java (JDK) | 11+ |
| Gradle | 8+ (wrapper included) |
| Browser | Google Chrome (latest) |
| Target application | Running at `http://localhost:3001/` |

> The web application must be running locally at `http://localhost:3001/` before executing the tests.

---

## Running the Tests

```bash
./gradlew clean test
```

## Running Tests in Parallel

This project executes Cucumber runners in parallel using Gradle test forks.

```bash
./gradlew clean test
```

Parallelism is configured in `build.gradle` with:

```gradle
test {
  useJUnit()
  maxParallelForks = 2
}
```

---

## Generating the Serenity Report

```bash
./gradlew reports
```

Open the generated report on Linux:

```bash
xdg-open target/site/serenity/index.html
```

The HTML report is generated at `target/site/serenity/index.html`.

---

## Automated Scenarios

### Positive Flow — `positive_flow.feature`

**Scenario**: Registered customer accesses authenticated area.

Steps covered:
1. Open the sign in page.
2. Authenticate with valid credentials.
3. Verify access to authenticated area.

---

### Negative Flow — `negative_flow.feature`

**Scenario**: Visitor receives validation feedback for invalid credentials.

Steps covered:
1. Open the sign in page.
2. Authenticate with invalid credentials.
3. Verify authentication error message.

---

## Project Structure

```
src/
  test/
    java/
      runner/
        CucumberTestRunner.java   # JUnit + Serenity runner
      pages/
        BasePage.java             # PageFactory initialisation
        LoginPage.java            # Login page object
        HomePage.java             # Dashboard/result page object
      steps/
        LoginSteps.java           # Cucumber step definitions
    resources/
      features/
        positive_flow.feature     # Successful login scenario
        negative_flow.feature     # Failed login scenario
serenity.conf                     # Serenity + WebDriver configuration
build.gradle                      # Gradle build & dependencies
```

