package ru.spbau.mit.oquechy.hashMap;

import org.junit.Test;

import org.junit.Assert;

public class StringListTest {

    private final static String[] VALUES = new String[]{"one", "two", "three", "four"};

    @Test
    public void testAdd() {

        ListIterator[] nodes = new ListIterator[VALUES.length + 1];

        for (int i = 0; i < VALUES.length; i++) {
            String key = String.valueOf(i + 1);
            nodes[i + 1] = ListIterator.add(nodes[i], key, VALUES[i]);
            assertEntry(VALUES[i], key, nodes[i + 1].getEntry());
            Assert.assertEquals(nodes[i], nodes[i + 1].getNext());
            Assert.assertEquals(null, nodes[i + 1].getPrev());
            if (nodes[i] != null) {
                Assert.assertEquals(nodes[i + 1], nodes[i].getPrev());
            }
        }
    }

    @Test
    public void testAddNull() {
        ListIterator head = initNull();
        head = ListIterator.add(head, null, null);
    }

    @Test
    public void testGet() {
        ListIterator head = init();

        for (int i = 0; i < VALUES.length; i++) {
            String key = String.valueOf(i + 1);
            ListIterator.Entry entry = head.get(key);
            assertEntry(VALUES[i], key, entry);
        }
    }

    @Test
    public void testGetNull() {
        ListIterator head = initNull();

        ListIterator.Entry entry = head.get(null);
        assertEntry("value", null, entry);

        entry = head.get("key");
        assertEntry(null, "key", entry);

        entry = head.get("hey");
        Assert.assertEquals(null, entry);
    }

    private ListIterator initNull() {
        ListIterator head = ListIterator.add(null, null, "value");
        head = ListIterator.add(head, "key", null);
        return head;
    }

    @Test
    public void testDelete() {
        ListIterator[] nodes = new ListIterator[VALUES.length + 1];

        for (int i = 0; i < VALUES.length; i++) {
            nodes[i + 1] = ListIterator.add(nodes[i], String.valueOf(i + 1), VALUES[i]);
        }

        ListIterator head = nodes[VALUES.length - 1];

        for (int i = VALUES.length - 1; i >= 0; i--) {
            head = ListIterator.delete(head, String.valueOf(i + 1));
            Assert.assertEquals(nodes[i], head);
        }

        for (int i = VALUES.length - 1; i >= 0; i--) {
            head = ListIterator.delete(head, String.valueOf(i + 1));
            Assert.assertEquals(null, head);
        }
    }

    private void assertEntry(String value, String key, ListIterator.Entry entry) {
        Assert.assertEquals(key, entry.key);
        Assert.assertEquals(value, entry.value);
    }

    private ListIterator init() {
        ListIterator head = null;

        for (int i = 0; i < VALUES.length; i++) {
            head = ListIterator.add(head, String.valueOf(i + 1), VALUES[i]);
        }
        return head;
    }
}