package alar0615.cache.common.counter.impl;

import alar0615.cache.common.ItemNotFoundException;
import alar0615.cache.common.counter.ICallCounter;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public class ICallCounterImpl<K> implements ICallCounter<K> {

    private Map<K, Integer> counterMap = new HashMap<K, Integer>();

    @Override
    public void register(K key) {
        counterMap.put(key, Integer.valueOf(0));
    }

    @Override
    public void call(K key) {
        if (!counterMap.containsKey(key)){
            register(key);
        }
        Integer value = counterMap.get(key);
        counterMap.put(key, value + 1);
    }

    @Override
    public int getCallCounter(K key) throws ItemNotFoundException {
        if (!counterMap.containsKey(key)){
            throw new ItemNotFoundException();
        }
        return counterMap.get(key);
    }

    @Override
    public List<K> getSorted() {
        List<Map.Entry<K, Integer>> allKeyCounterPairs = new ArrayList<Map.Entry<K, Integer>>(counterMap.entrySet());

        Collections.sort(allKeyCounterPairs, new Comparator<Map.Entry<K, Integer>>() {
            @Override
            public int compare(Map.Entry<K, Integer> o1, Map.Entry<K, Integer> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });

        List<K> sortedKeys = new ArrayList<K>();

        for (Map.Entry<K, Integer> entry: allKeyCounterPairs) {
            sortedKeys.add(entry.getKey());
        }

        return sortedKeys;
    }

    @Override
    public void reset() {
        counterMap.clear();
    }

    @Override
    public void remove(K key) {
        counterMap.remove(key);
    }
}
