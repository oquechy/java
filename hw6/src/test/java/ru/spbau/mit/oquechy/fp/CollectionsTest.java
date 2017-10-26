package ru.spbau.mit.oquechy.fp;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CollectionsTest {
    private static final List<Object> OBJECTS = new ArrayList<>(
            Arrays.asList(1, 4, "lisa", "", new int[]{1, 2, 3}, new int[]{}, 6.00001, -5.5));

    @Test
    public void testMap() {
        Function1<Object, Integer> hash = Object::hashCode;
        List<Integer> map = Collections.map(hash, OBJECTS);
        assertThat(map, hasSize(OBJECTS.size()));
        for (int i = 0; i < map.size(); i++) {
            Integer h = map.get(i);
            assertThat(h, is(OBJECTS.get(i).hashCode()));
        }
    }

    @Test
    public void testFilter() {
        Predicate<Object> isNum = o -> o.getClass() == Integer.class ||o.getClass() == Double.class;
        List<Object> filter = Collections.filter(isNum, OBJECTS);
        Object ans[] = {1, 4, 6.00001, -5.5};
        assertThat(filter, hasSize(ans.length));
        for (int i = 0; i < filter.size(); i++) {
            Object h = filter.get(i);
            assertThat(h, is(ans[i]));
        }
    }

    @Test
    public void takeWhile() {
        Predicate<Object> isInt = o -> o.getClass() == Integer.class;
        List<Object> notArray = Collections.takeWhile(isInt, OBJECTS);
        Object ans[] = {1, 4};
        assertThat(notArray, hasSize(ans.length));
        for (int i = 0; i < notArray.size(); i++) {
            Object h = notArray.get(i);
            assertThat(h, is(ans[i]));
        }
    }

    @Test
    public void takeUnless() {
        Predicate<Object> isArray = o -> o.getClass() == int[].class;
        List<Object> notArray = Collections.takeUnless(isArray, OBJECTS);
        Object ans[] = {1, 4, "lisa", ""};
        assertThat(notArray, hasSize(ans.length));
        for (int i = 0; i < notArray.size(); i++) {
            Object h = notArray.get(i);
            assertThat(h, is(ans[i]));
        }
    }

    @Test
    public void foldl() {
        Collection<String> list = new LinkedList<>();
        list.add("ell");
        list.add("0, \\|\\|");
        list.add("orl");
        list.add("d!");
        Function2<String, String, String> inefficientConcatWithQ = (s1, s2) -> s1 + "Q" + s2;

        String concat  = Collections.foldl(inefficientConcatWithQ, "H", list);
        assertThat(concat, is("HQellQ0, \\|\\|QorlQd!"));
    }

    @Test
    public void foldr() {
        Collection<Boolean> set = new HashSet<>();
        set.add(true);
        set.add(false);
        set.add(true);
        set.add(true);
        set.add(false);
        Function2<Object, Integer, Integer> hashSumPlusNum = (o, acc) -> acc + o.hashCode() + 1;

        Integer sum  = Collections.foldr(hashSumPlusNum, 0, set);
        assertThat(sum, is(2 + Boolean.TRUE.hashCode() + Boolean.FALSE.hashCode()));
    }

}