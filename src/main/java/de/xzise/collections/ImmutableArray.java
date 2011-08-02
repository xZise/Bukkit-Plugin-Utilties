package de.xzise.collections;

import java.util.Collection;
import java.util.Iterator;

public class ImmutableArray<T> implements Iterable<T> {

    private final T[] elements;
    
    @SuppressWarnings("unchecked")
    public ImmutableArray(Iterator<? extends T> iterator, int size) {
        this.elements = (T[]) new Object[size];
        int i = 0;
        while (i < size && iterator.hasNext()) {
            this.elements[i++] = iterator.next();
        }
    }
    
    public static <T> ImmutableArray<T> create(Collection<? extends T> collection) {
        return create(collection.iterator(), collection.size());
    }
    
    public static <T> ImmutableArray<T> create(Iterator<? extends T> iterator, int size) {
        return new ImmutableArray<T>(iterator, size);
    }
    
    public T get(int i) {
        return this.elements[i];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(this.elements);
    }
}
