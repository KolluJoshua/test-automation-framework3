package com.qmentis.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing the response from the login API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    /**
     * The status of the login operation.
     */
    @JsonProperty("status")
    private String status;

    /**
     * The message returned from the login operation.
     */
    @JsonProperty("message")
    private String message;

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
}