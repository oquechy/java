package ru.spbau.mit.oquechy.fp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * implements functional methods applicable to
 * Function1, Function2, Predicate and any Collection
 */
public class Collections {

    /**
     * applies f function to each element of collection
     * @param f function to be applied
     * @param collection to modify
     * @param <E> type of elements
     * @param <U> type of resulting elements
     * @return list of resulting values in order of collection iteration
     */
    @NotNull
    public static <E, U> List<U> map(@NotNull Function1<? super E, U> f, @NotNull Collection<E> collection) {
        @NotNull List<U> list = new ArrayList<>();

        for (E e : collection) {
            list.add(f.apply(e));
        }

        return list;
    }

    /**
     * returns list of collection elements satisfying the predicate
     * @param p predicate to sift elements
     * @param collection with elements to filter
     * @param <E> type of elements
     */
    @NotNull
    public static <E> List<E> filter(@NotNull Predicate<? super E> p, @NotNull Collection<E> collection) {
        @NotNull List<E> list = new ArrayList<>();

        for (E e : collection) {
            if (p.apply(e)) {
                list.add(e);
            }
        }

        return list;
    }

    /**
     * returns the first few elements satisfying the predicate
     * @param p is predicate to check
     * @param collection with elements to take
     * @param <E> type of element
     */
    @NotNull
    public static <E> List<E> takeWhile(@NotNull Predicate<? super E> p, @NotNull Collection<E> collection) {
        @NotNull List<E> list = new ArrayList<>();

        for (E e : collection) {
           if (p.apply(e)) {
               list.add(e);
           } else {
               break;
           }
        }

        return list;
    }

    /**
     * returns the first few elements not satisfying the predicate
     * @param p is predicate to check
     * @param collection with elements to take
     * @param <E> type of element
     */
    @NotNull
    public static <E> List<E> takeUnless(@NotNull Predicate<? super E> p, @NotNull Collection<E> collection) {
        return takeWhile(p.not(), collection);
    }

    /**
     * reduces collection by applying function f to each element
     * in natural order and storing intermediate result in accumulator
     * @param f function to be applied
     * @param acc accumulator with initial value
     * @param collection to fold
     * @param <T> type of accumulator
     * @param <E> type of elements
     * @return final acc value
     */
    public static <T, E> T foldl(@NotNull Function2<? super T, ? super E, ? extends T> f, T acc, @NotNull Collection<E> collection) {
        for (E e : collection) {
            acc = f.apply(acc, e);
        }

        return acc;
    }

    /**
     * reduces collection by applying function f to each element
     * in reverse order and storing intermediate result in accumulator
     * @param f function to be applied
     * @param acc accumulator with initial value
     * @param collection to fold
     * @param <T> type of accumulator
     * @param <E> type of elements
     * @return final acc value
     */

    public static <T, E> T foldr(@NotNull Function2<? super E, ? super T, ? extends T> f, T acc, @NotNull Collection<E> collection) {

        @NotNull List<E> elements = new ArrayList<>();
        elements.addAll(collection);

        @NotNull ListIterator<E> iterator = elements.listIterator(elements.size());

        while (iterator.hasPrevious()) {
            acc = f.apply(iterator.previous(), acc);
        }

        return acc;
    }

}
