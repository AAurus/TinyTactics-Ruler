package com.aurus.tinytactics.util;

public abstract class Collection<T> {

    public abstract Collection<T> copy();

    public abstract Collection<T> add(T value);

    public abstract Collection<T> remove(Object o);
}