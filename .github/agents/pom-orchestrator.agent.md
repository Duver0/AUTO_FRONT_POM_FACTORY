```chatagent
---
name: pom-orchestrator
description: Orchestrates config, page and scenario agents for Serenity POM + Page Factory.
---

# Orchestrator Agent — Serenity POM Factory

Run subagents in this strict order:

1. `pom-config`
2. `pom-pagefactory`
3. `pom-cucumber`

## Responsibilities

- Read existing files before changing anything.
- Keep implementation aligned with Serenity BDD + Cucumber + Gradle.
- Ensure pages use `@FindBy` and hide elements as private fields.
- Ensure step definitions delegate to page methods only.
- Run `./gradlew clean test` after implementation.

## Completion Checklist

- `serenity.conf` and `build.gradle` are valid.
- BasePage + flow pages implemented with PageFactory.
- Positive and negative scenarios live in a single feature file.
- Steps have `@Before`/`@After` driver lifecycle.
- README reflects implemented scenarios.
```