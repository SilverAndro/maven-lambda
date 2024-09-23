package dev.silverandro.maven.responses;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

public class InvalidRequestResponse {
    public static APIGatewayV2HTTPResponse respond() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(400)
                .withBody("Invalid request")
                .build();
    }
}
