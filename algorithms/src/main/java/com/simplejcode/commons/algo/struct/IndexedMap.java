package com.simplejcode.commons.algo.struct;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IndexedMap<E, I> {

    private final Map<E, I> indexMap;

    private final Map<I, Collection<E>> collectionMap;


    public IndexedMap() {
        indexMap = new HashMap<>();
        collectionMap = new ConcurrentHashMap<>();
    }


    public synchronized Collection<E> get(I index) {
        Collection<E> collection = collectionMap.get(index);
        return collection == null ? Collections.emptyList() : new ArrayList<>(collection);
    }

    public synchronized void index(E element, I index) {
        reindex(element, index);
    }

    public synchronized void remove(E element) {
        reindex(element, null);
    }

    private synchronized void reindex(E element, I index) {

        I oldIndex = index == null ? indexMap.remove(element) : indexMap.put(element, index);

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
