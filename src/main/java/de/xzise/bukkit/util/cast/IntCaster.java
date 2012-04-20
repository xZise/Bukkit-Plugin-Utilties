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

/**
 * Casts the object to an Integer, if the object is a Number, otherwise null.
 */
public final class IntCaster implements Caster<Integer> {

    public static final IntCaster INSTANCE = new IntCaster();

    private IntCaster() {
    };

    /**
     * Casts the object to an Integer, if the object is a Number, otherwise
     * null.
     */
    public Integer cast(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Number) {
            return ((Number) o).intValue();
        } else {
            return null;
        }
    }
}