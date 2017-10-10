package ru.spbau.mit.oquechy.hashMap;

import org.junit.Test;

import org.junit.Assert;

public class HashMapTest {

    private final static String[] VALUES = new String[]{"one", "two", "three", "four"};

    @Test
    public void testConstructor() {
        HashMap map = new HashMap();
        Assert.assertEquals(0, map.size());
        Assert.assertFalse(map.contains("key"));
        Assert.assertEquals(null, map.get("key"));
    }

    @Test
    public void testPut() {
        HashMap map = new HashMap();

        init(map);

        Assert.assertEquals(VALUES.length, map.size());

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.put(String.valueOf(i + 1), VALUES[VALUES.length - i - 1]);
            Assert.assertEquals(VALUES[i], value);
        }

        Assert.assertEquals(VALUES.length, map.size());
    }

    @Test
    public void testPutNull() {
        HashMap map = new HashMap();

        initNull(map);

        Assert.assertEquals("value", map.get(null));
        Assert.assertEquals(null, map.get("key"));
    }

    @Test
    public void testContains() {
        HashMap map = new HashMap();

        init(map);

        for (int i = 0; i < VALUES.length; i++) {
            Assert.assertTrue(map.contains(String.valueOf(i + 1)));
        }

        Assert.assertFalse(map.contains("hey"));
    }

    @Test
    public void testContainsNull() {
        HashMap map = new HashMap();

        Assert.assertFalse(map.contains(null));
        map.put(null, "value");
        Assert.assertTrue(map.contains(null));
    }

    @Test
    public void testSize() {
        HashMap map = new HashMap();

        Assert.assertEquals(0, map.size());

        init(map);

        Assert.assertEquals(VALUES.length, map.size());
    }

    @Test
    public void testGet() {
        HashMap map = new HashMap();

        init(map);
        map.put("привет, мир!", "привет :)");

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.get(String.valueOf(i + 1));
            Assert.assertEquals(VALUES[i], value);
        }

        {
            String value = map.get("привет, мир!");
            Assert.assertEquals("привет :)", value);
        }

        for (int i = 0; i < VALUES.length; i++) {
            map.put(String.valueOf(i + 1), VALUES[VALUES.length - i - 1]);
        }

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.get(String.valueOf(i + 1));
            Assert.assertEquals(VALUES[VALUES.length - i - 1], value);
        }
    }

    @Test
    public void testGetNull() {
        HashMap map = new HashMap();

        initNull(map);

        Assert.assertEquals("value", map.get(null));
        Assert.assertEquals(null, map.get("key"));
        Assert.assertEquals(null, map.get("hey"));
    }

    @Test
    public void testClear() {
        HashMap map = new HashMap();

        init(map);
        map.clear();

        Assert.assertEquals(0, map.size());
        for (int i = 0; i < VALUES.length; i++) {
            Assert.assertEquals(null, map.get(String.valueOf(i + 1)));
            Assert.assertFalse(map.contains(String.valueOf(i + 1)));
        }
    }

    @Test
    public void testRemove() {
        HashMap map = new HashMap();

        init(map);

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.remove(String.valueOf(i + 1));
            Assert.assertEquals(VALUES[i], value);
        }

        for (int i = 0; i < VALUES.length; i++) {
            String value = map.remove(String.valueOf(i + 1));
            Assert.assertEquals(null, value);
        }
    }

    @Test
    public void testRemoveNull() {
        HashMap map = new HashMap();

        initNull(map);

        Assert.assertEquals("value", map.remove(null));
        Assert.assertEquals(null, map.remove("key"));
        Assert.assertEquals(null, map.remove("hey"));
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