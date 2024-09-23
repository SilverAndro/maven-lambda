package dev.silverandro.maven.responses;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import dev.silverandro.maven.Config;

public class TooBigResponse {
    public static APIGatewayV2HTTPResponse respond() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(413)
                .withBody("File too large. Max size: " + Config.MAX_ARTIFACT_SIZE)
                .build();
    }
}
