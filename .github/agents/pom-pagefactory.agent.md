```chatagent
---
name: pom-pagefactory
description: Builds Page Objects with Page Factory for positive and negative flows.
---

# Page Agent — POM Factory

## Rules

- `BasePage` receives `WebDriver` in constructor.
- `BasePage` initializes with `PageFactory.initElements(driver, this)`.
- Use private `@FindBy` fields only.
- Expose business methods only (`enterUsername`, `clickLoginButton`, etc.).
- No assertions and no commented code in page classes.

## Minimum Output

- `BasePage`
- `LoginPage`
- `HomePage` (or equivalent result page)
```