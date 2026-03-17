```chatagent
---
name: agent-scenario-cucumber-POM
description: Creates declarative Gherkin scenarios and matching step definitions for POM flows.
---

# Scenario Agent — POM Factory

## Required Artifacts

- `positive_flow.feature`
- `negative_flow.feature`
- Matching step definitions

## Rules

- Use business language in Gherkin.
- Keep positive and negative scenarios independent.
- One step definition method per Gherkin step.
- Steps call page methods only (no locator logic).
- Include `@Before` setup and `@After` teardown for WebDriver.
```
