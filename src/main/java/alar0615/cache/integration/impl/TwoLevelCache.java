package alar0615.cache.integration.impl;

import alar0615.cache.api.ICache;
import alar0615.cache.common.counter.ICallCounter;
import alar0615.cache.integration.ILeveledCache;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public class TwoLevelCache<K, V> implements ILeveledCache<K, V>{
    private ICallCounter<K> callCounter;
    private ICache<K, V> hdCache;
    private ICache<K, V> memoryCache;
    private int maxNumberInMemory;
    private int maxNumberHD;
    private int maxCallNumberForReCache;
    private int callNumberForReCache;


    @Override
    public V getObject(K key) throws IOException, ClassNotFoundException {
        V object = memoryCache.getObject(key);
        if (object==null){
            object = hdCache.getObject(key);
        }
        if (object==null){
            return null;
        }

        callCounter.call(key);

        callNumberForReCache++;
        if (callNumberForReCache >= maxCallNumberForReCache){
            reCache();
        }
        return object;
    }

    @Override
    public void cacheObject(K key, V value) throws IOException, ClassNotFoundException {
        if (memoryCache.cacheSize()<maxNumberInMemory){
            memoryCache.cacheObject(key, value);
        } else {
            reCache();
            memoryCache.cacheObject(key, value);
        }
    }

    @Override
    public void clearCache() {
        memoryCache.clearCache();
        hdCache.clearCache();
        callCounter.reset();
    }

    @Override
    public int cacheSize() {
        return memoryCache.cacheSize() + hdCache.cacheSize();
    }

    @Override
    public void remove(K key) throws IOException, ClassNotFoundException {
        memoryCache.remove(key);
        hdCache.remove(key);
        callCounter.remove(key);
    }

    @Override
    public Set<K> keySet() {
        Set<K> allKeys = new HashSet<K>();
        Set<K> memorySet = memoryCache.keySet();
        Set<K> hdSet = hdCache.keySet();
        allKeys.addAll(memorySet);
        allKeys.addAll(hdSet);
        return allKeys;
    }

    @Override
    public void reCache() throws IOException, ClassNotFoundException {
        List<K> allSorted = callCounter.getSorted();
        List<K> newMemoryKeys = new ArrayList<K>();
        for (int i=0; i<maxNumberInMemory-1; i++){
            if (i>=allSorted.size()){
                break;
            }
            newMemoryKeys.add(allSorted.get(i));
        }
        Map<K, V> toRemoveFromMemoryCache = new HashMap<K, V>();
        Map<K, V> toAddToMemoryCache = new HashMap<K, V>();
        for (K key: memoryCache.keySet()){
            if (!newMemoryKeys.contains(key)){
                toRemoveFromMemoryCache.put(key, memoryCache.getObject(key));
            }
        }
        for (K key: hdCache.keySet()){
            if (newMemoryKeys.contains(key)){
                toAddToMemoryCache.put(key, hdCache.getObject(key));
            }
        }
        for (K key: toRemoveFromMemoryCache.keySet()){
            hdCache.cacheObject(key, toRemoveFromMemoryCache.get(key));
            memoryCache.remove(key);
        }
        for (K key: toAddToMemoryCache.keySet()){
            memoryCache.cacheObject(key, toAddToMemoryCache.get(key));
            hdCache.remove(key);
        }
    }

    @Override
    public Map<K, V> add(K key, V object) throws IOException, ClassNotFoundException {
        int totalMaxSize = maxNumberHD + maxNumberInMemory;
        int totalCurrentSize = memoryCache.cacheSize() + hdCache.cacheSize();
        if (totalCurrentSize < totalMaxSize) {
            this.cacheObject(key, object);
            return null;
        } else {
            List<K> sorted = callCounter.getSorted();
            K expelledKey = sorted.get(sorted.size() - 1);
            V expelledObject = hdCache.getObject(expelledKey);
            hdCache.remove(expelledKey);
            this.cacheObject(key, object);
            return Collections.singletonMap(expelledKey, expelledObject);
        }
    }

    @Override
    public List<K> sortedKeys() {
        return callCounter.getSorted();
    }

    @Override
    public int maxLevel1size() {
        return maxNumberInMemory;
    }

    @Override
    public Set<K> level1keys() {
        return memoryCache.keySet();
    }

    public ICallCounter<K> getCallCounter() {
        return callCounter;
    }

    public void setCallCounter(ICallCounter<K> callCounter) {
        this.callCounter = callCounter;
    }

    public ICache<K, V> getHdCache() {
        return hdCache;
    }

    public void setHdCache(ICache<K, V> hdCache) {
        this.hdCache = hdCache;
    }

    public ICache<K, V> getMemoryCache() {
        return memoryCache;
    }

    public void setMemoryCache(ICache<K, V> memoryCache) {
        this.memoryCache = memoryCache;
    }

    public int getMaxNumberInMemory() {
        return maxNumberInMemory;
    }

    public void setMaxNumberInMemory(int maxNumberInMemory) {
        this.maxNumberInMemory = maxNumberInMemory;
    }

    public int getMaxNumberHD() {
        return maxNumberHD;
    }

    public void setMaxNumberHD(int maxNumberHD) {
        this.maxNumberHD = maxNumberHD;
    }

    public int getMaxCallNumberForReCache() {
        return maxCallNumberForReCache;
    }

    public void setMaxCallNumberForReCache(int maxCallNumberForReCache) {
        this.maxCallNumberForReCache = maxCallNumberForReCache;
    }
}
