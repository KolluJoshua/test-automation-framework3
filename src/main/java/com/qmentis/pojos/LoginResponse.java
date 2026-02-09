package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response received after a login attempt.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("error_message")
    private String errorMessage;
}