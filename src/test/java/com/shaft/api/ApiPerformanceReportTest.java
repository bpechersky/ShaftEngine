package com.shaft.api;

import com.shaft.driver.SHAFT;


import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.shaft.driver.SHAFT;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.AfterSuite;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class ApiPerformanceReportTest {

    @BeforeSuite
    public void disableReportAutoOpen() {
        System.setProperty("shaft.report.alwaysOpen", "false");
    }


    String token;
    SHAFT.API api = new SHAFT.API("https://restful-booker.herokuapp.com/");

    @Test
    public void testALogin() {
        Response response = api.post("auth").
                setContentType("application/json").
                setRequestBody("{\n" +
                        "    \"username\" : \"admin\",\n" +
                        "    \"password\" : \"password123\"\n" +
                        "}").
                perform();


        JsonPath jsonPath = response.jsonPath();
        System.out.println(jsonPath.get("token").toString());


        token = response.path("token").toString();
        token = response.jsonPath().get("token").toString();
        System.out.println("The Tonken is: " + token);

    }

    @Test
    public void testGetALLBookingIds() {

        api.get("booking").
                perform();

    }

    @Test
    public void testGetBookingByName() {
        List<List<Object>> parameters = Arrays.asList(Arrays.asList("firstname", "Jim"), Arrays.asList("lastname", "Brown"));

        api.get("booking").
                setParameters(parameters, RestActions.ParametersType.QUERY).
                perform();

    }

    @Test
    public void testGetBooking() {
        SHAFT.API api = new SHAFT.API("https://restful-booker.herokuapp.com/"); // ✅ No trailing slash
        api.get("booking/1")
                .setTargetStatusCode(200)
                .perform();

        System.out.println("Status code: " + api.getResponseStatusCode());
        System.out.println("Response: " + api.getResponseBody());
    }


    @Test
    public void testCreateBookingJSON() {
        api.post("booking").
                setContentType("application/json").
                setRequestBody("{\"firstname\":\"Jim\",\"lastname\":\"Brown\",\"totalprice\":111,\n" +
                        " \"depositpaid\":true,\"bookingdates\":{\"checkin\":\"2018-01-01\",\"checkout\":\"2019-01-01\"},\n" +
                        " \"additionalneeds\":\"Breakfast\"}").
                perform();
    }


    @Test
    public void testGetUsers() {
        SHAFT.API api = new SHAFT.API("https://reqres.in/");
        List<List<Object>> parameters = Arrays.asList(Arrays.asList("page", "2"));
        api.get("api/users")
                .setParameters(parameters, RestActions.ParametersType.QUERY)
                .addHeader("x-api-key", "reqres-free-v1") // ✅ Required
                .perform();

        int statusCode = api.getResponseStatusCode();
        String responseBody = api.getResponseBody();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        org.testng.Assert.assertEquals(statusCode, 200, "Expected HTTP 200 OK");
    }

    @Test
    public void testGetUser() {
        SHAFT.API api = new SHAFT.API("https://reqres.in/");
        api.get("api/users/2").perform();
        api.assertThatResponse()
                .extractedJsonValue("data.id")
                .isEqualTo("2") // Compare using string literal
                .perform();
    }

    @Test
    public void testCreateUser() {
        SHAFT.API api = new SHAFT.API("https://reqres.in/");
        api.post("api/users")
                .setContentType("application/json")
                .addHeader("x-api-key", "reqres-free-v1") // ✅ API Key here
                .setRequestBody("{\n" +
                        "  \"name\": \"morpheus\",\n" +
                        "  \"job\": \"leader\"\n" +
                        "}")
                .perform();

        int statusCode = api.getResponseStatusCode();
        String responseBody = api.getResponseBody();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response: " + responseBody);

        Assert.assertEquals(statusCode, 201, "Expected HTTP 201 Created");
    }

    @Test
    public void testRegisterUser() {
        SHAFT.API api = new SHAFT.API("https://reqres.in/");

        api.post("api/register")
                .setContentType("application/json")
                .addHeader("x-api-key", "reqres-free-v1")  // ✅ REQUIRED
                .setRequestBody("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .perform();

        int statusCode = api.getResponseStatusCode();
        String responseBody = api.getResponseBody();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        org.testng.Assert.assertEquals(statusCode, 200, "Expected 200 OK");
    }


    @Test
    public void testLoginUser() {
        SHAFT.API api = new SHAFT.API("https://reqres.in/");

        api.post("api/login")
                .setContentType("application/json")
                .addHeader("x-api-key", "reqres-free-v1") // ✅ required for all requests now
                .setRequestBody("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .perform();

        int statusCode = api.getResponseStatusCode();
        String response = api.getResponseBody();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + response);

        org.testng.Assert.assertEquals(statusCode, 200, "Expected 200 OK from login");
    }


    @Test
    public void testDelayedResponse() {
        SHAFT.API api = new SHAFT.API("https://reqres.in/");
        List<List<Object>> parameters = Arrays.asList(Arrays.asList("delay", "3"));

        api.get("api/users")
                .setParameters(parameters, RestActions.ParametersType.QUERY)
                .addHeader("x-api-key", "reqres-free-v1") // ✅ Required API key
                .perform();

        int statusCode = api.getResponseStatusCode();
        String responseBody = api.getResponseBody();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response: " + responseBody);
    }

    @Test
    public void testGetActivities() {
        SHAFT.API api = new SHAFT.API("https://fakerestapi.azurewebsites.net/api/v1/");

        api.get("Activities/1")
                .addHeader("User-Agent", "Mozilla/5.0") // optional, mimics browser
                .perform();

        int statusCode = api.getResponseStatusCode();
        String responseBody = api.getResponseBody();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response: " + responseBody);

        Assert.assertEquals(statusCode, 200, "Expected HTTP 200 OK");
    }

    @Test
    public void testGetActivity() {
        SHAFT.API api = new SHAFT.API("https://fakerestapi.azurewebsites.net/api/v1/");
        api.get("Activities/1").perform();
    }

    @Test
    public void testGetAuthors() {
        SHAFT.API api = new SHAFT.API("https://fakerestapi.azurewebsites.net/api/v1/");
        api.get("Authors").perform();
    }

    @Test
    public void testGetAuthor() {
        SHAFT.API api = new SHAFT.API("https://fakerestapi.azurewebsites.net/api/v1/");
        api.get("Authors/1").perform();
    }


    @Test
    public void testGetAuthorBook() {
        SHAFT.API api = new SHAFT.API("https://fakerestapi.azurewebsites.net/api/v1/");
        api.get("Authors/authors/books/1").perform();
    }

    @Test
    public void testGetBooks() {
        SHAFT.API api = new SHAFT.API("https://fakerestapi.azurewebsites.net/api/v1/");
        api.get("Books").perform();
    }

    @Test
    public void addToCart() {
        SHAFT.API api = new SHAFT.API("https://api.demoblaze.com/");
        api.post("addtocart")
                .setContentType("application/json")
                .setRequestBody("{\n" +
                        "\"cookie\":\"dGVzdGNiYTEyMzE3MjI0MDI=\",\n" +
                        "\"flag\":true,\n" +
                        "\"id\" :\"995bed43-ab62-2940-c093-b42b2cc4d887\",\n" +
                        "\"prod_id\":1\n" +
                        "}")
                .perform();
    }

    @Test
    public void deleteCart() {
        SHAFT.API api = new SHAFT.API("https://api.demoblaze.com/");
        api.post("deletecart")
                .setContentType("application/json")
                .setRequestBody("{\"id\": \"995bed43-ab62-2940-c093-b42b2cc4d887\"}")
                .perform();
    }

    @Test
    public  void viewCart() {
        SHAFT.API api = new SHAFT.API("https://api.demoblaze.com/");
        api.post("viewcart")
                .setContentType("application/json")
                .setRequestBody("{\n" +
                        "    \"cookie\": \"dGVzdGNiYTEyMzE3MjI0MDI=\", \n" +
                        "    \"flag\": true\n" +
                        "}")
                .perform();
    }

    @AfterSuite
    public void afterSuite() throws IOException {
        //RequestBuilder.generatePerformanceReport("src/test/resources/report.html");
        //RequestBuilder.printPerformanceReport();
        // HTMLPerformanceReport.generatePerformanceReport();
        //com.shaft.driver.SHAFT.Engine.tearDownAllDrivers();

    }

}
