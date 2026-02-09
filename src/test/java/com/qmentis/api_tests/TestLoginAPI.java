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
 * Test class for Login API using TestNG and RestAssured.
 * This class tests login functionality with valid and invalid credentials.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private RequestSpecification request;

    /**
     * Setup method to initialize RestAssured request specification.
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
     * @return Object[][] containing test scenarios
     */
    @DataProvider(name = "loginTestData")
    public Object[][] loginTestDataProvider() {
        return new Object[][] {
            {"Valid credentials", "testuser@example.com", "ValidPass123!", 200, "Login successful"},
            {"Invalid password", "testuser@example.com", "InvalidPass", 401, "Invalid credentials"},
            {"Invalid username", "invaliduser@example.com", "ValidPass123!", 401, "Invalid credentials"}
        };
    }

    /**
     * Test method for login scenarios.
     * 
     * @param description Description of the test scenario
     * @param username Username for login
     * @param password Password for login
     * @param expectedStatusCode Expected HTTP status code
     * @param expectedMessage Expected response message
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginTestData", priority = 1)
    public void testLoginScenario(String description, String username, String password, int expectedStatusCode, String expectedMessage) {
        // Arrange
        LoginRequest requestBody = LoginRequest.builder()
            .username(username)
            .password(password)
            .build();

        // Act
        Response response = request
            .body(requestBody)
            .post(LOGIN_ENDPOINT);

        // Assert
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");

        if (response.getStatusCode() == 200) {
            LoginResponse responseBody = response.as(LoginResponse.class);
            softly.assertEquals(responseBody.getMessage(), expectedMessage, "Response message mismatch");
        } else {
            String errorMessage = response.jsonPath().getString("error");
            softly.assertEquals(errorMessage, expectedMessage, "Error message mismatch");
        }

        softly.assertAll();
    }

    /**
     * Cleanup method after all tests are executed.
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