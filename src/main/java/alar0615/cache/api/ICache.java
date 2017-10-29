package alar0615.cache.api;

import java.io.IOException;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public interface ICache<K, V> {
    V getObject(K key) throws IOException, ClassNotFoundException;
    void cacheObject(K key, V value) throws IOException, ClassNotFoundException;
    void clearCache();
    int cacheSize();
    void remove(K key) throws IOException, ClassNotFoundException;
    Set<K> keySet();
}
