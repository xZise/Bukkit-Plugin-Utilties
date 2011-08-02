package de.xzise;

import java.text.Collator;
import java.util.Comparator;

public abstract class StringComparator<T> implements Comparator<T> {
    
    private final Collator collator;
    
    public StringComparator(Collator collator) {
        this.collator = collator;
    }
    
    public StringComparator() {
        this(Collator.getInstance());
        this.collator.setStrength(Collator.SECONDARY);
    }
    
    protected abstract String getValue(T t);
    
    @Override
    public int compare(T t1, T t2) {
        return this.collator.compare(this.getValue(t1), this.getValue(t2));
    }
}