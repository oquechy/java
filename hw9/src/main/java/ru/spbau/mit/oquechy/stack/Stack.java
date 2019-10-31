package ru.spbau.mit.oquechy.stack;

import org.jetbrains.annotations.NotNull;

public interface Stack<T> {

    /**
     * Returns total size.
     */
    int size();

    /**
     * Returns {@code true} if stack is empty and {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * Adds value to stack.
     * @param value specified value
     */
    void push(T value);

    /**
     * Removes element from stack.
     * @return removed element
     */
    @NotNull T pop();

    /**
     * Returns first element of stack without removing it.
     */
    T peek();

    /**
     * Removes all elements from the stack.
     */
    void clear();
}
