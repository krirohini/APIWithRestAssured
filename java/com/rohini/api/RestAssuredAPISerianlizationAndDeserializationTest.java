package com.rohini.api;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;


public class RestAssuredAPISerianlizationAndDeserializationTest {

	private static final String BASE_URI = "https://demoqa.com";

	@Test
	public void UserRegistrationSuccessful() {
		RestAssured.baseURI = BASE_URI;
		RequestSpecification request = RestAssured.given();
		
		JSONObject requestParams = new JSONObject();
		requestParams.put("UserName", "test_rest");
		requestParams.put("Password", "rest@123");
		request.body(requestParams.toJSONString());
		
		Response response = request.post("/Account/v1/User");
		ResponseBody body = response.getBody();
		System.out.println("....response...." +response.asString());
		System.out.println("....response.getStatusCode()...." +response.getStatusCode());
				
		if (response.statusCode() == 200) {

			// Deserialize the Response body into JSONFailureResponse
			FailureResponsseClass responseBody = body.as(FailureResponsseClass.class);

			// Use the JSONFailureResponse class instance to Assert the values of Response.
			Assert.assertEquals("User already exists", responseBody.fault);
			Assert.assertEquals("FAULT_USER_ALREADY_EXISTS", responseBody.fault);
		} else if (response.statusCode() == 201) {

			// Deserialize the Response body into JSONSuccessResponse
			SuccessResponseClass responseBody = body.as(SuccessResponseClass.class);

			// Use the JSONSuccessResponse class instance to Assert the values of response.
			Assert.assertEquals("OPERATION_SUCCESS", responseBody.successCode);
			Assert.assertEquals("Operation completed successfully", responseBody.message);
		}
	}

}
