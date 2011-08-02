package de.xzise.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterator<T> {
    private T[] array;
    private int pos = 0;

    public ArrayIterator(T[] array) {
        this.array = array;
    }

    public boolean hasNext() {
        return this.pos < this.array.length;
    }

    public T next() throws NoSuchElementException {
        if (hasNext()) {
            return this.array[this.pos++];
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
