package de.xzise.collections;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterator<T>, ListIterator<T> {
    private final T[] array;
    private int pos;

    public ArrayIterator(final T[] array) {
        this(array, 0);
    }

    public ArrayIterator(final T[] array, int start) {
        this.array = array;
        this.pos = start;
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

    @Override
    public boolean hasPrevious() {
        return this.pos > 0;
    }

    @Override
    public T previous() {
        if (hasPrevious()) {
            return this.array[this.pos--];
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int nextIndex() {
        return this.pos;
    }

    @Override
    public int previousIndex() {
        return this.pos - 1;
    }

    @Override
    public void set(T e) {
        this.array[this.pos] = e;
    }

    @Override
    public void add(T e) {
        throw new UnsupportedOperationException();
    }
}
