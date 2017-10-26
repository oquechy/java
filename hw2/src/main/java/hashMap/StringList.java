package hashMap;

import java.util.Objects;

/**
 * Created by oquechy on 08.09.17.
 */

class StringList {

    Entry entry;
    StringList prev, next;

    class Entry{

        String key, value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private StringList(String key, String value, StringList prev, StringList next) {
        this.entry = new Entry(key, value);
        this.prev = prev;
        this.next = next;
    }

    Entry get(String key) {
        for (StringList cur = this; cur != null; cur = cur.next) {
            if (Objects.equals(cur.entry.key, key)) {
                return cur.entry;
            }
        }
        return null;
    }

    static StringList add(StringList list, String key, String value) {
        StringList head = new StringList(key, value, null, list);

        if (list != null)
            list.prev = head;

        return head;
    }

    static StringList delete(StringList list, String key) {

        if (list == null) {
            return null;
        }

        StringList head = Objects.equals(list.entry.key, key) ? list.next : list;

        for (StringList cur = list; cur != null; cur = cur.next) {
            if (Objects.equals(cur.entry.key, key)) {
                if (cur.prev != null) cur.prev.next = cur.next;
                if (cur.next != null) cur.next.prev = cur.prev;

                return head;
            }
        }
        return head;
    }
}
