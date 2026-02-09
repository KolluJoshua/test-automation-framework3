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
 * This class uses TestNG for testing and RestAssured for API interaction.
 */
public class TestLoginAPI extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/auth/login";
    private RequestSpecification request;

    /**
     * Sets up the RestAssured request specification.
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
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
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
     * @param username    Username for login
     * @param password    Password for login
     * @param expectedStatusCode Expected HTTP status code
     * @param expectedSuccess Expected success status in response
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginData", priority = 1)
    public void testLogin(String description, String username, String password, int expectedStatusCode, boolean expectedSuccess) {
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
        softly.assertEquals(loginResponse.isSuccess(), expectedSuccess, "Success status mismatch");

        softly.assertAll();
    }

    /**
     * Cleans up after tests.
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
 * POJO for login request.
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
 * POJO for login response.
 */
@Data
public class LoginResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("token")
    private String token;
}