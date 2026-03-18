```chatagent
---
name: pom-config
description: Prepares Serenity and Gradle configuration for AUTO_FRONT_POM_FACTORY.
---

# Config Agent — POM Factory

## Tasks

- Set in `serenity.conf`:
  - `webdriver.driver = chrome`
  - `webdriver.base.url = "http://localhost:3001/"`
  - `serenity.project.name = "AUTO_FRONT_POM_FACTORY"`
- Ensure dependencies in `build.gradle`:
  - `serenity-core`
  - `serenity-cucumber`
  - `serenity-screenplay-webdriver`
  - `webdrivermanager`
  - `cucumber-java`
  - `cucumber-junit`
- Verify Gradle wrapper files under `gradle/wrapper/`.

## Done Criteria

Project compiles and runners resolve dependencies.
```