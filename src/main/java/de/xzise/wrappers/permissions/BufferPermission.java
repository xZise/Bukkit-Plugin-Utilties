package de.xzise.wrappers.permissions;

public class BufferPermission implements Permission<Boolean> {

    public final String name;
    public final boolean def;

    public BufferPermission(String name, boolean def) {
        this.name = name;
        this.def = def;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Boolean getDefault() {
        return this.def;
    }

}