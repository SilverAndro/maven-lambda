package dev.silverandro.maven.responses;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.s3.model.ObjectMetadata;
import dev.silverandro.maven.Config;
import dev.silverandro.maven.Handler;
import dev.silverandro.maven.Util;

import java.util.Map;

public class HeadResponse {
    public static APIGatewayV2HTTPResponse respond(String path) {
        try {
            ObjectMetadata data = Handler.s3.getObjectMetadata(Config.BUCKET_NAME, path);
            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(200)
                    .withHeaders(Map.of(
                            "content-type", Util.mimeType(path),
                            "Cache-Control", "public, max-age=259200",
                            "Last-Modified", Util.formatDate(data.getLastModified()),
                            "Content-Length", String.valueOf(data.getContentLength())
                    ))
                    .build();
        } catch (SdkClientException e) {
            return NoContentResponse.respond();
        }
    }
}
