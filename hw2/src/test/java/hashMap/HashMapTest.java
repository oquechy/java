package hashMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashMapTest {

    final static String[] VALUES = new String[]{"one", "two", "three", "four"};

    @Test
    void testConstructor() {
        HashMap map = new HashMap();
        assertEquals(0, map.size());
        assertFalse(map.contains("key"));
        assertEquals(null, map.get("key"));
    }

    @Test
    void testPut() {
        HashMap map = new HashMap();

        init(map);

        assertEquals(VALUES.length, map.size());

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.put(String.valueOf(i + 1), VALUES[VALUES.length - i - 1]);
            assertEquals(VALUES[i], value);
        }

        assertEquals(VALUES.length, map.size());
    }

    @Test
    void testPutNull() {
        HashMap map = new HashMap();

        initNull(map);

        assertEquals("value", map.get(null));
        assertEquals(null, map.get("key"));
    }

    @Test
    void testContains() {
        HashMap map = new HashMap();

        init(map);

        for (int i = 0; i < VALUES.length; i++) {
            assertTrue(map.contains(String.valueOf(i + 1)));
        }

        assertFalse(map.contains("hey"));
    }

    @Test
    void testContainsNull() {
        HashMap map = new HashMap();

        assertFalse(map.contains(null));
        map.put(null, "value");
        assertTrue(map.contains(null));
    }

    @Test
    void testSize() {
        HashMap map = new HashMap();

        assertEquals(0, map.size());

        init(map);

        assertEquals(VALUES.length, map.size());
    }

    @Test
    void testGet() {
        HashMap map = new HashMap();

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
    void testGetNull() {
        HashMap map = new HashMap();

        initNull(map);

        assertEquals("value", map.get(null));
        assertEquals(null, map.get("key"));
        assertEquals(null, map.get("hey"));
    }

    @Test
    void testClear() {
        HashMap map = new HashMap();

        init(map);
        map.clear();

        assertEquals(0, map.size());
        for (int i = 0; i < VALUES.length; i++) {
            assertEquals(null, map.get(String.valueOf(i + 1)));
            assertFalse(map.contains(String.valueOf(i + 1)));
        }
    }

    @Test
    void testRemove() {
        HashMap map = new HashMap();

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
    void testRemoveNull() {
        HashMap map = new HashMap();

        initNull(map);

        assertEquals("value", map.remove(null));
        assertEquals(null, map.remove("key"));
        assertEquals(null, map.remove("hey"));
    }

    private void initNull(HashMap map) {
        map.put(null, "value");
        map.put("key", null);
    }

    private void init(HashMap map) {
        for (int i = 0; i < VALUES.length; i++) {
            map.put(String.valueOf(i + 1), VALUES[i]);
        }
    }

}