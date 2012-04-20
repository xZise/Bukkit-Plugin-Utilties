/*
 * This file is part of Bukkit Plugin Utilities.
 * 
 * Bukkit Plugin Utilities is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
<<<<<<< HEAD
 * Bukkit Plugin Utilities is distributed in the hope that it will be useful,
=======
 * Foobar is distributed in the hope that it will be useful,
>>>>>>> 1.3
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
<<<<<<< HEAD
 * along with Bukkit Plugin Utilities.
 * If not, see <http://www.gnu.org/licenses/>.
=======
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
>>>>>>> 1.3
 */

package de.xzise.collections;

import java.util.Collection;
import java.util.Iterator;

public class ImmutableArray<T> implements Iterable<T> {

    private final T[] elements;
    
    @SuppressWarnings("unchecked")
    public ImmutableArray(Iterator<? extends T> iterator, int size) {
        this.elements = (T[]) new Object[size];
        int i = 0;
        while (i < size && iterator.hasNext()) {
            this.elements[i++] = iterator.next();
        }
    }
    
    public static <T> ImmutableArray<T> create(Collection<? extends T> collection) {
        return create(collection.iterator(), collection.size());
    }
    
    public static <T> ImmutableArray<T> create(Iterator<? extends T> iterator, int size) {
        return new ImmutableArray<T>(iterator, size);
    }
    
    public T get(int i) {
        return this.elements[i];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(this.elements);
    }
}
