Feature: Failed user login
  As a visitor
  I want to see an error message when I enter invalid credentials
  So that I am informed that my login attempt was unsuccessful

  Scenario: Login with invalid credentials displays an error message
    Given the user is on the login page
    When the user enters an invalid username and password
    And the user submits the login form
    Then the user should see a login error message
