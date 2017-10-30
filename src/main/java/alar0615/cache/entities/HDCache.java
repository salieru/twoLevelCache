package alar0615.cache.entities;

import alar0615.cache.api.ICache;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: alar0615
 */
public class HDCache<K, V> implements ICache<K, V> {

    private final Map<K, String> pathMap = new HashMap<K, String>();

    @Override
    public V getObject(K key) throws IOException, ClassNotFoundException{
        if(pathMap.containsKey(key)){
            String path = pathMap.get(key);

            FileInputStream fileStream = new FileInputStream(path);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            V object = (V)objectStream.readObject();
            fileStream.close();
            objectStream.close();
            return object;
        }

        return null;
    }

    @Override
    public void cacheObject(K key, V value) throws IOException {
        String pathToObject;
        pathToObject = "\\temp\\" + UUID.randomUUID().toString() + ".temp";
        pathMap.put(key, pathToObject);

        FileOutputStream fileStream = new FileOutputStream(pathToObject);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        objectStream.writeObject(value);
        objectStream.flush();
        objectStream.close();
        fileStream.flush();
        fileStream.close();
    }

    @Override
    public void clearCache() {
        for(K key : pathMap.keySet()){
            File deletingFile = new File(pathMap.get(key));
            deletingFile.delete();
        }
        pathMap.clear();
    }

    @Override
    public int cacheSize() {
        return pathMap.size();
    }

    @Override
    public void remove(K key) {
        if(pathMap.containsKey(key))
        {
            File deletingFile = new File(pathMap.remove(key));
            deletingFile.delete();
        }
    }

    @Override
    public Set<K> keySet() {
        return pathMap.keySet();
    }
}
