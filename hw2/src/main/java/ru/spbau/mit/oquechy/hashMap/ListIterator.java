package ru.spbau.mit.oquechy.hashMap;

import java.util.Objects;

class ListIterator {

    private Entry entry;
    private ListIterator prev;
    private ListIterator next;

    static class Entry {

        String key, value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public Entry getEntry() {
        return entry;
    }

    public ListIterator getPrev() {
        return prev;
    }

    public ListIterator getNext() {
        return next;
    }

    private ListIterator(String key, String value, ListIterator prev, ListIterator next) {
        this.entry = new Entry(key, value);
        this.prev = prev;
        this.next = next;
    }

    public Entry get(String key) {
        for (ListIterator cur = this; cur != null; cur = cur.next) {
            if (Objects.equals(cur.entry.key, key)) {
                return cur.entry;
            }
        }
        return null;
    }

    public static ListIterator add(ListIterator list, String key, String value) {
        ListIterator head = new ListIterator(key, value, null, list);

        if (list != null)
            list.prev = head;

        return head;
    }

    public static ListIterator delete(ListIterator list, String key) {

        if (list == null) {
            return null;
        }

        ListIterator head = Objects.equals(list.entry.key, key) ? list.next : list;

        for (ListIterator cur = list; cur != null; cur = cur.next) {
            if (Objects.equals(cur.entry.key, key)) {
                if (cur.prev != null) cur.prev.next = cur.next;
                if (cur.next != null) cur.next.prev = cur.prev;

                return head;
            }
        }
        return head;
    }
}
