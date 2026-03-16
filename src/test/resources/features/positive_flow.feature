Feature: Positive sign in flow
  As a registered user
  I want to log in with valid credentials
  So that I can access authenticated options

  Scenario: Login with existing user shows Registro button
    Given the user opens the application in the home page
    When the user clicks the sign in button
    And the user enters valid credentials
    And the user submits the login form
    Then the user should see the Registro button as authenticated user
