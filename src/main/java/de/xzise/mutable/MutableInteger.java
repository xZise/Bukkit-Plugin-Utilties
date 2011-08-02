package de.xzise.mutable;

public final class MutableInteger extends Number implements Mutable<Integer> {

    private static final long serialVersionUID = 1877823395691583265L;
    
    public int value;
    
    public MutableInteger() {
        this.value = 0;
    }
    
    public MutableInteger(Number number) {
        this.value = number.intValue();
    }
    
    public MutableInteger(int value) {
        this.value = value;
    }
    
    @Override
    public void setValue(Integer t) {
        this.value = t;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
    
    public void inc() {
        this.value++;
    }
    
    @Override
    public int intValue() {
        return this.value;
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }
}