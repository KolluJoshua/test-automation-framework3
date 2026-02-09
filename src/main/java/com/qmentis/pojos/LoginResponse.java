package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing the response from the login API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("scope")
    private String scope;
}
package com.qmentis.api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import com.qmentis.pojos.LoginResponse;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

/**
 * Test class for verifying the login API functionality.
 */
public class LoginAPITest extends BaseRestAssuredTest {

    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private RequestSpecification request;

    /**
     * Setup method to initialize RestAssured request specifications.
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
            {"Valid Login", "testuser@example.com", "ValidPass123!", true},
            {"Invalid Login", "testuser@example.com", "InvalidPass", false}
        };
    }

    /**
     * Test method for verifying login scenarios.
     *
     * @param description Test scenario description
     * @param username Username for login
     * @param password Password for login
     * @param isSuccess Expected success status
     */
    @Test(groups = {"api", "login"}, dataProvider = "loginTestData", priority = 1)
    public void testLogin(String description, String username, String password, boolean isSuccess) {
        // Arrange
        String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        // Act
        Response response = request
            .body(requestBody)
            .post(LOGIN_ENDPOINT);

        // Assert
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(response.getStatusCode(), isSuccess ? 200 : 401, "Status code mismatch");

        if (isSuccess) {
            LoginResponse loginResponse = response.as(LoginResponse.class);
            softly.assertNotNull(loginResponse.getToken(), "Token should not be null");
            softly.assertTrue(loginResponse.getExpiresIn() > 0, "Expires in should be positive");
        } else {
            softly.assertThat(response.jsonPath().getString("error"), is(notNullValue()));
        }

        softly.assertAll();
    }

    /**
     * Cleanup method to perform any required teardown operations.
     */
    @AfterClass
    public void tearDown() {
        // Cleanup operations
    }
}