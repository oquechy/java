package ru.spbau.mit.oquechy.set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetTest {

    static private final int[] VALUES = new int[]{8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15};

    private Set<Integer> iset;
    private Set<String> sset;

    @Before
    public void setUp() {
        iset = new Set<>();
        sset = new Set<>();
    }

    @Test
    public void testAdd() {
        for (int i = 0; i < 10; i++) {
            assertTrue(iset.add(VALUES[i]));
            assertTrue(sset.add(Integer.toBinaryString(VALUES[i])));
        }

        for (int i = 0; i < 10; i++) {
            assertFalse(iset.add(VALUES[i]));
            assertFalse(sset.add(Integer.toBinaryString(VALUES[i])));
        }
    }

    @Test
    public void testContains() {
        for (int i = 0; i < 10; i++) {
            assertFalse(iset.contains(VALUES[i]));
            assertFalse(sset.contains(Integer.toBinaryString(VALUES[i])));
        }

        for (int i = 0; i < 10; i++) {
            iset.add(VALUES[i]);
            sset.add(Integer.toBinaryString(VALUES[i]));
        }

        for (int i = 0; i < 10; i++) {
            assertTrue(iset.contains(VALUES[i]));
            assertTrue(sset.contains(Integer.toBinaryString(VALUES[i])));
        }
    }

    @Test
    public void testSize() {
        assertEquals(0, iset.size());
        assertEquals(0, sset.size());

        for (int i = 0; i < 10; i++) {
            iset.add(VALUES[i]);
            sset.add(Integer.toBinaryString(VALUES[i]));
            assertEquals(i + 1, iset.size());
            assertEquals(i + 1, sset.size());
        }
    }
}