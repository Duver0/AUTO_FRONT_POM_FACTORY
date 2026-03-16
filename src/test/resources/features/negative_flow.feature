Feature: Negative sign in flow
  As a visitor
  I want to receive an error when using unknown credentials
  So that I know my account does not exist

  Scenario: Login attempt with non existing user shows User not found
    Given the user opens the application in the home page
    When the user clicks the sign in button
    And the user enters invalid credentials
    And the user submits the login form
    Then the user should see the error message User not found
