package com.aurus.tinytactics.util;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ListCollection<T> extends Collection<T> {

    private List<T> list;

    public ListCollection(List<T> list) {
        this.list = list;
    }

    public ListCollection() {
        this.list = new ArrayList<>();
    }

    public List<T> getEntries() {
        return list;
    }

    @Override
    public ListCollection<T> copy() {
        return new ListCollection<>(new ArrayList<>(list));
    }

    @Override
    public ListCollection<T> add(T value) {
        List<T> newList = new ArrayList<>(list);
        newList.add(value);
        return new ListCollection<>(newList);
    }

    @Override
    public ListCollection<T> remove(Object index) {
        if (index instanceof Integer) {
            List<T> newList = new ArrayList<>(list);
            if (newList.remove(index)) {
                return new ListCollection<>(newList);
            }
        }
        return this;
    }

    public Codec<ListCollection<T>> getCodec(Codec<T> elementCodec) {
        return RecordCodecBuilder.create(instance -> instance
                .group(elementCodec.listOf().fieldOf("entries").forGetter(ListCollection::getEntries))
                .apply(instance, ListCollection::new));
    }

}
