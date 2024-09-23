package dev.silverandro.maven;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Util {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"));
    private static final Pattern regex = Pattern.compile(".+/\\w+\\.\\D{2,}\\d*?");

    public static boolean isIndexing(String path) {
        return !path.endsWith("/") && !regex.matcher(path).matches();
    }

    public static String formatDate(Date date) {
        return formatter.format(date.toInstant());
    }

    public static String mimeType(String path) {
        int last = path.lastIndexOf('.');
        String postfix = path.substring(last + 1);
        return switch (postfix) {
            case "jar" -> "application/java-archive";
            case "xml", "pom" -> "application/xml";
            default -> "text/plain";
        };
    }
}
