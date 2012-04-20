/*
 * This file is part of Bukkit Plugin Utilities.
 * 
 * Bukkit Plugin Utilities is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Bukkit Plugin Utilities is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Bukkit Plugin Utilities.
 * If not, see <http://www.gnu.org/licenses/>.
 */

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
