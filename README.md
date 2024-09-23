# Setup

1) build the project with `shadowJar`
2) create a new lambda with java runtime
3) upload the jar
4) edit the entry point to use `dev.silverandro.maven.Handler::handleRequest` as the handler
5) create an s3 bucket
6) configure env variables
7) update permissions for the lambda to write and read s3
8) optional: enable and use snapstart https://docs.aws.amazon.com/lambda/latest/dg/snapstart-activate.html
9) create an api gateway
10) create `$default` route for the gateway and attach the integration (and remove the default integration)
11) use https://kylebarron.dev/blog/caching-lambda-functions-cloudflare to connect through cloudflare

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
