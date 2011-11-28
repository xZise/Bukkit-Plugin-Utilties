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

package de.xzise.mutable;

public final class MutableInteger extends Number implements Mutable<Integer> {

    private static final long serialVersionUID = 1877823395691583265L;

    private int v;

    /**
     * @deprecated Use {@link #getValue()} or {@link #intValue()}!
     */
    @Deprecated
    public int value;

    public MutableInteger() {
        this(0);
    }

    public MutableInteger(Number number) {
        this(number.intValue());
    }

    public MutableInteger(int value) {
        this.setValue(value);
    }

    @Override
    public void setValue(Integer t) {
        this.v = t;
        this.value = t;
    }

    @Override
    public Integer getValue() {
        return this.v;
    }

    public void inc() {
        this.v++;
        this.value++;
    }

    @Override
    public int intValue() {
        return this.v;
    }

    @Override
    public long longValue() {
        return this.v;
    }

    @Override
    public float floatValue() {
        return this.v;
    }

    @Override
    public double doubleValue() {
        return this.v;
    }
}