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
 * It uses TestNG for testing and RestAssured for API interactions.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/auth/login";
    private RequestSpecification request;

    /**
     * Sets up the base URI and headers for the API requests.
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
            {"Valid Login", "testuser@example.com", "ValidPass123!", 200, true},
            {"Invalid Password", "testuser@example.com", "InvalidPass", 401, false},
            {"Invalid Username", "invaliduser@example.com", "ValidPass123!", 401, false}
        };
    }

    /**
     * Tests the login API with various scenarios.
     *
     * @param description Test scenario description
     * @param username Username for login
     * @param password Password for login
     * @param expectedStatus Expected HTTP status code
     * @param expectedSuccess Expected success flag in response
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginTestData", priority = 1)
    public void testLoginScenario(String description, String username, String password, int expectedStatus, boolean expectedSuccess) {
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

        if (response.getStatusCode() == 200) {
            LoginResponse loginResponse = response.as(LoginResponse.class);
            softly.assertEquals(loginResponse.isSuccess(), expectedSuccess, "Success flag mismatch");
        }

        softly.assertAll();
    }

    /**
     * Cleans up resources after tests.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup resources if needed
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

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("token")
    private String token;

    @JsonProperty("message")
    private String message;
}