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
 * It uses TestNG framework and RestAssured for API testing.
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
     *
     * @return Object[][] containing test scenarios with description, username, password, and expected result.
     */
    @DataProvider(name = "loginTestData")
    public Object[][] loginTestDataProvider() {
        return new Object[][] {
            {"Valid login", "testuser@example.com", "ValidPass123!", true},
            {"Invalid login - wrong password", "testuser@example.com", "WrongPass!", false},
            {"Invalid login - non-existent user", "nonexistent@example.com", "SomePass123!", false}
        };
    }

    /**
     * Test method for login scenarios using different credentials.
     *
     * @param description Description of the test scenario.
     * @param username Username for login.
     * @param password Password for login.
     * @param expectedSuccess Expected success result.
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginTestData", priority = 1)
    public void testLoginScenario(String description, String username, String password, boolean expectedSuccess) {
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
        softly.assertEquals(response.getStatusCode(), 200, "Status code mismatch");

        LoginResponse loginResponse = response.as(LoginResponse.class);
        softly.assertEquals(loginResponse.isSuccess(), expectedSuccess, "Login success mismatch");

        softly.assertAll();
    }

    /**
     * Teardown method for cleanup after tests.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup resources if needed
    }
}