package com.aurus.tinytactics.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapCollection<K, V> extends Collection<Entry<K, V>> {

    private Map<K, V> map;

    private MapCollection(Map<K, V> map) {
        this.map = map;
    }

    public MapCollection() {
        this.map = new HashMap<>();
    }

    @Override
    public MapCollection<K, V> copy() {
        return new MapCollection<>(new HashMap<>(map));
    }

    public MapCollection<K, V> add(K key, V value) {
        HashMap<K, V> newMap = new HashMap<>(map);
        newMap.put(key, value);
        return new MapCollection<>(newMap);
    }

    public MapCollection<K, V> removeKey(K key) {
        HashMap<K, V> newMap = new HashMap<>(map);
        newMap.remove(key);
        return new MapCollection<>(newMap);
    }

    public MapCollection<K, V> removeValue(V value) {
        HashMap<K, V> newMap = new HashMap<>(map);
        newMap.values().remove(value);
        return new MapCollection<>(newMap);
    }

    @Override
    public Collection<Entry<K, V>> add(Entry<K, V> value) {
        return this.add(value.getKey(), value.getValue());
    }

    @Override
    public Collection<Entry<K, V>> remove(Object o) {
        if (o instanceof Entry entry) {
            HashMap<K, V> newMap = new HashMap<>(map);
            boolean successFlag = false;
            if (entry.getValue() == null) {
                successFlag = (newMap.remove(entry.getKey()) != null);
            } else if (entry.getKey() == null) {
                successFlag = newMap.values().remove(entry.getValue());
            } else {
                successFlag = (newMap.remove(entry.getKey(), entry.getValue()));
            }

            if (successFlag) {
                return new MapCollection<>(newMap);
            }
        }
        return this;
    }

}
