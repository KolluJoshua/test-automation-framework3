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
 * TestLoginAPI class contains test cases for the login API.
 * It uses TestNG for test management and RestAssured for API testing.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/auth/login";
    private RequestSpecification request;

    /**
     * Sets up the RestAssured request specification before any test method is executed.
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
     * Provides test data for login scenarios.
     *
     * @return Object[][] containing test scenarios, usernames, passwords, and expected results.
     */
    @DataProvider(name = "loginTestData")
    public Object[][] loginTestDataProvider() {
        return new Object[][] {
            {"Valid Login", "testuser@example.com", "ValidPass123!", true},
            {"Invalid Password", "testuser@example.com", "InvalidPass", false},
            {"Invalid Username", "invaliduser@example.com", "ValidPass123!", false}
        };
    }

    /**
     * Tests the login API with various credentials.
     *
     * @param description Description of the test scenario.
     * @param username    Username for login.
     * @param password    Password for login.
     * @param shouldSucceed Expected success status.
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginTestData", priority = 1)
    public void testLoginAPI(String description, String username, String password, boolean shouldSucceed) {
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
        int expectedStatusCode = shouldSucceed ? 200 : 401;
        softly.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");

        if (shouldSucceed) {
            LoginResponse loginResponse = response.as(LoginResponse.class);
            softly.assertNotNull(loginResponse.getToken(), "Token should not be null for successful login");
        } else {
            softly.assertTrue(response.getBody().asString().contains("Invalid credentials"), "Error message mismatch");
        }

        softly.assertAll();
    }

    /**
     * Cleans up resources after all test methods have been executed.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup resources if needed
    }
}