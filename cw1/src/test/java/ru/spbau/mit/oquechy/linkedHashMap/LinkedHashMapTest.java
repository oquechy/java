package ru.spbau.mit.oquechy.linkedHashMap;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class LinkedHashMapTest {

    final static String[] VALUES = new String[]{"one", "two", "three", "four"};

    @Test
    public void testConstructor() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        assertEquals(0, map.size());
        assertFalse(map.containsKey("key"));
        assertEquals(null, map.get("key"));
    }

    @Test
    public void testPut() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        init(map);

        assertEquals(VALUES.length, map.size());

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.put(String.valueOf(i + 1), VALUES[VALUES.length - i - 1]);
            assertEquals(VALUES[i], value);
        }

        assertEquals(VALUES.length, map.size());
    }

    @Test
    public void testPutNull() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        initNull(map);

        assertEquals("value", map.get(null));
        assertEquals(null, map.get("key"));
    }

    @Test
    public void testContainsKey() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        init(map);

        for (int i = 0; i < VALUES.length; i++) {
            assertTrue(map.containsKey(String.valueOf(i + 1)));
        }

        assertFalse(map.containsKey("hey"));
    }

    @Test
    public void testContainsNull() {
        LinkedHashMap map = new LinkedHashMap();

        assertFalse(map.containsKey(null));
        map.put(null, "value");
        assertTrue(map.containsKey(null));
    }

    @Test
    public void testSize() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        assertEquals(0, map.size());

        init(map);

        assertEquals(VALUES.length, map.size());
    }

    @Test
    public void testGet() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        init(map);

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.get(String.valueOf(i + 1));
            assertEquals(VALUES[i], value);
        }

        for (int i = 0; i < VALUES.length; i++) {
            map.put(String.valueOf(i + 1), VALUES[VALUES.length - i - 1]);
        }

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.get(String.valueOf(i + 1));
            assertEquals(VALUES[VALUES.length - i - 1], value);
        }
    }

    @Test
    public void testGetNull() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        initNull(map);

        assertEquals("value", map.get(null));
        assertEquals(null, map.get("key"));
        assertEquals(null, map.get("hey"));
    }

    @Test
    public void testClear() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        init(map);
        map.clear();

        assertEquals(0, map.size());
        for (int i = 0; i < VALUES.length; i++) {
            assertEquals(null, map.get(String.valueOf(i + 1)));
            assertFalse(map.containsKey(String.valueOf(i + 1)));
        }
    }

    @Test
    public void testRemove() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        init(map);

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.remove(String.valueOf(i + 1));
            assertEquals(VALUES[i], value);
        }

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.remove(String.valueOf(i + 1));
            assertEquals(null, value);
        }
    }

    @Test
    public void testRemoveNull() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        initNull(map);

        assertEquals("value", map.remove(null));
        assertEquals(null, map.remove("key"));
        assertEquals(null, map.remove("hey"));
    }

    private void initNull(LinkedHashMap<String, String> map) {
        map.put(null, "value");
        map.put("key", null);
    }

    private void init(LinkedHashMap<String, String> map) {
        for (int i = 0; i < VALUES.length; i++) {
            map.put(String.valueOf(i + 1), VALUES[i]);
        }
    }

    @Test
    public void testEntrySet() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        init(map);

        assertEquals(VALUES.length, map.entrySet().size());
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            assertEquals(String.valueOf(i + 1), entry.getKey());
            assertEquals(VALUES[i++], entry.getValue());
        }
    }

    @Test
    public void testEntrySetWithRemove() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        init(map);
        map.remove(String.valueOf(2));
        map.put(String.valueOf(2), VALUES[1]);
        assertEquals(VALUES.length, map.entrySet().size());
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            switch (i) {
                case 0:
                    assertEquals(String.valueOf(1), entry.getKey());
                    assertEquals(VALUES[i++], entry.getValue());
                    break;
                case 1:
                    assertEquals(String.valueOf(3), entry.getKey());
                    assertEquals(VALUES[i++ + 1], entry.getValue());
                    break;
                case 2:
                    assertEquals(String.valueOf(4), entry.getKey());
                    assertEquals(VALUES[i++ + 1], entry.getValue());
                    break;
                case 3:
                    assertEquals(String.valueOf(2), entry.getKey());
                    assertEquals(VALUES[1], entry.getValue());
                    break;
            }
        }
    }

    @Test
    public void testEntrySetWithChangeValueForKey() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        init(map);
        map.put(String.valueOf(2), VALUES[1]);
        assertEquals(VALUES.length, map.entrySet().size());
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            switch (i) {
                case 0:
                    assertEquals(String.valueOf(1), entry.getKey());
                    assertEquals(VALUES[i++], entry.getValue());
                    break;
                case 1:
                    assertEquals(String.valueOf(3), entry.getKey());
                    assertEquals(VALUES[i++ + 1], entry.getValue());
                    break;
                case 2:
                    assertEquals(String.valueOf(4), entry.getKey());
                    assertEquals(VALUES[i++ + 1], entry.getValue());
                    break;
                case 3:
                    assertEquals(String.valueOf(2), entry.getKey());
                    assertEquals(VALUES[1], entry.getValue());
                    break;
            }
        }
    }

}