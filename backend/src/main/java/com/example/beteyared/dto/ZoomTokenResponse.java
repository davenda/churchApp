package com.example.beteyared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class ZoomTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;  // This will generate getAccessToken()

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("scope")
    private String scope;
}