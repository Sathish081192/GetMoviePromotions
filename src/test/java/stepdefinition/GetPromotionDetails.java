package stepdefinition;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONObject;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.Gson;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import pojo.ParentError;
import pojo.ParentJSON;
import pojo.Promotions;
import utils.CommonActions;

public class GetPromotionDetails {
	private List<Map<String, String>> testData;
	private Map<String, String> currentIterationMap;
	private String endpoint;
	private CommonActions commonAction;
	private Response response;
	private JSONObject jsonObject = new JSONObject();
	private ExtentTest extTest;
	private Gson gson;
	
	public GetPromotionDetails() {
		commonAction = new CommonActions();
		gson = new Gson();
	}

	@Given("Define the API Endoint based on Test Data from Sheetname {string} and TestcaseName {string}")
	public void define_the_api_endoint_based_on_test_data(String sheetName, String expectedTCName)
			throws InvalidFormatException, IOException {
			testData = commonAction.readDataFromExcelFile(sheetName);
			for (int i = 0; i < testData.size(); i++) {
				if (testData.get(i).get("TC_NAME").equalsIgnoreCase(expectedTCName)) {
					currentIterationMap = testData.get(i);
					break;} 
				else {
					currentIterationMap = null;	}}
			if (!(currentIterationMap == null)) {
				try {
				extTest=CommonActions.extent.createTest(expectedTCName+" - "+currentIterationMap.get("TC_Description"));
				String apiKeyType=currentIterationMap.get("API_Key");
				endpoint=commonAction.constructEndpoint_BasedonAPIKeyType(apiKeyType,currentIterationMap.get("Expected_StatusCode"));
				extTest.log(Status.PASS, endpoint);
			}
			catch (Exception e) {
			extTest.log(Status.FAIL, e.getStackTrace().toString());
		}}
	}

	@When("Perform API call with given Parameters")
	public void perform_api_call_with_given_Parameters() {
		if (!(currentIterationMap == null)) {
		String methodType=null;
		try {
		
			methodType = currentIterationMap.get("Method_Type");
			response = commonAction.performAPICall_BasedonMethodType(methodType, endpoint, jsonObject);
		extTest.log(Status.PASS, "API Call is Successful with methodType: "+methodType);
		}
		catch(Exception e) {
			extTest.log(Status.FAIL, e.getStackTrace().toString());
		}
		}
	}

	@Then("Validate the Response Statuscode that matches with Input Test data")
	public void validate_the_response_statuscode_that_matches_with_input_test_data() {
		if (!(currentIterationMap == null)) {
		try {
			Assert.assertEquals(Integer.parseInt(currentIterationMap.get("Expected_StatusCode")), response.getStatusCode());
			extTest.info("Expected Status Code: "+currentIterationMap.get("Expected_StatusCode")+" || "+" "
				+ " Actual Status Code from API Call: "+response.getStatusCode());
			extTest.log(Status.PASS,"Status Code matched with expected Status Code Successfully");
		}
		catch(Exception e) {
			e.printStackTrace();
			extTest.log(Status.FAIL, e.getStackTrace().toString());
		}
		}
	}

	@And("Validate the Response Body with Required Assertions")
	public void validate_the_response_body_that_matches_with_input_test_data() {
		if (!(currentIterationMap == null)) {
			try {
			if(currentIterationMap.get("Expected_StatusCode").equals("200")) {		
				ParentJSON validResponse=gson.fromJson(response.getBody().asString(), ParentJSON.class);
				validateResponse_validStatusCode(validResponse);}
			else if(currentIterationMap.get("Expected_StatusCode").equals("500")) {
				validateXMLResponse(response.getBody().asString());
			}
			else {
				ParentError invalidResponse=gson.fromJson(response.getBody().asString(), ParentError.class);
				validateResponse_InvalidStatusCode(invalidResponse);							
			}
			}
			catch(Exception e) {
				e.printStackTrace();
				extTest.log(Status.FAIL, e.getStackTrace().toString());
			}
		}
	}
	
	public void validateResponse_InvalidStatusCode(ParentError invalidResponse) {
		extTest.info("Expected Response Message: "+currentIterationMap.get("Expected_Response_Message")+" || "+" "
			+ " Actual Response Message: "+invalidResponse.getError().getMessage());
		Assert.assertEquals(currentIterationMap.get("Expected_Response_Message"), invalidResponse.getError().getMessage());
		extTest.log(Status.PASS,"Response Message matched with expected Response Message Successfully");
		extTest.info("Expected Response Code: "+currentIterationMap.get("Expected_code")+" || "+" "
			+ " Actual Response Code: "+invalidResponse.getError().getCode());
		Assert.assertEquals(currentIterationMap.get("Expected_code"), invalidResponse.getError().getCode());
		extTest.log(Status.PASS,"Response Code matched with expected Response Code Successfully");
		extTest.info("Actual Request.ID from Response: "+invalidResponse.getError().getRequestId());
		Assert.assertNotNull(invalidResponse.getError().getRequestId());
		extTest.log(Status.PASS, "Request.ID is populated successfully and it's not Null");	
	}
	
	public void validateResponse_validStatusCode(ParentJSON validResponse) {
		for(int i=0;i<validResponse.getPromotion().size();i++) {
			String promotionID=validResponse.getPromotion().get(i).getPromotionId();
			extTest.info("PromotionID from Response: "+promotionID);
			Assert.assertTrue(commonAction.isValidString(promotionID));
			extTest.log(Status.PASS, "Promotion ID is populated successfully in Response with valid String format");
			extTest.info("OrderID from Response: "+validResponse.getPromotion().get(i).getOrderId()+" for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
			Assert.assertNotNull(validResponse.getPromotion().get(i).getOrderId());
			extTest.log(Status.PASS, "OrderID is Populated Successfully");
			extTest.info("PromoArea from Response: "+validResponse.getPromotion().get(i).getPromoArea().toString()+" for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
			Assert.assertNotNull(validResponse.getPromotion().get(i).getPromoArea());
			extTest.log(Status.PASS, "PromoArea is Populated Successfully");
			extTest.info("PromoType from Response: "+validResponse.getPromotion().get(i).getPromoType().toString());
			Assert.assertNotNull(validResponse.getPromotion().get(i).getPromoType().toString());
			extTest.log(Status.PASS, "PromoType is Populated Successfully");
			String showPrice=String.valueOf(validResponse.getPromotion().get(i).isShowPrice());
			extTest.info("ShowPrice from Response: "+showPrice+" for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
			Assert.assertTrue(commonAction.isBooleanValue(showPrice));
			extTest.log(Status.PASS, "showPrice is Populated with Boolean Format Successfully");
			String showText=String.valueOf(validResponse.getPromotion().get(i).isShowText());
			extTest.info("ShowText from Response: "+showText+" for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
			Assert.assertTrue(commonAction.isBooleanValue(showText));
			extTest.log(Status.PASS, "showText is Populated with Boolean Format Successfully");
			extTest.info("AR Value under localizedTexts JSONObject: "+validResponse.getPromotion().get(i).getLocalizedTexts().getAr().toString()
					+" for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
			Assert.assertTrue(validResponse.getPromotion().get(i).getLocalizedTexts().getAr().size()>0);
			extTest.log(Status.PASS, "AR Value is populated successfully under localizedTexts JSONObject for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
			extTest.info("EN Value under localizedTexts JSONObject: "+validResponse.getPromotion().get(i).getLocalizedTexts().getEn().toString()
					+" for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
			Assert.assertTrue(validResponse.getPromotion().get(i).getLocalizedTexts().getEn().size()>0);
			extTest.log(Status.PASS, "EN Value is populated successfully under localizedTexts JSONObject for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
			int propertySize=validResponse.getPromotion().get(i).getProperties().size();
			for(int j=0;j<propertySize;j++) {
				String programType=validResponse.getPromotion().get(i).getProperties().get(j).getProgramType();
				extTest.info("ProgramType from Response: "+validResponse.getPromotion().get(i).getProperties().get(j).getProgramType()+" for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());
				if(!((programType==null)||(programType.length()==0))) {
					Assert.assertTrue(commonAction.isValidProgramType(validResponse.getPromotion().get(i).getProperties().get(j).getProgramType()));
					extTest.log(Status.PASS, "ProgramType value is populated correctly under Properties JSONObject");}
				else {
					extTest.log(Status.FAIL, "ProgramType value is populated as NULL under Properties JSONObject for Promotion.ID: "+validResponse.getPromotion().get(i).getPromotionId());}
				}
					}	
			
		}
	
		public void validateXMLResponse(String responseBody) {
		DocumentBuilderFactory dbfactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuilder;
		try {
			dbuilder=dbfactory.newDocumentBuilder();
			Document doc=dbuilder.parse(new InputSource(new StringReader(responseBody)));
			doc.getDocumentElement().normalize();
			NodeList errorMainDTO=doc.getElementsByTagName("errorMainDTO");
			Element error=null;
			for(int i=0;i<errorMainDTO.getLength();i++) {				
				error=(Element) errorMainDTO.item(i);
				Node message=error.getElementsByTagName("message").item(0).getFirstChild();
				String messageID=message.getNodeValue();
				Node codeNode=error.getElementsByTagName("code").item(0).getFirstChild();
				String code=codeNode.getNodeValue();	
				extTest.info("Expected Response MessageID: "+currentIterationMap.get("Expected_Response_Message")+" || "+" "
						+ " Actual Response Message: "+messageID);
				Assert.assertEquals(currentIterationMap.get("Expected_Response_Message"), messageID);
				extTest.log(Status.PASS,"Response Message matched with expected Response Message Successfully");
				extTest.info("Expected Response MessageID: "+currentIterationMap.get("Expected_code")+" || "+" "
						+ " Actual Response Message: "+code);
				Assert.assertEquals(currentIterationMap.get("Expected_code"), code);
				extTest.log(Status.PASS,"Response code matched with expected Response Code Successfully");
			}
		}
		catch(Exception e) {
		extTest.log(Status.FAIL,e.getStackTrace().toString());
		e.printStackTrace();}
		
	}
}
