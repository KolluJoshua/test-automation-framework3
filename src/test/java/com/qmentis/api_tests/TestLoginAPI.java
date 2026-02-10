package com.qmentis.api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.qmentis.pojos.LoginRequest;
import com.qmentis.pojos.LoginResponse;
import base.BaseRestAssuredTest;

/**
 * TestLoginAPI class to test the login API with valid and invalid credentials.
 * Utilizes TestNG for test management and RestAssured for API interaction.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/auth/login";
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
     * DataProvider for login test scenarios.
     * 
     * @return Object[][] containing test scenarios with description, username, password, and expected status.
     */
    @DataProvider(name = "loginTestData")
    public Object[][] loginTestDataProvider() {
        return new Object[][] {
            {"Valid credentials", "testuser@example.com", "ValidPass123!", 200},
            {"Invalid password", "testuser@example.com", "InvalidPass", 401},
            {"Invalid username", "invaliduser@example.com", "ValidPass123!", 401}
        };
    }

    /**
     * Test method to verify login functionality with different credentials.
     * 
     * @param description Test scenario description.
     * @param username Username for login.
     * @param password Password for login.
     * @param expectedStatus Expected HTTP status code.
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginTestData", priority = 1)
    public void testLogin(String description, String username, String password, int expectedStatus) {
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
        softly.assertEquals(response.getStatusCode(), expectedStatus, "Status code mismatch");

        if (expectedStatus == 200) {
            LoginResponse loginResponse = response.as(LoginResponse.class);
            softly.assertNotNull(loginResponse.getToken(), "Token should not be null");
        } else {
            softly.assertThat(response.getBody().asString(), containsString("error"), "Error message expected");
        }

        softly.assertAll();
    }

    /**
     * Teardown method for cleanup after tests.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup resources if necessary
    }
}