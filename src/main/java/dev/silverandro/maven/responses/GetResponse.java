package dev.silverandro.maven.responses;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.s3.model.S3Object;
import dev.silverandro.maven.Config;
import dev.silverandro.maven.Handler;
import dev.silverandro.maven.Util;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class GetResponse {
    public static APIGatewayV2HTTPResponse respond(String path) {
        try {
            S3Object object = Handler.s3.getObject(Config.BUCKET_NAME, path);
            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(200)
                    .withIsBase64Encoded(true)
                    .withHeaders(Map.of(
                            "content-type", Util.mimeType(path),
                            "Cache-Control", "public, max-age=259200",
                            "Last-Modified", Util.formatDate(object.getObjectMetadata().getLastModified()),
                            "Content-Length", String.valueOf(object.getObjectMetadata().getContentLength())
                    ))
                    .withBody(Base64.getEncoder().encodeToString(object.getObjectContent().readAllBytes()))
                    .build();
        } catch (SdkClientException | IOException e) {
            return NoContentResponse.respond();
        }
    }
}
