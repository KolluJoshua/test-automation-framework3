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
 * It includes data-driven tests using @DataProvider for different login scenarios.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private RequestSpecification request;

    /**
     * Sets up the RestAssured request specification before any tests are run.
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
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][] {
            {"Valid credentials", "testuser@example.com", "ValidPass123!", 200, "Login successful"},
            {"Invalid password", "testuser@example.com", "InvalidPass", 401, "Invalid credentials"},
            {"Non-existent user", "nonexistent@example.com", "SomePass123!", 404, "User not found"}
        };
    }

    /**
     * Tests the login API with various scenarios.
     *
     * @param description Description of the test scenario
     * @param username Username for login
     * @param password Password for login
     * @param expectedStatusCode Expected HTTP status code
     * @param expectedMessage Expected message in the response
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginData", priority = 1)
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

        LoginResponse loginResponse = response.as(LoginResponse.class);
        softly.assertEquals(loginResponse.getMessage(), expectedMessage, "Response message mismatch");

        softly.assertAll();
    }

    /**
     * Cleans up resources after all tests have been run.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup logic if necessary
    }
}
package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing the login request payload.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("deviceId")
    private String deviceId;
}
package com.qmentis.pojos;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing the login response payload.
 */
@Data
public class LoginResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;
}