Feature: S3 and Snowflake Test

  @Test
  Scenario: S3 Test
    Given I load "" to "" bucket and "" catalog


  @Test
  Scenario: Snowflake Test
    Given I run query from file ""
    And I verify number of rows in Table "" equals "" using query from file "" on schema ""
    And I verify column values in Table "" using query from file "" on schema ""
       | columnName | expectedValue |
       |            |               |
    And I validate table "" on schema "" using query "" and "" expected result