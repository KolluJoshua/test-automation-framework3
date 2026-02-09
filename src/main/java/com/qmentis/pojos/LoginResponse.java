package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing the response from a login API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    /**
     * The token received upon successful login.
     */
    @JsonProperty("token")
    private String token;

    /**
     * The expiration time of the token.
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * The type of the token.
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * The refresh token for obtaining new access tokens.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * The scope of the access token.
     */
    @JsonProperty("scope")
    private String scope;

    /**
     * The user ID associated with the token.
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * The message indicating the status of the login attempt.
     */
    @JsonProperty("message")
    private String message;
}