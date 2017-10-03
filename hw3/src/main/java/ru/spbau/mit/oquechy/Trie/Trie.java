package ru.spbau.mit.oquechy.Trie;

import java.io.*;
import java.util.HashMap;

/**
 * implementation of trie structure for string storage,
 * based on hash tables
 *
 * null-string storage is unsupported
 *
 */
class Trie implements Serializable {

    private Node root = new Node();

    private static class Node implements Serializable {
        boolean terminal = false;
        int size = 0;
        HashMap<Character, Node> next = new HashMap<>();
    }

    /**
     * adds new element to trie with O(|element|) complexity
     * returns true if it wasn't such key in trie before
     * @param element is key to add to storage
     */
    boolean add(String element) {
        validateKey(element);

        if (contains(element)) {
            return false;
        }

        Node cur = root;
        root.size++;

        for (char c : element.toCharArray()) {
            Node next = cur.next.get(c);
            if (next == null) {
                next = new Node();
                cur.next.put(c, next);
            }
            cur = next;
            cur.size++;
        }

        cur.terminal = true;
        return true;
    }

    /**
     * returns total number of storing strings
     */
    int size() {
        return root.size;
    }

    /**
     * checks the existence of the element
     * @param element to check
     */
    boolean contains(String element) {
        validateKey(element);

        Node cur = root;

        for (char c : element.toCharArray()) {
            Node next = cur.next.get(c);
            if (next == null) {
                return false;
            }
            cur = next;
        }

        return cur.terminal;
    }

    /**
     * removes element from the trie with O(|element|) complexity
     * returns true if given key really was in trie
     * @param element to be removed
     */
    boolean remove(String element) {
        validateKey(element);

        if (!contains(element)) {
            return false;
        }

        Node cur = root;
        root.size--;

        for (char c : element.toCharArray()) {
            Node next = cur.next.get(c);
            next.size--;
            if (next.size == 0) {
                cur.next.remove(c);
                break;
            }
            cur = next;
        }

        cur.terminal = false;
        return true;
    }

    private void validateKey(String element) {
        if (element == null) {
            throw new UnsupportedOperationException("null-string storage is not allowed");
        }
    }

    /**
     * counts total number of strings starting with the prefix
     * @param prefix of strings to be counted
     */
    int howManyStartsWithPrefix(String prefix) {
        validateKey(prefix);

        Node cur = root;

        for (char c : prefix.toCharArray()) {
            Node next = cur.next.get(c);
            if (next == null) {
                return 0;
            }
            cur = next;
        }

        return cur.size;
    }

    /**
     * puts serialization of the trie to given stream
     * @param out is stream to write binary data to
     * @throws IOException if ObjectOutputStream creating fails
     */
    void serialize(OutputStream out) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(out)) {
            objectOutputStream.writeObject(this);
        }
    }

    /**
     * reads serialization of a new trie from given stream
     * and assigns it to an old trie
     * @param in is stream to read binary data from
     * @throws IOException if ObjectInputStream creating fails
     * @throws ClassNotFoundException if it reads not existing class from in
     */
    void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(in)) {
            this.root = ((Trie) objectInputStream.readObject()).root;
        }
    }

}


