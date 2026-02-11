package com.qmentis.api_tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import com.qmentis.utils.TestNGDataProviderUtils;
import com.qmentis.base.BaseRestAssuredTest;
import com.qmentis.models.LoginRequest;
import com.qmentis.models.LoginResponse;

public class TestLoginAPI extends BaseRestAssuredTest {

    @BeforeClass
    public void setup() {
        super.setup();
        baseURI = "https://api.qmentis.com";
    }

    @DataProvider(name = "loginDataProvider")
    public Object[][] loginDataProvider() {
        return TestNGDataProviderUtils.getDataFromJson("src/test/resources/loginTestData.json");
    }

    @Test(dataProvider = "loginDataProvider")
    public void testValidLogin(LoginRequest loginRequest) {
        Response response = given()
            .contentType("application/json")
            .body(loginRequest)
            .when()
            .post("/login")
            .then()
            .statusCode(200)
            .extract()
            .response();

        LoginResponse loginResponse = response.as(LoginResponse.class);
        assertThat("Token should not be null", loginResponse.getToken(), notNullValue());
    }

    @Test(dataProvider = "loginDataProvider")
    public void testInvalidLogin(LoginRequest loginRequest) {
        loginRequest.setPassword("WrongPassword");
        
        given()
            .contentType("application/json")
            .body(loginRequest)
            .when()
            .post("/login")
            .then()
            .statusCode(401)
            .body("message", equalTo("Invalid credentials"));
    }

    @Test(dataProvider = "loginDataProvider")
    public void testMissingFields(LoginRequest loginRequest) {
        loginRequest.setUsername(null);
        
        given()
            .contentType("application/json")
            .body(loginRequest)
            .when()
            .post("/login")
            .then()
            .statusCode(400)
            .body("message", equalTo("Username is required"));
    }
}