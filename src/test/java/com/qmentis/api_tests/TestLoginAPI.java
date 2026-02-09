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
 * TestLoginAPI is a TestNG test class that tests the login API using RestAssured.
 * It includes data-driven tests with valid and invalid credentials.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private RequestSpecification request;

    /**
     * Sets up the RestAssured request specification before any test methods are run.
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
     * @return an array of test data scenarios
     */
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][] {
            {"Valid login", "testuser@example.com", "ValidPass123!", 200, true},
            {"Invalid password", "testuser@example.com", "WrongPass123!", 401, false},
            {"Invalid username", "wronguser@example.com", "ValidPass123!", 401, false}
        };
    }

    /**
     * Tests the login API with various credentials.
     *
     * @param description the test scenario description
     * @param username the username to test
     * @param password the password to test
     * @param expectedStatusCode the expected HTTP status code
     * @param expectedSuccess the expected success status in the response
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginData", priority = 1)
    public void testLogin(String description, String username, String password, int expectedStatusCode, boolean expectedSuccess) {
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
        softly.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");

        if (response.getStatusCode() == 200) {
            LoginResponse loginResponse = response.as(LoginResponse.class);
            softly.assertEquals(loginResponse.isSuccess(), expectedSuccess, "Success status mismatch");
        }

        softly.assertAll();
    }

    /**
     * Cleans up after all test methods have run.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup resources if needed
    }
}