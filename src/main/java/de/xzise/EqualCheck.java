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

package de.xzise;

import java.util.Comparator;

public interface EqualCheck<T> {
    boolean equals(T a, T b);
    
    public static class ComparatorEqualChecker<T> implements EqualCheck<T>, Comparator<T> {
        private final Comparator<T> comparator;

        public ComparatorEqualChecker(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public boolean equals(T a, T b) {
            return this.comparator.compare(a, b) == 0;
        }

        @Override
        public int compare(T a, T b) {
            return this.comparator.compare(a, b);
        }
    }

    public static final ClassicEqualChecker CLASSIC_EQUAL_CHECKER = new ClassicEqualChecker();
    public static class ClassicEqualChecker implements EqualCheck<Object> {
        @Override
        public boolean equals(Object a, Object b) {
            return MinecraftUtil.equals(a, b);
        }
    }

    public static final ComparableEqualChecker<? extends Comparable<?>> COMPARABLE_EQUAL_CHECKER = ComparableEqualChecker.createComparableEqualChecker();
    public static class ComparableEqualChecker<T extends Comparable<T>> implements EqualCheck<T> {
        @Override
        public boolean equals(T a, T b) {
            return a == null ? b == null : a.compareTo(b) == 0;
        }

        public static <T extends Comparable<T>> ComparableEqualChecker<T> createComparableEqualChecker() {
            return new ComparableEqualChecker<T>();
        }
    }

    public static final StringIgnoreCaseEqualChecker STRING_IGNORE_CASE_EQUAL_CHECKER = new StringIgnoreCaseEqualChecker();
    public static class StringIgnoreCaseEqualChecker implements EqualCheck<String> {
        @Override
        public boolean equals(String a, String b) {
            return a == null ? b == null : a.equalsIgnoreCase(b);
        }
    }

    public static final NumberComparable GREATER_EQUAL_CHECKER = new NumberComparable(true, true);
    public static final NumberComparable GREATER_CHECKER = new NumberComparable(true, false);
    public static final NumberComparable LOWER_CHECKER = new NumberComparable(false, false);
    public static final NumberComparable LOWER_EQUAL_CHECKER = new NumberComparable(false, true);
    public static class NumberComparable implements EqualCheck<Number> {
        private final int v;
        private final boolean equals;

        public NumberComparable(final boolean greater, final boolean equals) {
            this.v = greater ? 1 : -1;
            this.equals = equals;
        }

        @Override
        public boolean equals(Number a, Number b) {
            int comparable = MinecraftUtil.sgn(Double.compare(a.doubleValue(), b.doubleValue()));
            return comparable == this.v || (this.equals && comparable == 0);
        }
    }
}