package de.xzise.mutable;

public interface Mutable<T> {

    void setValue(T t);
    T getValue();
    
}
