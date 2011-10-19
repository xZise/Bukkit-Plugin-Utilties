package de.xzise.bukkit.util.cast;

import de.xzise.MinecraftUtil;

public class ClassCaster<T> implements Caster<T> {

    public static final ClassCaster<Boolean> BOOLEAN_CASTER = ClassCaster.create(Boolean.class);
    public static final ClassCaster<String> STRICT_STRING_CASTER = ClassCaster.create(String.class);

    private final Class<T> clazz;

    public ClassCaster(final Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> ClassCaster<T> create(final Class<T> clazz) {
        return new ClassCaster<T>(clazz);
    }

    @Override
    public T cast(Object o) {
        return MinecraftUtil.cast(clazz, o);
    }
}
