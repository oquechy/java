package ru.spbau.mit.oquechy.maybe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * container for one element with two states:
 * storing a value or storing nothing
 *
 * @param <T> is type of stored object
 */
public class Maybe<T> {
    @Nullable
    final private T object;
    private final boolean isPresent;

    private Maybe(@Nullable T object, boolean isPresent) {
        this.object = object;
        this.isPresent = isPresent;
    }

    /**
     * returns new Maybe with object of type T
     * @param t is object to be stored
     * @param <T> it's type
     */
    @NotNull
    public static <T> Maybe<T> just(T t) {
        return new Maybe<>(t, true);
    }

    /**
     * returns new empty Maybe
     * @param <T> type of object that could be stored
     */
    @NotNull
    public static <T> Maybe<T> nothing() {
        return new Maybe<>(null, false);
    }

    /**
     * returns stored value
     * @throws AccessToNothingException if called on an empty instance of Maybe
     */
    @Nullable
    public T get() throws AccessToNothingException {
        if (isPresent) {
            return object;
        }
        throw new AccessToNothingException();
    }

    /**
     * returns true if Maybe is not empty
     */
    public boolean isPresent() {
        return isPresent;
    }

    /**
     * applies mapper to stored value if it exists
     * and returns new Maybe with result of mapper
     * otherwise returns empty Maybe with mapper return type as parameter
     * @param mapper is function to be applied
     * @param <U> mapper's return type
     */
    @NotNull
    public <U> Maybe<U> map(@NotNull Function<T, U> mapper) {
        if (!isPresent) {
            return new Maybe<>(null, false);
        }

        return new Maybe<>(mapper.apply(object), true);
    }

    /**
     * returns result of comparing two Maybes on their fields
     * @param o is object to compare to
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Maybe<?> maybe = (Maybe<?>) o;

        return isPresent == maybe.isPresent && (object != null ? object.equals(maybe.object) : maybe.object == null);
    }

    /**
     * returns hash code based on fields
     */
    @Override
    public int hashCode() {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + (isPresent ? 1 : 0);
        return result;
    }
}

