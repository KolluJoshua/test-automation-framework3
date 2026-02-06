package com.example.utils;

import com.google.gson.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Generic utility class for JSON operations using Google Gson.
 * Provides support for deep navigation and manipulation using dot-separated paths.
 */
public class JSONUtil {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    /**
     * Reads a JSON file and parses it into a JsonObject.
     *
     * @param jsonFilePath Path to the JSON file.
     * @return JsonObject representation of the file.
     */
    public JsonObject readJSONTemplate(String jsonFilePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            return JsonParser.parseString(content).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON file at: " + jsonFilePath, e);
        }
    }

    /**
     * Updates a single field in a JsonObject. Supports dot-separated paths for nested fields.
     *
     * @param json  The JsonObject to update.
     * @param path  Dot-separated path (e.g., "user.address.city").
     * @param value The value to set.
     * @return The updated JsonObject.
     */
    public JsonObject updateJSONField(JsonObject json, String path, Object value) {
        return updateValueByPath(json, path, value);
    }

    /**
     * Updates a JsonObject with values from a Map. Keys in the map can be dot-separated paths.
     *
     * @param jsonPayload The JsonObject to update.
     * @param testData    Map of paths and values.
     */
    public void updateJson(JsonObject jsonPayload, Map<String, ?> testData) {
        if (testData == null) return;
        for (Map.Entry<String, ?> entry : testData.entrySet()) {
            updateValueByPath(jsonPayload, entry.getKey(), entry.getValue());
        }
    }

    /**
     * Safely adds a property to a JsonObject. Converts the value to an appropriate JsonElement.
     *
     * @param json  The JsonObject.
     * @param key   The key.
     * @param value The value.
     */
    public void addPropertySafe(JsonObject json, String key, Object value) {
        if (value == null) return;
        json.add(key, GSON.toJsonTree(value));
    }

    /**
     * Removes a field from a JsonObject. Supports dot-separated paths.
     *
     * @param json The JsonObject.
     * @param path Dot-separated path to the field.
     */
    public void removeFieldByPath(JsonObject json, String path) {
        if (path == null || path.isEmpty()) return;
        
        String[] keys = path.split("\\.");
        JsonObject current = json;
        
        for (int i = 0; i < keys.length - 1; i++) {
            if (!current.has(keys[i]) || !current.get(keys[i]).isJsonObject()) {
                return; // Path does not exist
            }
            current = current.getAsJsonObject(keys[i]);
        }
        current.remove(keys[keys.length - 1]);
    }

    /**
     * Deeply sets a value at a given dot-separated path. Creates intermediate objects if they don't exist.
     *
     * @param json  The root JsonObject.
     * @param path  Dot-separated path.
     * @param value The value to set.
     * @return The root JsonObject.
     */
    public JsonObject updateValueByPath(JsonObject json, String path, Object value) {
        if (path == null || path.isEmpty()) return json;

        String[] keys = path.split("\\.");
        JsonObject current = json;

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            if (!current.has(key) || !current.get(key).isJsonObject()) {
                current.add(key, new JsonObject());
            }
            current = current.getAsJsonObject(key);
        }

        String targetKey = keys[keys.length - 1];
        if (value == null) {
            current.add(targetKey, JsonNull.INSTANCE);
        } else {
            current.add(targetKey, GSON.toJsonTree(value));
        }

        return json;
    }

    /**
     * Checks if a field exists at the given dot-separated path.
     *
     * @param json The JsonObject.
     * @param path Dot-separated path.
     * @return true if the path exists.
     */
    public boolean isPathPresent(JsonObject json, String path) {
        if (path == null || path.isEmpty()) return false;
        
        String[] keys = path.split("\\.");
        JsonObject current = json;
        
        for (int i = 0; i < keys.length - 1; i++) {
            if (!current.has(keys[i]) || !current.get(keys[i]).isJsonObject()) {
                return false;
            }
            current = current.getAsJsonObject(keys[i]);
        }
        return current.has(keys[keys.length - 1]);
    }

    /**
     * Simple removal of a top-level field.
     */
    public void removeField(JsonObject json, String key) {
        json.remove(key);
    }

    /**
     * Simple removal of multiple top-level fields.
     */
    public void removeFields(JsonObject json, List<String> keys) {
        if (keys != null) keys.forEach(json::remove);
    }

    /**
     * Converts a JsonElement to a pretty-printed string.
     */
    public String toPrettyString(JsonElement element) {
        return GSON.toJson(element);
    }

    /**
     * Parses a JSON string into a JsonObject.
     */
    public JsonObject parse(String jsonString) {
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }
}
