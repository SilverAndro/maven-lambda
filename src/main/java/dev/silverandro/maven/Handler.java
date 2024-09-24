package dev.silverandro.maven;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import dev.silverandro.maven.responses.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("unused")
public class Handler implements RequestHandler<Map<String, Object>, APIGatewayV2HTTPResponse> {
    public static final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private static final Log log = LogFactory.getLog(Handler.class);

    static {
        Config.verify();
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("Starting request handling");

        //noinspection unchecked
        Map<String, String> tmpHeaders = (Map<String, String>) input.get("headers");
        Map<String, String> headers = new HashMap<>();
        for (String entry : tmpHeaders.keySet()) {
            headers.put(entry.toLowerCase(Locale.ROOT), tmpHeaders.get(entry));
        }

        String httpMethod = (String) input.get("httpMethod");
        logger.log("HTTP method is " + httpMethod);

        //noinspection unchecked
        String stage = ((Map<String, String>) input.get("requestContext")).get("stage");
        String path = ((String) input.get("path")).substring(1).replace(stage + "/", "");
        logger.log("Path is \"" + path + '"');

        boolean isIndexing = Objects.equals(httpMethod, "GET") && Util.isIndexing(path);
        logger.log("Is indexing " + isIndexing);

        if (isIndexing && !Config.IS_INDEXING_ENABLED) {
            return CannotIndexResponse.respond();
        }

        if (isIndexing) {
            logger.log("Building index");
            return IndexResponse.respond(path, logger);
        }

        if (Objects.equals(httpMethod, "HEAD")) {
            return HeadResponse.respond(path);
        }

        if (Objects.equals(httpMethod, "GET")) {
            logger.log("Pulling " + path + " from s3");
            return GetResponse.respond(path);
        }

        if (Objects.equals(httpMethod, "PUT")) {
            if (path.isBlank()) return InvalidRequestResponse.respond();
            String rawAuth = headers.get("authorization");
            if (rawAuth == null || rawAuth.isBlank()) {
                logger.log("Did not provide authentication field");
                return InvalidAuthResponse.respond();
            }

            String auth = new String(Base64.getDecoder().decode(rawAuth.substring("Basic ".length())), StandardCharsets.UTF_8);

            if (!auth.contains(":")) {
                logger.log("Tried to log in with " + auth + " which is an invalid format");
                return InvalidAuthResponse.respond();
            }

            String[] split = auth.split(":", 2);
            String username = split[0];
            String password = split[1];

            if (!Objects.equals(username, Config.USERNAME) || !Objects.equals(password, Config.PASSWORD)) {
                logger.log("Invalid credentials '" + username +"' and '" + password + "'");
                return InvalidAuthResponse.respond();
            }

            int size;
            try {
                size = Integer.parseInt(headers.get("content-length"));
            } catch (NumberFormatException e) {
                return NoLengthResponse.respond();
            }

            if (size > Config.MAX_ARTIFACT_SIZE) {
                return TooBigResponse.respond();
            }

            return PutResponse.respond(path, Base64.getDecoder().decode((String) input.get("body")), logger);
        }

        return InvalidRequestResponse.respond();
    }
}