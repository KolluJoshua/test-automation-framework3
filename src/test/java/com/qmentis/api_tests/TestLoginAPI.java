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
 * Test class for the Login API.
 * This class tests the login functionality with valid and invalid credentials.
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
     * Provides test data for the login scenarios.
     *
     * @return Object[][] containing test scenarios, usernames, passwords, and expected results.
     */
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][] {
            {"Valid Login", "testuser@example.com", "ValidPass123!", true},
            {"Invalid Login", "testuser@example.com", "InvalidPass", false}
        };
    }

    /**
     * Tests the login API with different scenarios.
     *
     * @param description Description of the test scenario.
     * @param username Username for login.
     * @param password Password for login.
     * @param isSuccess Expected success status.
     */
    @Test(groups = {"api", "smoke"}, dataProvider = "loginData", priority = 1)
    public void testLogin(String description, String username, String password, boolean isSuccess) {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
            .username(username)
            .password(password)
            .build();

        // Act
        Response response = request
            .body(loginRequest)
            .post(LOGIN_ENDPOINT);

        // Assert
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(response.getStatusCode(), 200, "Status code mismatch");

        LoginResponse loginResponse = response.as(LoginResponse.class);
        softly.assertEquals(loginResponse.isSuccess(), isSuccess, "Login success status mismatch");

        softly.assertAll();
    }

    /**
     * Cleans up after all tests have been run.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup resources if necessary
    }
}