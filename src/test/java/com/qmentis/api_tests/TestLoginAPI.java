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
 * TestLoginAPI class for testing the login API with valid and invalid credentials.
 * This class uses TestNG and RestAssured for API testing.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/auth/login";
    private RequestSpecification request;

    /**
     * Setup method to initialize RestAssured configurations.
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
            {"Valid credentials", "testuser@example.com", "ValidPass123!", true},
            {"Invalid password", "testuser@example.com", "InvalidPass", false},
            {"Invalid username", "invaliduser@example.com", "ValidPass123!", false}
        };
    }

    /**
     * Test method for login scenarios using data from the data provider.
     * 
     * @param description Test scenario description.
     * @param username Username for login.
     * @param password Password for login.
     * @param expectedSuccess Expected success status.
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
        softly.assertEquals(loginResponse.isSuccess(), expectedSuccess, "Login success status mismatch");

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
package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for login request payload.
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
 * POJO for login response payload.
 */
@Data
public class LoginResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;
}