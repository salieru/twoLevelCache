package alar0615.cache.integration.impl;

import alar0615.cache.integration.ILeveledCache;
import alar0615.cache.integration.ReCacheStrategy;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public class ReCacheStrategyImpl<K, V> implements ReCacheStrategy<K, V> {

    @Override
    public boolean isNeedReCache(ILeveledCache<K, V> leveledCache) {
        return false;
    }
}
