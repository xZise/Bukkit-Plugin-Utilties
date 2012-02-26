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

package de.xzise.collections;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;

import de.xzise.EqualCheck;

public class ArrayReferenceList<E> extends AbstractList<E> implements RandomAccess, java.io.Serializable
{
    private static final long serialVersionUID = 700754851942464062L;
    private final E[] array;
    public final int length;

    public ArrayReferenceList(E[] array) {
        if (array == null)
            throw new NullPointerException();
        this.array = array;
        this.length = array.length;
    }

    public static <E> ArrayReferenceList<E> create(E[] array) {
        return new ArrayReferenceList<E>(array);
    }

    public int size() {
        return this.array.length;
    }

    public Object[] toArray() {
        return this.array.clone();
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size)
            return Arrays.copyOf(this.array, size, (Class<? extends T[]>) a.getClass());
        System.arraycopy(this.array, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    public E get(int index) {
        return this.array[index];
    }

    public E set(int index, E element) {
        E oldValue = this.array[index];
        this.array[index] = element;
        return oldValue;
    }

    /**
     * Tests where the first object is inside the array.
     * 
     * @param o
     *            Searched object.
     * @param a
     *            Searched array.
     * @return the first position found.
     * @since 1.3
     */
    public static <T> int indexOf(T o, T... a) {
        return indexOf(o, EqualCheck.CLASSIC_EQUAL_CHECKER, a);
    }

    /**
     * Tests where the first object is inside the array.
     * 
     * @param o
     *            Searched object.
     * @param a
     *            Searched array.
     * @param checker Object which checks if two objects are equals.
     * @return the first position found.
     * @since 1.3
     */
    public static <T> int indexOf(T o, EqualCheck<T> checker, T... a) {
        for (int i = 0; i < a.length; i++) {
            if (checker.equals(o, a[i])) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(Object o) {
        return indexOf(o, this.array);
    }

    /**
     * Returns if the tested object <code>o</code> is in the array <code>a</code>.
     * @param o Searched object.
     * @param a Searched array.
     * @return if the object is in the array.
     * @since 1.3
     */
    public static <T> boolean contains(T o, T[] a) {
        return ArrayReferenceList.indexOf(o, a) >= 0;
    }

    /**
     * Returns if the tested character is in the characters array.
     * @param character Searched character.
     * @param characters Searched characters.
     * @return if the character is in the array.
     * @since 1.3
     */
    public static boolean contains(char character, char[] characters) {
        return ArrayReferenceList.indexOf(character, characters) >= 0;
    }

    /**
     * Returns if the tested object <code>o</code> is in the array <code>a</code>.
     * @param o Searched object.
     * @param a Searched array.
     * @param checker Object which checks if two objects are equals.
     * @return if the object is in the array.
     * @since 1.3
     */
    public static <T> boolean contains(T o, T[] a, EqualCheck<T> checker) {
        return ArrayReferenceList.indexOf(o, checker, a) >= 0;
    }

    public boolean contains(Object o) {
        return contains(o, this.array);
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(this.array);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ArrayIterator<E>(this.array, index);
    }
}
