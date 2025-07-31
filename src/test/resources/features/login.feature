@Smoke
Feature: Login Feature

  Scenario: Valid login with username, password, and level
    Given user is on login page
    When user enters username "admin"
    And user enters password "admin123"
    And user selects level "Advanced"
    And user clicks the login button
    Then user should see the dashboard
