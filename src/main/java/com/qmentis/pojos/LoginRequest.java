package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing a login request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    
    /**
     * The username of the user attempting to log in.
     */
    @JsonProperty("username")
    private String username;
    
    /**
     * The password of the user attempting to log in.
     */
    @JsonProperty("password")
    private String password;
    
    /**
     * The device ID from which the login request is made.
     */
    @JsonProperty("deviceId")
    private String deviceId;
}