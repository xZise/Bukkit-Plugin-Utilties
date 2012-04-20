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

import java.util.Map;

/**
 * Casts the object to a map, if the object is one, otherwise null.
 */
public class MapCaster<K, V> implements Caster<Map<K, V>> {

    public static final MapCaster<String, Object> STRING_OBJECT_INSTANCE = new MapCaster<String, Object>();

    /**
     * Casts the object to a map, if the object is one, otherwise null.
     */
    @SuppressWarnings("unchecked")
    public Map<K, V> cast(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Map) {
            return (Map<K, V>) o;
        } else {
            return null;
        }
    }

}
