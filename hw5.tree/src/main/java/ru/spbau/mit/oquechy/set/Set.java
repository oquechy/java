package ru.spbau.mit.oquechy.set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of set without deleting elements
 * @param <T> is type of containing value
 */
public class Set<T extends Comparable<T>> {

    @Nullable
    private Node<T> root = null;
    private int size = 0;

    static private class Node<T extends Comparable<T>> {
        T t;

        @Nullable Node<T> l = null;
        @Nullable Node<T> r = null;

        Node(T t) {
            this.t = t;
        }

        boolean add(@NotNull T t) {
            int compare = this.t.compareTo(t);
            if (compare == 0) {
                return false;
            }
            if (compare < 0 && l == null) {
                l = new Node<>(t);
                return true;
            }
            if (compare > 0 && r == null) {
                r = new Node<>(t);
                return true;
            }

            return (compare < 0 ? l : r).add(t);
        }

        boolean contains(@NotNull T t) {
            int compare = this.t.compareTo(t);
            if (compare == 0) {
                return true;
            }
            if (compare < 0) {
                return l != null && l.contains(t);
            }
            return r != null && r.contains(t);
        }

    }

    /**
     * Inserts new element to set.
     * Returns true if there was not such element before
     * @param t is a new element
     */
    public boolean add(@NotNull T t) {
        if (root == null) {
            root = new Node<>(t);
            size = 1;
            return true;
        }

        final boolean added = root.add(t);
        if (added) {
            size++;
        }
        return added;
    }

    /**
     * Returns true if t is in set
     * @param t is a value to check existence
     */
    public boolean contains(@NotNull T t) {
        return root != null && root.contains(t);
    }

    /**
     * Returns number of elements in set
     */
    public int size() {
        return size;
    }
}
