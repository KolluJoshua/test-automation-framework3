package com.qmentis.api_tests;

import com.qmentis.pojos.LoginRequest;
import com.qmentis.pojos.LoginResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import static io.restassured.RestAssured.given;

/**
 * Test class for Login API.
 * This class tests login functionality using valid and invalid credentials.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private RequestSpecification request;

    /**
     * Setup method to initialize RestAssured base URI and request specification.
     */
    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.basePath = "/api/v1";
        request = given()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json");
    }

    /**
     * Data provider for login test scenarios.
     * Provides valid and invalid login credentials.
     *
     * @return Object[][] containing test scenarios
     */
    @DataProvider(name = "loginTestData")
    public Object[][] loginTestDataProvider() {
        return new Object[][] {
            {"Valid Login", "testuser@example.com", "ValidPass123!", 200, "Login successful"},
            {"Invalid Login", "invaliduser@example.com", "InvalidPass", 401, "Unauthorized"}
        };
    }

    /**
     * Test method for login scenarios.
     * Uses data from the loginTestDataProvider.
     *
     * @param description Description of the test scenario
     * @param username Username for login
     * @param password Password for login
     * @param expectedStatusCode Expected HTTP status code
     * @param expectedMessage Expected response message
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginTestData", priority = 1)
    public void testLogin(String description, String username, String password, int expectedStatusCode, String expectedMessage) {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
            .username(username)
            .password(password)
            .deviceId("android_test_001")
            .build();

        // Act
        Response response = request
            .body(loginRequest)
            .post(LOGIN_ENDPOINT);

        // Assert
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");

        if (response.getStatusCode() == 200) {
            LoginResponse loginResponse = response.as(LoginResponse.class);
            softly.assertEquals(loginResponse.getMessage(), expectedMessage, "Message mismatch");
        } else {
            softly.assertTrue(response.getBody().asString().contains(expectedMessage), "Error message mismatch");
        }

        softly.assertAll();
    }

    /**
     * Cleanup method to perform any necessary teardown.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup actions if necessary
    }
}