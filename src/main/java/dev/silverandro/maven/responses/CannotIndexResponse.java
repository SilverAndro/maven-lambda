package dev.silverandro.maven.responses;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import java.util.Map;

public class CannotIndexResponse {
    public static APIGatewayV2HTTPResponse respond() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(405)
                .withHeaders(Map.of("Allow", ""))
                .withBody("Indexing is not enabled for this repository")
                .build();
    }
}
