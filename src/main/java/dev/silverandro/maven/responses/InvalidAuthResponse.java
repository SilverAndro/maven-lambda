package dev.silverandro.maven.responses;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

public class InvalidAuthResponse {
    public static APIGatewayV2HTTPResponse respond() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(403)
                .withBody("Invalid authorization")
                .build();
    }
}
