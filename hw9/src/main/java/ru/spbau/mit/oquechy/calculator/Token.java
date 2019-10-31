package ru.spbau.mit.oquechy.calculator;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Tokens to parse expression.
 * Bad thing about this enum: it encapsulates static stack
 * with given numbers, so you can't use two parsers in parallel.
 * Also you can't set and get one value twice.
 *
 * Good thing: ot is easy to add new operation.
 */
public enum Token {
    NUMBER {
        @Override
        public void setValue(@NotNull String value) {
            double e = Double.parseDouble(value);
            numbers.add(e);
        }

        @Override
        public double getValue() {
            if (numbers.isEmpty()) {
                throw new RuntimeException("Ran out numbers stack");
            }

            return numbers.poll();
        }

        @Override
        public int getPriority() {
            throw new UnsupportedOperationException("Number hasn't got priority");
        }
    },
    OPEN_BRACKET {
        @Override
        public int getPriority() {
            throw new UnsupportedOperationException("Bracket hasn't got priority");
        }
    },
    CLOSE_BRACKET {
        @Override
        public int getPriority() {
            throw new UnsupportedOperationException("Bracket hasn't got priority");
        }
    },
    DIVISION(2) {
        @Override
        public double apply(Double l, Double r) {
            return l / r;
        }
    },
    MULTIPLICATION(2) {
        @Override
        public double apply(Double l, Double r) {
            return l * r;
        }
    },
    ADDITION(1) {
        @Override
        public double apply(Double l, Double r) {
            return l + r;
        }
    },
    SUBTRACTION(1) {
        @Override
        public double apply(Double l, Double r) {
            return l - r;
        }
    };

    @NotNull
    protected Queue<Double> numbers = new LinkedList<>();
    private boolean isOperation = false;
    private int priority = -1;

    Token() {}

    Token(int priority) {
        this.isOperation = true;
        this.priority = priority;
    }

    /**
     * Sets value to numeric tolens
     * and ignores value for non numeric tokens.
     * @param value to be set
     */
    public void setValue(String value) { }

    /**
     * Returns value of numeric token and throws an exception
     * if called on not numeric
     */
    public double getValue() {
        throw new UnsupportedOperationException("Only numbers have values");
    }

    /**
     * Returns {@code true} for operation tokens
     */
    public boolean isOperation() {
        return isOperation;
    }

    /**
     * Returns priority of operation and
     * throws exception while being called
     * on not operation token
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Returns result of operation.
     * @param l left operand
     * @param r right operand
     */
    public double apply(Double l, Double r) {
        throw new UnsupportedOperationException("Only operations can be applied");
    }

    /**
     * Clears stack of values.
     */
    public void reset() {
        numbers.clear();
    }
}
