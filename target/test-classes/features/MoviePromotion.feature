Feature: Movie Promotion API Testing


  @GetPromotionDetails
  Scenario Outline: Validate Movie Promotion API Endpoint
    Given Define the API Endoint based on Test Data from Sheetname "<SheetName>" and TestcaseName "<Testcase_Name>"
    When Perform API call with given Parameters
    Then Validate the Response Statuscode that matches with Input Test data
    And Validate the Response Body with Required Assertions
  Examples:
  |SheetName          |Testcase_Name         |
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_001|
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_002|
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_003|
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_004|
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_005|
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_006|
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_007|
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_008|
  |GetPromotionDetails|TC_MOVIEPROMOTIONS_009|