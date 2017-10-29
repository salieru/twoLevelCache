package alar0615.cache.entities;

import alar0615.cache.api.ICache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public class MemoryCache<K, V> implements ICache<K, V> {

    private final Map<K, V> cache = new HashMap<K, V>();

    @Override
    public V getObject(K key) throws IOException, ClassNotFoundException {
        if (!cache.containsKey(key)){
            return null;
        }
        return cache.get(key);
    }

    @Override
    public void cacheObject(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    @Override
    public int cacheSize() {
        return cache.size();
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public Set<K> keySet() {
        return cache.keySet();
    }
}
