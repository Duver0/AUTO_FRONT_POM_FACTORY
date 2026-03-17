Feature: Positive sign in flow
  As a registered user
  I want to log in with valid credentials
  So that I can access authenticated options

  @positive_login
  Scenario: Registered customer accesses authenticated area
    Given the customer is on the sign in page
    When the customer authenticates with valid credentials
    Then the customer should access the authenticated area
