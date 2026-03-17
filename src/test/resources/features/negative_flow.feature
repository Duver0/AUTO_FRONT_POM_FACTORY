Feature: Negative sign in flow
  As a visitor
  I want to receive an error when using unknown credentials
  So that I know my account does not exist

  @negative_login
  Scenario: Visitor receives validation feedback for invalid credentials
    Given the customer is on the sign in page
    When the customer authenticates with invalid credentials
    Then the customer should see an authentication error message
