package com.aurus.tinytactics.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class MapCollection<K, V> extends Collection<Pair<K, V>> {

    private Map<K, V> map;

    public MapCollection(Map<K, V> map) {
        this.map = map;
    }

    public MapCollection() {
        this.map = new HashMap<>();
    }

    public MapCollection(List<Pair<K, V>> pairs) {
        this.map = new HashMap<>();
        for (Pair<K, V> p : pairs) {
            this.map.put(p.getFirst(), p.getSecond());
        }
    }

    public Map<K, V> getEntries() {
        return this.map;
    }

    public List<Pair<K, V>> getPairs() {
        List<Pair<K, V>> result = new ArrayList<>();
        for (Entry<K, V> e : map.entrySet()) {
            result.add(new Pair<K, V>(e.getKey(), e.getValue()));
        }
        return result;
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
    public Collection<Pair<K, V>> add(Pair<K, V> value) {
        return this.add(value.getFirst(), value.getSecond());
    }

    @Override
    public Collection<Pair<K, V>> remove(Object o) {
        if (o instanceof Pair pair) {
            HashMap<K, V> newMap = new HashMap<>(map);
            boolean successFlag = false;
            if (pair.getSecond() == null) {
                successFlag = (newMap.remove(pair.getFirst()) != null);
            } else if (pair.getFirst() == null) {
                successFlag = newMap.values().remove(pair.getSecond());
            } else {
                successFlag = (newMap.remove(pair.getFirst(), pair.getSecond()));
            }

            if (successFlag) {
                return new MapCollection<>(newMap);
            }
        }
        return this;
    }

    public Codec<MapCollection<K, V>> getCodec(Codec<K> keyCodec, Codec<V> valueCodec) {
        return RecordCodecBuilder.create(instance -> instance
                .group(Codec.unboundedMap(keyCodec, valueCodec)
                        .fieldOf("entries")
                        .forGetter(MapCollection::getEntries))
                .apply(instance, MapCollection::new));
    }

    public Codec<?> getCodec(Codec<Pair<K, V>> elementCodec) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCodec'");
    }

}
