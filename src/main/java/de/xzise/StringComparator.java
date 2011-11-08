/*
 * This file is part of Bukkit Plugin Utilities.
 * 
 * Bukkit Plugin Utilities is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.xzise;

import java.text.Collator;
import java.util.Comparator;

public abstract class StringComparator<T> implements Comparator<T> {
    
    private final Collator collator;
    
    public StringComparator(Collator collator) {
        this.collator = collator;
    }
    
    public StringComparator() {
        this(Collator.getInstance());
        this.collator.setStrength(Collator.SECONDARY);
    }
    
    protected abstract String getValue(T t);
    
    @Override
    public int compare(T t1, T t2) {
        return this.collator.compare(this.getValue(t1), this.getValue(t2));
    }
}