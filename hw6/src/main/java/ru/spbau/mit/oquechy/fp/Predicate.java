package ru.spbau.mit.oquechy.fp;

import org.jetbrains.annotations.NotNull;

/**
 * function of one argument with return type of Boolean
 * transforms value of type T to either true or false
 * @param <T> argument type
 */
public interface Predicate<T> extends Function1<T, Boolean> {

    /**
     * constants
     */
    Predicate<Object> ALWAYS_TRUE = o -> true;
    Predicate<Object> ALWAYS_FALSE = o -> false;

    /**
     * @param p another predicate
     * @return predicate equivalent of (this || p)
     */
    @NotNull
    default Predicate<T> or(@NotNull Predicate<? super T> p) {
        return (T x) -> apply(x) || p.apply(x);
    }

    /**
     * @param p another predicate
     * @return predicate equivalent of (this && p)
     */
    @NotNull
    default Predicate<T> and(@NotNull Predicate<? super T> p) {
        return (T x) -> apply(x) && p.apply(x);
    }

    /**
     * @return predicate equivalent of (!this)
     */
    @NotNull
    default Predicate<T> not() {
        return (T x) -> !apply(x);
    }

}
