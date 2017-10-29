package alar0615.cache.integration;

import alar0615.cache.api.ICache;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public interface ILeveledCache<K, V> extends ICache<K, V>{
    void reCache() throws IOException, ClassNotFoundException;
    Map<K, V> add(K key, V object) throws IOException, ClassNotFoundException;
    List<K> sortedKeys();
    int maxLevel1size();
    Set<K> level1keys();
}
