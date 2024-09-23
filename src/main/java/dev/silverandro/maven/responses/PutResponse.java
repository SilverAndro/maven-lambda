package dev.silverandro.maven.responses;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import dev.silverandro.maven.Config;
import dev.silverandro.maven.Handler;

import java.io.ByteArrayInputStream;

public class PutResponse {
    public static APIGatewayV2HTTPResponse respond(String path, byte[] content, LambdaLogger logger) {
        try {
            Handler.s3.putObject(Config.BUCKET_NAME, path, new ByteArrayInputStream(content), null);
        } catch (SdkClientException e) {
            logger.log("Failed to upload to " + path);
            throw e;
        }
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(201)
                .withBody("Successfully uploaded")
                .build();
    }
}
