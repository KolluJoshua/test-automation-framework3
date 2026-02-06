package com.example.utils;

import com.example.beans.APIDetails;
import com.example.beans.ConfigurationDetails;
import com.example.logs.RequestPayloadRecorder;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class APIUtil
{

    private static Logger logger = LoggerFactory.getLogger(APIUtil.class);
    /**
     * -- GETTER --
     * Return the shared RequestSpecification
     */
    @Getter
    RequestSpecification request;
    Response response;
    public String JSONFilePath;
    public String userName;
    public String password;
    public String token;

    public APIUtil() {

       loadConfig();
       this.request = RestAssured.given()
                .contentType(ContentType.JSON)
                .log().all();
    }

    private void loadConfig() {
        ConfigurationDetails configurationDetails = ConfigurationDetailsUtil.getInstance().getConfigurationDetails();
        APIDetails apiConfig = configurationDetails.getApiDetails();

        RestAssured.baseURI = apiConfig.getBaseAPIURL();
        this.userName = apiConfig.getApiUserName();
        this.password = apiConfig.getApiUserPassword();
        this.JSONFilePath = apiConfig.getApiTemplateLocation();
    }

    // Written for Cogmento application, modify as per your application
    public void authenticate() {
        String authEndpoint = "auth/";
        // Build JSON body for login
        JsonObject credentials = new JsonObject();
        credentials.addProperty("email", userName);
        credentials.addProperty("password", password);

        logger.info("Authenticating user: {}", userName);

        // Send POST request
        Response authResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(credentials.toString())
                .post(authEndpoint)
                .andReturn();

        if (authResponse.getStatusCode() == 201) {
            JsonPath jsonPath = authResponse.jsonPath();
            // Extract the token from the response
            this.token = jsonPath.getString("response.token"); // e.g., "data.token"
            logResponse("POST", authEndpoint, authResponse);
            logger.info("Authentication successful. Token obtained.");

        // Rebuild request with token header
        this.request = RestAssured.given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Token " + token);
        } else {
            logger.error("Authentication failed with status code: " + authResponse.getStatusCode());
            throw new RuntimeException("Failed to authenticate");
        }
    }

    // ====== POST ======
    public Response sendPost(JsonObject jsonObject,String endpoint,String logMessage){
        RequestPayloadRecorder.recordRequest(jsonObject);
        logRequest(logMessage,jsonObject);
        this.response = request.body(jsonObject.toString()).post(endpoint).andReturn();
        logResponse("POST", endpoint, response);
        return response;
    }

    // ====== PUT ======
    public Response sendPut(JsonObject jsonObject,String endpoint){
        RequestPayloadRecorder.recordRequest(jsonObject);
        this.response = request.body(jsonObject.toString()).put(endpoint).andReturn();
        logResponse("PUT", endpoint, response);
        return response;
    }

    // ====== GET ======
    public Response sendGet(String endpoint) {
        Response response = request.get(endpoint).andReturn();
        logResponse("GET", endpoint, response);
        return response;
    }

    // ====== DELETE  ======
    public Response sendDELETE(String endpoint) {
        Response response = request.delete(endpoint).andReturn();
        logResponse("DELETE", endpoint, response);
        return response;
    }

    // For requests needing query params
    public Response sendGet(String endpoint, Map<String, Object> queryParams) {
        Response response = request.queryParams(queryParams).get(endpoint).andReturn();
        logResponse("GET", endpoint, response);
        return response;
    }

    // For dynamic headers (useful for special APIs)
    public Response sendPost(JsonObject body, String endpoint, Map<String, String> headers) {
        RequestPayloadRecorder.recordRequest(body);
        Response response = request.headers(headers).body(body.toString()).post(endpoint).andReturn();
        logResponse("POST", endpoint, response);
        return response;
    }


    private void logRequest(String message, JsonObject requestJSON) {
        String sanitizedMessage = message != null ? message.replace("{}", "[payload captured]") : "API request payload captured";
        logger.info(sanitizedMessage);
        if (logger.isDebugEnabled()) {
            logger.debug("Request payload: {}", requestJSON);
        }
    }

    private void logResponse(String method, String endpoint, Response response) {
        logger.info("\n=== {} {} ===\nStatus: {}\nResponse Body:\n{}\n=====================",
                method, endpoint, response.getStatusCode(), response.asPrettyString());
    }

}
