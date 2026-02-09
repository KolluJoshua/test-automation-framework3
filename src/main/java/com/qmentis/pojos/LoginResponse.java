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

    @JsonProperty("token")
    private String token;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("roles")
    private String[] roles;
}