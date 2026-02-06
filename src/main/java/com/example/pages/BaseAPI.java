package com.example.pages;

import com.example.utils.APIUtil;
import com.example.utils.JSONUtil;
import com.example.utils.ResponseUtil;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public abstract class BaseAPI<T extends BaseAPI<T>> {

    protected final APIUtil apiUtil;
    protected final JSONUtil jsonUtil;
    protected final ResponseUtil responseUtil;

    @Getter
    protected Response response;

    protected BaseAPI(APIUtil apiUtil) {
        this.apiUtil = apiUtil;
        this.jsonUtil = new JSONUtil();
        this.responseUtil = new ResponseUtil();
    }

    /**
     * Sets the API response and returns the current instance for method chaining.
     *
     * @param response The RestAssured Response object.
     * @return The current instance (subclass type).
     */
    @SuppressWarnings("unchecked")
    public T setResponse(Response response) {
        this.response = response;
        return (T) this;
    }

    // JSON Helper Methods

    public JsonObject readJSONTemplate(String path) {
        return jsonUtil.readJSONTemplate(path);
    }

    public JsonObject updateJSONField(JsonObject json, String key, Object value) {
        return jsonUtil.updateJSONField(json, key, value);
    }

    public void addProperty(JsonObject json, String key, Object value) {
        jsonUtil.addPropertySafe(json, key, value);
    }

    public void removeField(JsonObject json, String key) {
        jsonUtil.removeField(json, key);
    }

    public void removeFields(JsonObject json, List<String> keys) {
        jsonUtil.removeFields(json, keys);
    }

    public void removeFieldByPath(JsonObject json, String path) {
        jsonUtil.removeFieldByPath(json, path);
    }

    /**
     * Updates a JSON payload with multiple values from a data map.
     *
     * @param jsonPayload The JSON object to update.
     * @param data        A map containing key-value pairs to update in the JSON.
     */
    public void updateJsonPayload(JsonObject jsonPayload, Map<String, ?> data) {
        jsonUtil.updateJson(jsonPayload, data);
    }

    // Response Helper Methods

    public int getStatusCode() {
        return response != null ? response.getStatusCode() : -1;
    }

    public boolean isResponseSuccess() {
        return response != null && responseUtil.isSuccess(response);
    }

    public String getFieldValue(String jsonPath) {
        return response != null ? responseUtil.getValue(response, jsonPath) : "";
    }

    public boolean getBooleanFromResponse(String jsonPath) {
        return response != null && responseUtil.getBoolean(response, jsonPath);
    }

    public int getIntFromResponse(String jsonPath) {
        return response != null ? responseUtil.getInt(response, jsonPath) : -1;
    }

    /**
     * Checks if an element with a specific field and value exists in a JSON array within the response.
     *
     * @param arrayPath  The JSON path to the array.
     * @param fieldName  The field name to check within each array element.
     * @param fieldValue The expected value of the field.
     * @return true if the element is present, false otherwise.
     */
    public boolean isElementPresentInArray(String arrayPath, String fieldName, String fieldValue) {
        return response != null && responseUtil.isElementPresent(response, arrayPath, fieldName, fieldValue);
    }

    /**
     * Retrieves an element as a JsonObject from a JSON array based on a field value.
     *
     * @param arrayPath  The JSON path to the array.
     * @param fieldName  The field name to match.
     * @param fieldValue The expected value of the field.
     * @return The matching JsonObject, or null if not found.
     */
    public JsonObject getElementByField(String arrayPath, String fieldName, String fieldValue) {
        return response != null ? responseUtil.getElement(response, arrayPath, fieldName, fieldValue) : null;
    }
}
