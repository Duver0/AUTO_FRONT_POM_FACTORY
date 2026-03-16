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

---

## Generating the Serenity Report

```bash
./gradlew reports
```

The HTML report is generated at `target/site/serenity/index.html`.

---

## Automated Scenarios

### Positive Flow — `positive_flow.feature`

**Scenario**: A registered user logs in with valid credentials and is redirected to the application dashboard.

Steps covered:
1. Navigate to the login page.
2. Enter a valid username and password.
3. Submit the login form.
4. Verify the dashboard is displayed.

---

### Negative Flow — `negative_flow.feature`

**Scenario**: A visitor attempts to log in with invalid credentials and sees an error message.

Steps covered:
1. Navigate to the login page.
2. Enter an invalid username and password.
3. Submit the login form.
4. Verify the error message is displayed.

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

