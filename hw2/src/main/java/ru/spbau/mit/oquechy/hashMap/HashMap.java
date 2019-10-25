package ru.spbau.mit.oquechy.hashMap;

import java.util.Objects;

/**
 * Naive implementation of hash table for strings
 */
public class HashMap {

    private final static int INIT_CAPACITY = 30;
    private final static double OVERFLOW_LIMIT = 1. / 3;

    private int keysNum;
    private ListIterator[] table;

    /**
     * Class constructor
     */
    public HashMap() {
        clear();
    }

    /**
     * Returns total number of keys in table
     */
    public int size() {
        return keysNum;
    }

    /**
     * Returns true if table contains the key and false otherwise
     * @param key to check
     */
    public boolean contains(String key) {
        return getEntry(key) != null;
    }

    /**
     * Returns value, corresponding to the key
     * @param key to get corresponding value
     */
    public String get(String key) {
        ListIterator.Entry entry = getEntry(key);
        return entry == null ? null : entry.value;
    }

    private ListIterator.Entry getEntry(String key) {
        int index = index(key);
        return table[index] == null ? null : table[index].get(key);
    }

    private int index(String key) {
        return (Objects.hashCode(key) % table.length + table.length) % table.length;
    }

    /**
     * Returns old value if exists and null otherwise
     * @param key   is key for new entry
     * @param value is value for new entry
     */
    public String put(String key, String value) {
        ListIterator.Entry entry = getEntry(key);
        if (entry != null) {
            String oldValue = entry.value;
            entry.value = value;
            return oldValue;
        } else {
            keysNum++;
            if ((double) keysNum / table.length >= OVERFLOW_LIMIT) {
                extend();
            }

            int index = index(key);
            table[index] = ListIterator.add(table[index], key, value);
            return null;
        }
    }

    private void extend() {
        ListIterator[] oldTable = table;
        table = new ListIterator[2 * table.length];

        for (ListIterator list : oldTable) {
            for (; list != null; list = list.getNext()) {
                int index = index(list.getEntry().key);
                table[index] = ListIterator.add(table[index], list.getEntry().key, list.getEntry().value);
            }
        }
    }

    /**
     * Returns deleted value if exists and null otherwise
     * @param key to delete from table with its value
     */
    public String remove(String key) {
        int i = index(key);
        if (table[i] == null) return null;

        ListIterator.Entry entry = table[i].get(key);

        if (entry != null) {
            table[i] = ListIterator.delete(table[i], key);
            keysNum--;
            return entry.value;
        }

        return null;
    }

    /**
     * Resets hash table
     */
    public void clear() {
        keysNum = 0;
        table = new ListIterator[INIT_CAPACITY];
    }
}
