package hashMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringListTest {

    final static String[] VALUES = new String[]{"one", "two", "three", "four"};

    @Test
    void testAdd() {

        StringList[] nodes = new StringList[VALUES.length + 1];

        for (int i = 0; i < VALUES.length; i++) {
            String key = String.valueOf(i + 1);
            nodes[i + 1] = StringList.add(nodes[i], key, VALUES[i]);
            assertEntry(VALUES[i], key, nodes[i + 1].entry);
            assertEquals(nodes[i], nodes[i + 1].next);
            assertEquals(null, nodes[i + 1].prev);
            if (nodes[i] != null) {
                assertEquals(nodes[i + 1], nodes[i].prev);
            }
        }
    }

    @Test
    void testAddNull() {

        StringList head = initNull();
        head = StringList.add(head, null, null);

    }

    @Test
    void testGet() {

        StringList head = init();

        for (int i = 0; i < VALUES.length; i++) {
            String key = String.valueOf(i + 1);
            StringList.Entry entry = head.get(key);
            assertEntry(VALUES[i], key, entry);
        }

    }

    @Test
    void testGetNull() {
        StringList head = initNull();

        StringList.Entry entry = head.get(null);
        assertEntry("value", null, entry);

        entry = head.get("key");
        assertEntry(null, "key", entry);

        entry = head.get("hey");
        assertEquals(null, entry);
    }

    private StringList initNull() {
        StringList head = StringList.add(null, null, "value");
        head = StringList.add(head, "key", null);
        return head;
    }

    @Test
    void testDelete() {
        StringList[] nodes = new StringList[VALUES.length + 1];

        for (int i = 0; i < VALUES.length; i++) {
            nodes[i + 1] = StringList.add(nodes[i], String.valueOf(i + 1), VALUES[i]);
        }

        StringList head = nodes[VALUES.length - 1];

        for (int i = VALUES.length - 1; i >= 0; i--) {
            head = StringList.delete(head, String.valueOf(i + 1));
            assertEquals(nodes[i], head);
        }

        for (int i = VALUES.length - 1; i >= 0; i--) {
            head = StringList.delete(head, String.valueOf(i + 1));
            assertEquals(null, head);
        }

    }

    private void assertEntry(String value, String key, StringList.Entry entry) {
        assertEquals(key, entry.key);
        assertEquals(value, entry.value);
    }

    private StringList init() {
        StringList head = null;

        for (int i = 0; i < VALUES.length; i++) {
            head = StringList.add(head, String.valueOf(i + 1), VALUES[i]);
        }
        return head;
    }
}