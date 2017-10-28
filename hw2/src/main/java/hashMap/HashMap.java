package hashMap;

import java.util.Objects;

/**
 * Naive implementation of hash table for strings
 */
public class HashMap {

    private final static int INIT_CAPACITY = 30;
    private final static double OVERFLOW_LIMIT = 1. / 3;

    private int keysNum;
    private StringList[] table;

    /**
     * class constructor
     */
    public HashMap() {
        clear();
    }

    /**
     * @return total number of keys in table
     */
    public int size() {
        return keysNum;
    }

    /**
     * @param key to check
     * @return true if table contains the key and false otherwise
     */
    public boolean contains(String key) {
        return getEntry(key) != null;
    }

    /**
     * @param key to get corresponding value
     * @return value
     */
    public String get(String key) {
        StringList.Entry entry = getEntry(key);
        return entry == null ? null : entry.value;
    }

    private StringList.Entry getEntry(String key) {
        int index = index(key);
        return table[index] == null ? null : table[index].get(key);
    }

    private int index(String key) {
        return Objects.hashCode(key) % table.length;
    }

    /**
     * @param key   is key for new entry
     * @param value is value for new entry
     * @return old value if exists and null otherwise
     */
    public String put(String key, String value) {
        StringList.Entry entry = getEntry(key);
        if (entry != null) {
            String oldValue = entry.value;
            entry.value = value;
            return oldValue;
        } else {
            keysNum++;
            if ((double) keysNum / table.length >= OVERFLOW_LIMIT) extend();

            int index = index(key);
            table[index] = StringList.add(table[index], key, value);
            return null;
        }
    }

    private void extend() {
        StringList[] oldTable = table;
        table = new StringList[2 * table.length];

        for (StringList list : oldTable) {
            for (; list != null; list = list.next) {
                int index = index(list.entry.key);
                table[index] = StringList.add(table[index], list.entry.key, list.entry.value);
            }
        }
    }

    /**
     * @param key to delete from table with its value
     * @return deleted value if exists and null otherwise
     */
    public String remove(String key) {
        int i = index(key);
        if (table[i] == null) return null;


        StringList.Entry entry = table[i].get(key);

        if (entry != null) {
            table[i] = StringList.delete(table[i], key);
            keysNum--;
            return entry.value;
        }

        return null;
    }

    /**
     * resets hash table
     */
    public void clear() {
        keysNum = 0;
        table = new StringList[INIT_CAPACITY];
    }
}
