package ru.spbau.mit.oquechy.fp;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class PredicateTest {

    private static final Object[] OBJECTS = {"3", 1, "lisa", 0, new int[]{1, 2, 3}, -5, 6.00001};
    private static final int[] INTEGERS = {1, -2, -3, -4, 5, 6, 7, 8, 10};

    private Predicate<Object> evenStringLength;
    private Predicate<Integer> evenInteger;

    @Before
    public void setUp() {
        evenStringLength = o -> (o.toString().length() & 1) == 0;
        evenInteger = i -> (i & 1) == 0;
    }

    @Test
    public void testAlwaysTrue() {
        for (int i = 0; i < 5; i++) {
            Object o = OBJECTS[i];
            assertThat(Predicate.alwaysTrue().apply(o), is(true));
            assertThat(Predicate.alwaysTrue().apply(i), is(true));
        }
    }

    @Test
    public void testAlwaysFalse() {
        for (int i = 0; i < 5; i++) {
            Object o = OBJECTS[i];
            assertThat(Predicate.alwaysFalse().apply(o), is(false));
            assertThat(Predicate.alwaysFalse().apply(i), is(false));
        }
    }

    @Test
    public void testOr() {

        @NotNull final boolean[] ans = {false, true, true, true, false, true, false, true, true};

        for (int i = 0; i < INTEGERS.length; i++) {
            Integer integer = INTEGERS[i];
            assertThat(evenInteger.or(evenStringLength).apply(integer), equalTo(ans[i]));
        }
    }

    @Test
    public void testAnd() {
        @NotNull final boolean[] ans = {false, true, false, true, false, false, false, false, true};

        for (int i = 0; i < INTEGERS.length; i++) {
            Integer integer = INTEGERS[i];
            assertThat(evenInteger.and(evenStringLength).apply(integer), equalTo(ans[i]));
        }
    }

    @Test
    public void testNot() {
        @NotNull final boolean[] ans = {true, false, false, false, true, true, true, true, false};

        for (int i = 0; i < INTEGERS.length; i++) {
            Integer integer = INTEGERS[i];
            assertThat(evenStringLength.not().apply(integer), equalTo(ans[i]));
        }
    }

    @Test
    public void testApply() {
        for (int i = 0; i < 5; i++) {
            assertThat(evenInteger.apply(i), equalTo((i & 1) == 0));
        }

        @NotNull Predicate<Object> isInteger = o -> o.getClass() == Integer.class;
        for (int i = 0; i < OBJECTS.length; i++) {
            assertThat(isInteger.apply(OBJECTS[i]), equalTo((i & 1) != 0));
        }
    }

    @Test
    public void testCompose() {
        @NotNull Predicate<Boolean> not = b -> !b;

        for (int i = 0; i < 5; i++) {
            Object o = OBJECTS[i];
            assertThat(evenStringLength.compose(not).apply(o), equalTo((o.toString().length() & 1) != 0));
            assertThat(not.compose(evenStringLength).apply(o.getClass() == Integer.class), equalTo((i & 1) == 0));
        }
    }

}