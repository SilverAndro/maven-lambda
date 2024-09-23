package dev.silverandro.maven.layer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Layer {
    public static final Layer ROOT = new Layer();

    private final Map<String, Layer> children = new HashMap<>();
    public final HashSet<String> files = new HashSet<>();
    public final HashSet<String> packages = new HashSet<>();

    public Layer getChild(String[] split, int index) {
        if (index == split.length) return this;
        return children.computeIfAbsent(split[index], s -> new Layer()).getChild(split, index + 1);
    }

    public void populate(String path, List<String> prefixes) {
        for (String prefix : prefixes) {
            String clean = prefix.substring(path.length() + 1, prefix.length() - 1);
            if (path.isBlank()) clean = prefix.substring(0, prefix.length() - 1);
            packages.add(clean);
            children.computeIfAbsent(clean, s -> new Layer());
        }
    }
}
