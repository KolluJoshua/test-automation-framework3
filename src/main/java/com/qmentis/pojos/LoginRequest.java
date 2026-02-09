package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a login request payload.
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
     * The device ID from which the login is attempted.
     */
    @JsonProperty("deviceId")
    private String deviceId;
}