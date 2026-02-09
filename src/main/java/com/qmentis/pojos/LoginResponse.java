package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing the response of a login API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("expiresIn")
    private int expiresIn;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("roles")
    private String[] roles;
}