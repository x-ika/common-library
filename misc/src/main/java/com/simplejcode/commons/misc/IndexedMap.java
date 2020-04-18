package com.simplejcode.commons.misc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IndexedMap<E, I> {

    private Map<E, I> indexMap;

    private Map<I, Collection<E>> collectionMap;


    public IndexedMap() {
        indexMap = new HashMap<>();
        collectionMap = new ConcurrentHashMap<>();
    }


    public synchronized Collection<E> get(I index) {
        Collection<E> collection = collectionMap.get(index);
        return collection == null ? Collections.emptyList() : new ArrayList<>(collection);
    }

    public synchronized void index(E element, I index) {

        I oldIndex = indexMap.put(element, index);

        if (index != null && index.equals(oldIndex)) {
            return;
        }

        // remove from old list
        if (oldIndex != null) {
            Collection<E> collection = collectionMap.get(oldIndex);
            collection.remove(element);
            if (collection.isEmpty()) {
                collectionMap.remove(oldIndex);
            }
        }

        // add to new list
        if (index != null) {
            collectionMap.computeIfAbsent(index, k -> new ArrayList<>()).add(element);
        }

    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(indexMap.toString()).append('\n');
        for (I index : collectionMap.keySet()) {
            sb.append(index).append(" : ").append(collectionMap.get(index).size()).append("'\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
