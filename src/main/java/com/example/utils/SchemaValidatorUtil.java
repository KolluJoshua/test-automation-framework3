package com.example.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

public final class SchemaValidatorUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final JsonSchemaFactory SCHEMA_FACTORY =
            JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7); // draft-07

    private SchemaValidatorUtil() {
    }

    public static void validateResponseSchema(String jsonResponse, String schemaPath) {
        try (InputStream schemaStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(schemaPath)) {

            if (schemaStream == null) {
                throw new IllegalArgumentException("Schema file not found on classpath: " + schemaPath);
            }

            // Load schema
            JsonSchema schema = SCHEMA_FACTORY.getSchema(schemaStream);

            // Parse response JSON
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonResponse);

            // Validate
            Set<ValidationMessage> errors = schema.validate(jsonNode);

            if (!errors.isEmpty()) {
                String message = errors.stream()
                        .map(ValidationMessage::toString)
                        .collect(Collectors.joining("\n - "));
                throw new AssertionError("JSON schema validation failed:\n - " + message);
            }
        } catch (Exception e) {
            throw new AssertionError("Exception during JSON schema validation", e);
        }
    }
}
