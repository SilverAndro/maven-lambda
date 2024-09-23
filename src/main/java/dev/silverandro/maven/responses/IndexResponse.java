package dev.silverandro.maven.responses;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import dev.silverandro.maven.Config;
import dev.silverandro.maven.Handler;
import dev.silverandro.maven.layer.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

public class IndexResponse {
    private static final Log log = LogFactory.getLog(IndexResponse.class);

    public static APIGatewayV2HTTPResponse respond(String path, LambdaLogger logger) {
        if (path.isBlank()) {
            logger.log("Building root layer");
            initLayer(null, Layer.ROOT, logger);
            return constructFromLayer(path, Layer.ROOT);
        }

        String[] split = path.split("/");
        Layer child = Layer.ROOT.getChild(split, 0);
        initLayer(path, child, logger);
        return constructFromLayer(path, child);
    }

    private static void initLayer(String path, Layer layer, LambdaLogger logger) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(Config.BUCKET_NAME)
                .withDelimiter("/")
                .withMaxKeys(Config.INDEXING_MAX_KEYS);

        if (path != null) {
            request = request.withPrefix(path + '/');
        }

        ListObjectsV2Result listing = Handler.s3.listObjectsV2(request);
        List<String> prefixes = listing.getCommonPrefixes();
        layer.populate(path == null ? "" : path, prefixes);
        for (S3ObjectSummary summary : listing.getObjectSummaries()) {
            String key = summary.getKey();
            if (path == null) {
                layer.files.add(key);
            } else {
                layer.files.add(key.substring(path.length() + 1));
            }
        }
    }

    private static APIGatewayV2HTTPResponse constructFromLayer(String path, Layer layer) {
        String relativePrefix;
        if (path == null || path.equals("")) {
            relativePrefix = "";
        } else {
            if (path.lastIndexOf('/') == -1) {
                relativePrefix = path + '/';
            } else {
                int prev = path.lastIndexOf('/');
                relativePrefix = '.' + path.substring(prev) + '/';
            }
        }


        StringBuilder output = new StringBuilder();

        output.append("<!DOCTYPE html>\n");
        output.append("<html>\n");
        output.append("	<head>\n");
        output.append("		<title>").append(Config.MAVEN_NAME).append("</title>\n");
        output.append("		<meta property=\"og:title\" content=\"").append(Config.MAVEN_NAME).append("\">\n");
        output.append("		<meta property=\"og:description\" content=\"An index of ").append(path).append("/ on the maven\">\n");
        if (Config.MAVEN_IMAGE != null) {
            output.append("		<meta property=\"og:image\" content=\"").append(Config.MAVEN_IMAGE).append("\">\n");
        }
        if (Config.MAVEN_COLOR != null) {
            output.append("		<meta name=\"theme-color\" content=\"").append(Config.MAVEN_COLOR).append("\">\n");
        }
        if (Config.MAVEN_ICON != null) {
            output.append("		<link rel=\"icon\" type=\"image/x-icon\" href=\"").append(Config.MAVEN_ICON).append("\">\n");
        }
        output.append("	</head>\n");
        output.append("	<body>\n");
        output.append("		<h1>Index of ").append(path).append("/</h1>\n");

        if (!layer.packages.isEmpty()) {
            output.append("		<h3>Packages:</h3>\n");
            output.append("		<ul>\n");
            for (String pkg : layer.packages) {
                output.append("			<li><a href=\"")
                        .append(relativePrefix)
                        .append(pkg)
                        .append("\">")
                        .append(pkg)
                        .append("/</a></li>\n");
            }
            output.append("		</ul>\n");
        }

        if (!layer.files.isEmpty()) {
            output.append("		<h3>Files:</h3>\n");
            output.append("		<ul>\n");
            for (String file : layer.files) {
                output.append("			<li><a href=\"").append(relativePrefix).append(file).append("\" download>").append(file).append("</a></li>\n");
            }
            output.append("		</ul>\n");
        }

        output.append("	  </body>\n");
        output.append("</html>\n");

        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(200)
                .withHeaders(Map.of("content-type", "text/html", "Cache-Control", "public, max-age=43200"))
                .withBody(output.toString())
                .build();
    }
}
