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