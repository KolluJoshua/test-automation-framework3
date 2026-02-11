package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class representing the response from a login API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;
}