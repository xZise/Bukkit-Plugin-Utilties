package de.xzise.wrappers.permissions;

public class BufferPermission<T> implements Permission<T> {

    public final String name;
    public final T def;

    public BufferPermission(String name, T def) {
        this.name = name;
        this.def = def;
    }
    
    public static <T> BufferPermission<T> create(String name, T def) {
        return new BufferPermission<T>(name, def);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public T getDefault() {
        return this.def;
    }

}