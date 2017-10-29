package alar0615.cache.common.counter;

import alar0615.cache.common.ItemNotFoundException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public interface ICallCounter<K> {
    /**
     * Registers element in the counter. Also resets counter to 0.
     * @param key
     */
    void register(K key);

    /**
     * Adds 1 to the call counter of the key.
     * @param key to call
     */
    void call(K key);

    /**
     * Returns call counter of the key.
     * @param key
     */
    int getCallCounter(K key) throws ItemNotFoundException;

    /**
     * @return the list of all keys sorted by frequency of calls
     */
    List<K> getSorted();

    /**
     * Sets all counters to 0
     */
    void reset();

    /**
     * Removes element
     * @param key to remove
     */
    void remove(K key);
}
