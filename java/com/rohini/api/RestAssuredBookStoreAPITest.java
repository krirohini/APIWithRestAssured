package com.rohini.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredBookStoreAPITest {

	private static final String BASE_URI = "https://demoqa.com/BookStore/v1";
	private static final String BASE_POST_URI = "https://demoqa.com/BookStore/v1/Books";
	private static final String BASE_BOOKS_URI = "https://demoqa.com/BookStore/v1/Books";

	@Test(enabled = false, description = "GET call for REST API using Rest Assured")
	public void getBooksDetailsTest() {

		// Specify the base URL to the RESTful web service
		RestAssured.baseURI = BASE_BOOKS_URI;

		// Get the RequestSpecification of the request to be sent to the server.
		RequestSpecification httpRequest = RestAssured.given();

		// specify the method type (GET) and the parameters if any.
		// In this case the request does not take any parameters
		Response response = httpRequest.request(Method.GET, "");

		// Print the status and message body of the response received from the server
		System.out.println("Status received => " + response.getStatusLine());
		System.out.println("Response=>" + response.prettyPrint());

	}

	@Test(enabled = false, description = "Validate Response after passing Query Parameters using Rest Assured")
	public void validateResponseForGvnQueryParameter() {

		// Specify the base URL to the RESTful web service
		RestAssured.baseURI = BASE_URI;

		// Get the RequestSpecification of the request to be sent to the server.
		RequestSpecification httpRequest = RestAssured.given();

		// Passing the resource details
		Response response = httpRequest.queryParam("ISBN", "9781449325862").get("/Book");
		
		// Assert that correct status code is returned.
		Assert.assertEquals(response.getStatusCode(), 200 , ".....Response Code did NOT match...." + response.asString() );
		
		/*Validate Response Status Line for - Http protocol version, Status Code, Status Code's string value*/
		/** 
		 * Ex:- Status line will look something like this "HTTP/1.1 200 OK". 
		 * First part is Http protocol (HTTP/1.1). 
		 * Second is Status Code (200). 
		 * Third is the Status message (OK).
		 */
		Assert.assertEquals(response.getStatusLine(),  "HTTP/1.1 200 OK",   "......StatusLine did NOT match......");
		
		// Converting the response body to string object
		String rbdy = response.asString();
		//System.out.println(".....Response.....=>" + rbdy);
		System.out.println(".....Response....." + response.prettyPrint());

		
		// Creating object of JsonPath and passing the string response body as parameter
		JsonPath jpath = new JsonPath(rbdy);
		
		// Storing publisher name in a string variable
		String title = jpath.getString("title");
		Assert.assertEquals(title, "Git Pocket Guide", ".....Book Title did NOT match...." + jpath );
		System.out.println("......The book title is....." + title);
	}
	
	@Test(enabled = true, description = "Validate Response after passing Query Parameters using Rest Assured")
	public void validatePostForUnauthorizedUser() throws IOException {

		// Specify the base URL to the RESTful web service
		RestAssured.baseURI = BASE_URI;
		
		// Get the json payload to perform POST.
		String payload = generatePayLoad("src/test/resources/requestPayloads/postBooksPayload.json");
		
		// Get the RequestSpecification of the request to be sent to the server.
		RequestSpecification httpRequest = RestAssured.given();
		
		// Add a header stating the Request body is a JSON 
		httpRequest.header("Content-Type", "application/json"); // Add the Json to the body of the request 
		httpRequest.body(payload); // Post the request and check the response

		// Passing the payload to call POST api.
		Response response = httpRequest.post(BASE_POST_URI);
		
		// Assert that correct status code is returned.
		Assert.assertEquals(response.getStatusCode(), 401 , ".....Response Code did NOT match....\n" + response.asString() );
		
		/*Validate Response Status Line for - Http protocol version, Status Code, Status Code's string value*/
		/** 
		 * Ex:- Status line will look something like this "HTTP/1.1 401 Unauthorized". 
		 * First part is Http protocol (HTTP/1.1). 
		 * Second is Status Code (401). 
		 * Third is the Status message (Unauthorized).
		 */
		Assert.assertEquals(response.getStatusLine(),  "HTTP/1.1 401 Unauthorized",   "......StatusLine did NOT match......");
			
		// Converting the response body to string object
		String rbdy = response.asString();
		//System.out.println(".....Response.....=>" + rbdy);
		System.out.println(".....Response....." + response.prettyPrint());

		
		// Creating object of JsonPath and passing the string response body as parameter
		JsonPath jpath = new JsonPath(rbdy);
		
		// Storing publisher name in a string variable
		Assert.assertEquals(jpath.getString("code"), "1200", ".....code did NOT match...." + jpath );
		Assert.assertEquals(jpath.getString("message"), "User not authorized!", ".....code did NOT match...." + jpath );
	}
	
	@Test(enabled = false, description = "Validate Response after passing Query Parameters using Rest Assured")
	public void validatePostForGvnPayload() throws IOException {

		// Specify the base URL to the RESTful web service
		RestAssured.baseURI = BASE_URI;
		
		// Get the json payload to perform POST.		
		String payload = generatePayLoad("src/test/resources/requestPayloads/postBooksPayload.json");
		
		// Get the RequestSpecification of the request to be sent to the server.
		RequestSpecification httpRequest = RestAssured.given();
		
		// Add a header stating the Request body is a JSON 
		httpRequest.header("Content-Type", "application/json"); // Add the Json to the body of the request 
		httpRequest.body(payload); // Post the request and check the response

		// Passing the payload to call POST api.
		Response response = httpRequest.post(BASE_POST_URI);
		
		// Assert that correct status code is returned.
		Assert.assertEquals(response.getStatusCode(), 201 , ".....Response Code did NOT match....\n" + response.asString() );
		
		/*Validate Response Status Line for - Http protocol version, Status Code, Status Code's string value for Unauthorized Users */
		/** 
		 * Ex:- Status line will look something like this "HTTP/1.1 201 Success". 
		 * First part is Http protocol (HTTP/1.1). 
		 * Second is Status Code (201). 
		 * Third is the Status message (Success).
		 */
		Assert.assertEquals(response.getStatusLine(),  "HTTP/1.1 201 Success",   "......StatusLine did NOT match......");
		
		
	}
	
	public String generatePayLoad(String filepath) throws IOException {
        String payload = Files.readString(Paths.get(filepath));
		System.out.println("\n.....Request payload.....\n" + payload);
		return payload;
	}

}
