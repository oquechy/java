package ru.spbau.mit.oquechy.fp;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class Function1Test {

    private static final Object[] OBJECTS = {1, "lisa", new int[]{1, 2, 3}, 6.00001};

    @Test
    public void testApply() {
        {
            @NotNull
            Function1<Integer, String> f1 = Object::toString;
            for (int i = 0; i < 5; i++) {
                assertThat(f1.apply(i), equalTo(Integer.toString(i)));
            }
        }

        {
            @NotNull Function1<Object, Boolean> f1 = o -> o.getClass() == Integer.class;
            for (int i = 0; i < OBJECTS.length; i++) {
                assertThat(f1.apply(OBJECTS[i]), equalTo(i == 0));
            }
        }
    }

    @Test
    public void testCompose() {
        @NotNull
        Function1<Object, String> f1 = Object::toString;
        Function1<String, Integer> g1 = Integer::parseInt;

        for (int i = 0; i < 5; i++) {
            assertThat(f1.compose(g1).apply(i), is(i));
            assertThat(f1.compose(f1).apply(i), equalTo(Integer.toString(i)));
        }
    }

}