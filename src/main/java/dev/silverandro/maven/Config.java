package dev.silverandro.maven;

import java.util.Objects;

public class Config {
    public static final String BUCKET_NAME = System.getenv("BUCKET_NAME");

    public static final boolean IS_INDEXING_ENABLED = !Objects.equals(System.getenv("INDEXING_ENABLED"), "false");
    public static int INDEXING_MAX_KEYS = Integer.parseInt(Objects.requireNonNullElse(System.getenv("INDEXING_MAX_KEYS"), "1000"));

    public static String USERNAME = System.getenv("UPLOAD_USERNAME");
    public static String PASSWORD = System.getenv("UPLOAD_PASSWORD");

    public static int MAX_ARTIFACT_SIZE = Integer.parseInt(Objects.requireNonNullElse(System.getenv("MAX_ARTIFACT_SIZE"), "5900000"));

    public static String MAVEN_NAME = Objects.requireNonNullElse(System.getenv("MAVEN_NAME"), "Maven");
    public static String MAVEN_IMAGE = System.getenv("MAVEN_IMAGE");
    public static String MAVEN_COLOR = System.getenv("MAVEN_COLOR");
    public static String MAVEN_ICON = System.getenv("MAVEN_ICON");

    public static void verify() {
        if (BUCKET_NAME == null) {
            throw new IllegalArgumentException("A BUCKET_NAME must be set in this app's Lambda environment variables.");
        }

        if (USERNAME == null) {
            throw new IllegalArgumentException("Failed to get username from UPLOAD_USERNAME for uploading artifacts");
        }

        if (PASSWORD == null) {
            throw new IllegalArgumentException("Failed to get password from UPLOAD_PASSWORD for uploading artifacts");
        }
    }
}
