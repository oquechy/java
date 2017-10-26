package ru.spbau.mit.oquechy.fp;

import org.jetbrains.annotations.NotNull;

/**
 * function of one argument
 * transforms values of types T1 and T2 to value of type U
 * @param <T1> first arg type
 * @param <T2> second arg type
 * @param <U> return type
 */
public interface Function2<T1, T2, U> {
    /**
     * function call method
     * @param t1 first argument
     * @param t2 second argument
     * @return function computation result
     */
    @NotNull U apply(T1 t1, T2 t2);

    /**
     * returns function g(f(x, y)), assuming this = f
     * @param g function to compose with
     * @param <V> return type of resulting function
     */
    @NotNull
    default  <V> Function2<T1, T2, V> compose(@NotNull Function1<? super U, V> g) {
        return (T1 t1, T2 t2) ->  g.apply(apply(t1, t2));
    }

    /**
     * returns function f(_, y), assuming this = f
     * @param t1 a value instead first argument
     */
    @NotNull
    default Function1<T2, U> bind1(T1 t1) {
        return (T2 t2) -> apply(t1, t2);
    }

    /**
     * returns function f(x, _), assuming this = f
     * @param t2 a value instead second argument
     */
    @NotNull
    default Function1<T1, U> bind2(T2 t2) {
        return t1 -> apply(t1, t2);
    }

    /**
     * alias for bind2
     */
    @NotNull
    default Function1<T1, U> curry(T2 t2) {
        return bind2(t2);
    }
}
