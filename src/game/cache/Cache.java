package game.cache;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Cache {
    private final HashMap<String, Object> cache = new HashMap<>();
    // Private constructor to prevent direct instantiation
    private Cache() {
        // Initialize any necessary resources or setup here
    }

    private static final class InstanceHolder {
        private static final Cache instance = new Cache();
    }

    // Method to provide access to the singleton instance
    public static Cache getInstance() {
        return InstanceHolder.instance;
    }

    public <T> T queryCache(String key) {
        Object element = cache.getOrDefault(key, null);
        return (T) element;
    }

    public boolean hasInCache(String key) {
        return cache.containsKey(key);
    }

    public void saveInCache(String key, Object o) {
        cache.put(key, o);
    }
}

