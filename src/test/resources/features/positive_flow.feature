Feature: Successful user login
  As a registered user
  I want to log in with valid credentials
  So that I can access the application dashboard

  Scenario: Login with valid credentials grants access to the dashboard
    Given the user is on the login page
    When the user enters a valid username and password
    And the user submits the login form
    Then the user should be redirected to the dashboard
