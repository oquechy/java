package hashMap;

import org.junit.Test;

import org.junit.Assert;

public class StringListTest {

    private final static String[] VALUES = new String[]{"one", "two", "three", "four"};

    @Test
    public void testAdd() {

        StringList[] nodes = new StringList[VALUES.length + 1];

        for (int i = 0; i < VALUES.length; i++) {
            String key = String.valueOf(i + 1);
            nodes[i + 1] = StringList.add(nodes[i], key, VALUES[i]);
            assertEntry(VALUES[i], key, nodes[i + 1].entry);
            Assert.assertEquals(nodes[i], nodes[i + 1].next);
            Assert.assertEquals(null, nodes[i + 1].prev);
            if (nodes[i] != null) {
                Assert.assertEquals(nodes[i + 1], nodes[i].prev);
            }
        }
    }

    @Test
    public void testAddNull() {

        StringList head = initNull();
        head = StringList.add(head, null, null);

    }

    @Test
    public void testGet() {

        StringList head = init();

        for (int i = 0; i < VALUES.length; i++) {
            String key = String.valueOf(i + 1);
            StringList.Entry entry = head.get(key);
            assertEntry(VALUES[i], key, entry);
        }

    }

    @Test
    public void testGetNull() {
        StringList head = initNull();

        StringList.Entry entry = head.get(null);
        assertEntry("value", null, entry);

        entry = head.get("key");
        assertEntry(null, "key", entry);

        entry = head.get("hey");
        Assert.assertEquals(null, entry);
    }

    private StringList initNull() {
        StringList head = StringList.add(null, null, "value");
        head = StringList.add(head, "key", null);
        return head;
    }

    @Test
    public void testDelete() {
        StringList[] nodes = new StringList[VALUES.length + 1];

        for (int i = 0; i < VALUES.length; i++) {
            nodes[i + 1] = StringList.add(nodes[i], String.valueOf(i + 1), VALUES[i]);
        }

        StringList head = nodes[VALUES.length - 1];

        for (int i = VALUES.length - 1; i >= 0; i--) {
            head = StringList.delete(head, String.valueOf(i + 1));
            Assert.assertEquals(nodes[i], head);
        }

        for (int i = VALUES.length - 1; i >= 0; i--) {
            head = StringList.delete(head, String.valueOf(i + 1));
            Assert.assertEquals(null, head);
        }

    }

    private void assertEntry(String value, String key, StringList.Entry entry) {
        Assert.assertEquals(key, entry.key);
        Assert.assertEquals(value, entry.value);
    }

    private StringList init() {
        StringList head = null;

        for (int i = 0; i < VALUES.length; i++) {
            head = StringList.add(head, String.valueOf(i + 1), VALUES[i]);
        }
        return head;
    }
}