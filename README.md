# Setup

1) create a new lambda with java runtime
2) upload the jar
3) edit the entry point to use "dev.silverandro.maven.Handler::handleRequest" as the handler
4) create an s3 bucket
5) create an api gateway
6) create $default route for the gateway and attach the integration (and remove the default integration)
7) configure env variables
8) update permissions for the lambda
9) use https://kylebarron.dev/blog/caching-lambda-functions-cloudflare to connect through cloudflare

## Config:

Required:
- `BUCKET_NAME`: name of the s3 bucket for your maven
- `UPLOAD_USERNAME`: username for uploads
- `UPLOAD_PASSWORD`: password for uploads

## Optional:
- `IS_INDEXING_ENABLED`: controls indexing for the maven (default `true`)
- `INDEXING_MAX_KEYS`: max keys to request from s3 at a time (default `1000`)
- `MAX_ARTIFACT_SIZE`: max size of artifacts (default `5900000`)
- `MAVEN_NAME`: name of the maven (default `Maven`)
- `MAVEN_IMAGE`: URL to an image for rich embeds (default unused)
- `MAVEN_COLOR`: color for rich embeds (default unused)
- `MAVEN_ICON`: URL to favicon (default unused)