package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class representing a login request.
 * Utilizes Lombok for boilerplate code reduction and Jackson for JSON serialization.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    /**
     * The username for login.
     */
    @JsonProperty("username")
    private String username;

    /**
     * The password for login.
     */
    @JsonProperty("password")
    private String password;

    /**
     * The device ID from which the login request is made.
     */
    @JsonProperty("deviceId")
    private String deviceId;
}