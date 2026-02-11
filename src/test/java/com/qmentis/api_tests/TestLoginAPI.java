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
 * This class contains tests for validating login functionality with different credentials.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/auth/login";
    private RequestSpecification request;

    /**
     * Sets up the base URI and request specification for the tests.
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
     * @return Object[][] containing test scenarios
     */
    @DataProvider(name = "loginTestData")
    public Object[][] loginTestDataProvider() {
        return new Object[][] {
            {"Valid Login", "testuser@example.com", "ValidPass123!", 200, "Login successful"},
            {"Invalid Password", "testuser@example.com", "WrongPass!", 401, "Invalid credentials"},
            {"Invalid Username", "wronguser@example.com", "ValidPass123!", 401, "Invalid credentials"}
        };
    }

    /**
     * Tests login scenarios using provided test data.
     *
     * @param description Test scenario description
     * @param username Username for login
     * @param password Password for login
     * @param expectedStatusCode Expected HTTP status code
     * @param expectedMessage Expected response message
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginTestData", priority = 1)
    public void testLoginScenario(String description, String username, String password, int expectedStatusCode, String expectedMessage) {
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
            softly.assertEquals(response.jsonPath().getString("error"), expectedMessage, "Error message mismatch");
        }

        softly.assertAll();
    }

    /**
     * Cleans up resources after tests.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup resources if necessary
    }
}