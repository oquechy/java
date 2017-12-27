package ru.spbau.mit.oquechy.fp;

import org.jetbrains.annotations.NotNull;

/**
 * function of one argument
 * transforms value of type T to value of type U
 * @param <T> argument type
 * @param <U> return type
 */
@FunctionalInterface
public interface Function1<T, U> {
    /**
     * function call method
     * @param t argument
     * @return function computation result
     */
    U apply(T t);

    /**
     * returns function g(f(x)), assuming this = f
     * @param g function to compose with
     * @param <V> return type of resulting function
     */
    @NotNull
    default <V> Function1<T, V> compose(@NotNull Function1<? super U, V> g) {
        return (T t) -> g.apply(apply(t));
    }
}
