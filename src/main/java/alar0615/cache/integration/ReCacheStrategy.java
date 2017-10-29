package alar0615.cache.integration;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public interface ReCacheStrategy<K, V> {
    public boolean isNeedReCache(ILeveledCache<K, V> leveledCache);
}
