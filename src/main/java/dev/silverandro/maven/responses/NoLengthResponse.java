package dev.silverandro.maven.responses;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

public class NoLengthResponse {
    public static APIGatewayV2HTTPResponse respond() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(411)
                .withBody("No Content-Length provided")
                .build();
    }
}
