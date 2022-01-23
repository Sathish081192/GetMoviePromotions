package utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONObject;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.ParentError;
import pojo.ParentJSON;

public class CommonActions {
	public  RequestSpecification request;
	public Response response;
	public ExcelReader excelRead;
	public ConfigReader configRead;
	public Gson gson;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	
	public CommonActions() {
		excelRead=new ExcelReader();
		configRead=new ConfigReader();		
	}
	
	/*This method will initialize the Extent Report in Path target/CucumberReports/ExtentReport.html with Currentdate & time*/
	public static void initializeExtentReports() {
		htmlReporter = new ExtentHtmlReporter("target/CucumberReports/ExtentReport"+getcurrentdateandtime()+".html");
		htmlReporter.config().setDocumentTitle("API Automation Report"); // Title of report
		htmlReporter.config().setReportName("Get Promotion Details - API Report"); // Name of the report
		htmlReporter.config().setTheme(Theme.DARK);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host name", "localhost");
		extent.setSystemInfo("Environemnt", "QA");
		extent.setSystemInfo("user", "Sathish");
	}
	
	/*This method will Perform POST API Call based on Http Method
	 * Input: EndPoint & Request JSON
	 * Output: Response object reference*/
	public Response postMethod(String endpoint,JSONObject parentjsonObject) {		
		request=RestAssured.given().contentType(ContentType.JSON).body(parentjsonObject.toJSONString());
		response=request.request(Method.POST,endpoint);
		return response;
	}
	
	/*This method will Perform GET API Call based on Http Method
	 * Input: EndPoint
	 * Output: Response object reference*/
	public Response getMethod(String endPoint) {
		try {
		request=RestAssured.given();
		response=request.request(Method.GET,endPoint);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return response;
		
	}
	
	/*This method will Perform PUT API Call based on Http Method
	 * Input: EndPoint & Request JSON
	 * Output: Response object reference*/
	public Response putMethod(String endpoint,JSONObject parentjsonObject) {		
		request=RestAssured.given().contentType(ContentType.JSON).body(parentjsonObject.toJSONString());
		response=request.request(Method.PUT,endpoint);
		return response;
	}
	
	/*This method will Perform DELETE API Call based on Http Method
	 * Input: EndPoint & Request JSON
	 * Output: Response object reference*/
	public Response deleteMethod(String endpoint,JSONObject parentjsonObject) {		
		request=RestAssured.given().contentType(ContentType.JSON).body(parentjsonObject.toJSONString());
		response=request.request(Method.DELETE,endpoint);
		return response;
	}
	
	/*This method will Perform PATCH API Call based on Http Method
	 * Input: EndPoint & Request JSON
	 * Output: Response object reference*/
	public Response patchMethod(String endpoint,JSONObject parentjsonObject) {		
		request=RestAssured.given().contentType(ContentType.JSON).body(parentjsonObject.toJSONString());
		response=request.request(Method.PATCH,endpoint);
		return response;
	}
	
	public String readDataFromPropertyFile(String input) {
		ConfigReader configRead=new ConfigReader();
		Properties prop=configRead.init_prop();
		return prop.getProperty(input);
	}
	
	/*This method will read the data from Excel based on Excel Path and SheetName */
	public List<Map<String,String>> readDataFromExcelFile(String sheetName) throws InvalidFormatException, IOException {
		List<Map<String,String>> testData= excelRead.getData("./src/test/resources/TestData/inputExcelData.xlsx", sheetName);
		List<Map<String,String>> outputTestData=new ArrayList<Map<String,String>>();
		for(int i=0;i<testData.size();i++) {
			if((!testData.get(i).get("Execution_Flag").equalsIgnoreCase("No"))&&
					(!testData.get(i).get("Execution_Flag").isEmpty())){
				outputTestData.add(testData.get(i));
			}
		}
		return outputTestData;
	}
	
	
	/*This method will Perform API Call based on Http Method
	 * Input: MethodType, EndPoint & Request JSON
	 * Output: Response object reference*/
	public Response performAPICall_BasedonMethodType(String methodType,String endpoint,JSONObject jsonObject) {
		switch(methodType) {
		   case "GET":
			   response=getMethod(endpoint);
		   	   break;
		   case "POST":
			   response=postMethod(endpoint, jsonObject);
		   	   break;
		   case "PUT":
			   response=putMethod(endpoint, jsonObject);
		   	   break;
		   case "DELETE":
			   response=deleteMethod(endpoint, jsonObject);
		   	   break;
		   case "PATCH":
			   response=patchMethod(endpoint, jsonObject);
		   	   break;
	   }
	   return response;
	}
	
	/*This method will check whether given input is String value
	 * Returns TRUE if it is String'
	 * Returns FALSE if it is not String*/
	public boolean isValidString(String input) {
		boolean flag=true;
		for(int i=0;i<input.length();i++) {
		 if (!((input.charAt(i) >= 'a' && input.charAt(i) <= 'z')
		            || (input.charAt(i) >= 'A' && input.charAt(i) <= 'Z')
		            || (input.charAt(i) >= '0' && input.charAt(i) <= '9')
		            || (input.charAt(i) == '_')|| (input.charAt(i) == ' '))) {
			 flag=false;
			 break;
		 }
		}
		return flag;
	}
	
	/*This method will chek whether given input is valid String
	 * Returns TRUE if input is valid Program Type - EPISODE, MOVIE, SeRIES & SEASON
	 * Returns FALSE if input is not a valid Program Type*/
	public boolean isValidProgramType(String input) {
		boolean flag=true;
		if(!((input.equalsIgnoreCase("EPISODE"))||(input.equalsIgnoreCase("MOVIE"))||
				(input.equalsIgnoreCase("SERIES"))||(input.equalsIgnoreCase("SEASON")))){
			flag=false;
		}
		return flag;				
	}
	
	/*This method will check whether given input is Boolean value
	 * Returns TRUE if it is Boolean'
	 * Returns FALSE if it is not Boolean*/
	public boolean isBooleanValue(String input) {
		boolean flag=false;
		if((input.equalsIgnoreCase("true"))||(input.equalsIgnoreCase("false"))) {
			flag=true;
		}
		return flag;
	}
	
	/*This method used for decrypyting the encrypted password*/
	public String decryptPassword(String encodedString) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		String decodedString = new String(decodedBytes);
		return decodedString;		
	}
	
	/*This method used for encrypting the password string */
	public String encryptPassword(String decodedString) {
		String encodedString = Base64.getEncoder().encodeToString(decodedString.getBytes());
		return encodedString;	
		
	}
	
	/*This method will construct API endpoint based on Test case condition */
	public String constructEndpoint_BasedonAPIKeyType(String apiKeyType,String statusCode) {
		String endpoint=null;
		try {
		endpoint = readDataFromPropertyFile("GetPromotion_Endpoint");
		if (apiKeyType.equalsIgnoreCase("RemoveAPIKey")) {
			endpoint = endpoint.replaceAll("apikey=", "");
			endpoint = endpoint.replaceAll("[?]", "");
			} 
		else if (apiKeyType.equalsIgnoreCase("InvalidEndpoint")) {
			endpoint = endpoint.replaceAll("promotions", "1");
		}
		else if (apiKeyType.equalsIgnoreCase("null")) {}
		else if (statusCode.equals("200")){
			endpoint = endpoint + decryptPassword(apiKeyType);}
		else{
			endpoint=endpoint+apiKeyType;
		}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return endpoint;
	}
	
	/*This method will return current date and time*/
	public static String getcurrentdateandtime(){
		String str = null;
		try{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
		Date date = new Date();
		str= dateFormat.format(date);
		str = str.replace(" ", "").replaceAll("/", "").replaceAll(":", "");	
		}
		catch(Exception e){

		}
		return str;
}
	
	
}
