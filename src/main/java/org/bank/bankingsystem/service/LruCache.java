package org.bank.bankingsystem.service;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class LruCache<K, V> {

    private final int CAPACITY = 100;

    private final Map<K, V> cache =
            new LinkedHashMap<>(CAPACITY, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                    return size() > CAPACITY;
                }
            };

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public void remove(K key) {
        cache.remove(key);
    }
}

