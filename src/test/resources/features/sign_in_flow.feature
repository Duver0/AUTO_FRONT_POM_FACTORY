Feature: Sign in flow
  As a visitor
  I want to access the system with my credentials
  So that I can either enter the authenticated area or receive feedback when access is invalid

  @positive_login
  Scenario: Registered customer accesses authenticated area
    Given the customer is on the sign in page
    When the customer authenticates with valid credentials
    Then the customer should access the authenticated area

  @negative_login
  Scenario: Visitor receives validation feedback for invalid credentials
    Given the customer is on the sign in page
    When the customer authenticates with invalid credentials
    Then the customer should see an authentication error message