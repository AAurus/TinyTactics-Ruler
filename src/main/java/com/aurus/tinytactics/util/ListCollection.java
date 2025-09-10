package com.aurus.tinytactics.util;

import java.util.ArrayList;
import java.util.List;

public class ListCollection<T> extends Collection<T> {

    private List<T> list;

    public ListCollection(List<T> list) {
        this.list = list;
    }

    public ListCollection() {
        this.list = new ArrayList<>();
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

}
