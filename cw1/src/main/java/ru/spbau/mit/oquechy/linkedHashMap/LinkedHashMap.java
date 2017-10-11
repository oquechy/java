package ru.spbau.mit.oquechy.linkedHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LinkedHashMap<K, V> extends AbstractMap<K, V> {

    private final static int INIT_CAPACITY = 30;
    private final static double OVERFLOW_LIMIT = 1. / 3;

    private int keysNum;
    private List<List<Entry<K, V>>> table;

    @NotNull
    private List<Entry<K, V>> entryList = new ArrayList<>();

    static class Entry<K, V> implements Map.Entry<K, V> {

        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            V oldValue = value;
            value = v;
            return oldValue;
        }
    }

    @NotNull
    @Override
    public Set<Map.Entry<K, V>> entrySet() {

        return new AbstractSet<Map.Entry<K, V>>() {

            int idx = 0;

            @Override
            public int size() {
                return keysNum;
            }

            @NotNull
            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>() {
                    @Override
                    public boolean hasNext() {
                        return entryList.size() > idx;
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        return entryList.get(idx++);
                    }
                };
            }
        };
    }

    /**
     * Returns old value if exists and null otherwise
     * @param key   is key for new entry
     * @param value is value for new entry
     */
    @Nullable
    @Override
    public V put(K key, V value) {
        Entry<K, V> entry = getEntry(key);
        if (entry != null) {
            V oldValue = entry.getValue();
            entryList.remove(entry);
            entry.setValue(value);
            entryList.add(entry);
            return oldValue;
        } else {
            keysNum++;
            if ((double) keysNum / table.size() >= OVERFLOW_LIMIT) extend();

            int index = index(key);
            entry = new Entry<>(key, value);
            table.get(index).add(entry);
            putEntry(entry);

            return null;
        }
    }

    private void putEntry(Entry<K, V> entry) {
        entryList.add(entry);
    }

    /**
     * Returns total number of keys in table
     */
    @Override
    public int size() {
        return keysNum;
    }

    /**
     * Returns value
     * @param key to get corresponding value
     */
    @Nullable
    @Override
    public V get(Object key) {
        Entry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    /**
     * class constructor
     */
    public LinkedHashMap() {
        clear();
    }

    /**
     * @param key to check
     * @return true if table contains the key and false otherwise
     */
    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    private Entry<K, V> getEntry(Object key) {
        int index = index(key);
        return table.get(index) == null ? null : searchForKey(table.get(index), key);
    }

    private Entry<K, V> searchForKey(@NotNull List<Entry<K, V>> entries, Object key) {
        for (Entry<K, V> entry : entries) {
            if (Objects.equals(entry.getKey(), key)) {
                return entry;
            }
        }

        return null;
    }

    private int index(Object key) {
        return (Objects.hashCode(key) % table.size() + table.size()) % table.size();
    }

    private void extend() {
        List<List<Entry<K, V>>> oldTable = table;

        resizeTable(table.size() * 2);

        for (List<Entry<K, V>> list : oldTable) {
            for (Entry<K, V> entry : list) {
                int index = index(entry.getKey());
                table.get(index).add(entry);
            }
        }
    }

    /**
     * @param key to delete from table with its value
     * @return deleted value if exists and null otherwise
     */
    @Nullable
    @Override
    public V remove(Object key) {
        int i = index(key);
        if (table.get(i) == null) {
            return null;
        }


        Entry<K, V> entry = searchForKey(table.get(i), key);

        if (entry != null) {
            entryList.remove(entry);
            removeKey(table.get(i), key);
            keysNum--;
            return entry.value;
        }

        return null;
    }

    private void removeKey(@NotNull List<Entry<K, V>> entries, Object key) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K, V> entry = entries.get(i);
            if (Objects.equals(entry.getKey(), key)) {
                entries.remove(i);
            }
        }
    }

    /**
     * resets hash table
     */
    public void clear() {
        keysNum = 0;
        resizeTable(INIT_CAPACITY);
    }

    private void resizeTable(int capacity) {
        table = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            table.add(new ArrayList<>());
        }
    }
}
