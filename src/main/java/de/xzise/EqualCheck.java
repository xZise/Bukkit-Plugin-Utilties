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

//    public static final ComparableEqualChecker<? extends Comparable<?>> COMPARABLE_EQUAL_CHECKER = new ComparableEqualChecker();
    public static class ComparableEqualChecker<T extends Comparable<T>> implements EqualCheck<T> {
        @Override
        public boolean equals(T a, T b) {
            return a == null ? b == null : a.compareTo(b) == 0;
        }
    }

    public static final StringIgnoreCaseEqualChecker STRING_IGNORE_CASE_EQUAL_CHECKER = new StringIgnoreCaseEqualChecker();
    public static class StringIgnoreCaseEqualChecker implements EqualCheck<String> {
        @Override
        public boolean equals(String a, String b) {
            return a == null ? b == null : a.equalsIgnoreCase(b);
        }
    }
}