package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResponseUtil {

    private final Gson gson = new Gson();

    public boolean isSuccess(Response response) {
        int code = response.getStatusCode();
        return code == 200 || code == 201;
    }

    public String getValue(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    public boolean getBoolean(Response response, String jsonPath) {
        return response.jsonPath().getBoolean(jsonPath);
    }

    public int getInt(Response response, String jsonPath) {
        return response.jsonPath().getInt(jsonPath);
    }

    public boolean isElementPresent(Response response, String arrayPath, String fieldName, String value) {
        List<Map<String, Object>> list = response.jsonPath().getList(arrayPath);
        return list.stream().anyMatch(item -> value.equals(String.valueOf(item.get(fieldName))));
    }

    public JsonObject getElement(Response response, String arrayPath, String fieldName, String value) {
        List<Map<String, Object>> list = response.jsonPath().getList(arrayPath);
        Optional<Map<String, Object>> match = list.stream()
                .filter(item -> value.equals(String.valueOf(item.get(fieldName))))
                .findFirst();
        return match.map(item -> JsonParser.parseString(gson.toJson(item)).getAsJsonObject())
                .orElse(null);
    }

}