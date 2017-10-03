package ru.spbau.mit.oquechy.Trie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TrieTest {

    private Trie trie;

    @Before
    public void setUp() {
        trie = new Trie();
    }

    @Test
    public void testContains() {
        Assert.assertFalse(trie.contains(""));
        Assert.assertFalse(trie.contains("lisa"));

        trie.add("lisa");
        Assert.assertTrue(trie.contains("lisa"));

        trie.add("lisa");
        Assert.assertTrue(trie.contains("lisa"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsNull() {
        trie.contains(null);
    }

    @Test
    public void testAdd() {
        Assert.assertTrue(trie.add(""));
        Assert.assertTrue(trie.contains(""));

        assertFill(true);
        assertContains(true);

        assertFill(false);
        assertContains(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddNull() {
        trie.add(null);
    }

    private void assertFill(boolean b) {
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(b, trie.add(Integer.toBinaryString(i)));
        }
    }

    private void assertContains(boolean b) {
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(b, trie.contains(Integer.toBinaryString(i)));
        }
    }

    @Test
    public void testSize() {
        Assert.assertEquals(0, trie.size());

        for (int i = 0; i < 10; i++) {
            trie.add(Integer.toString(i));
            Assert.assertEquals(i + 1, trie.size());
        }

        trie.add("");
        Assert.assertEquals(11, trie.size());

        for (int i = 0; i < 10; i++) {
            trie.add(Integer.toString(i));
            Assert.assertEquals(11, trie.size());
        }
    }

    @Test
    public void testRemove() {

        fill();

        Assert.assertTrue(trie.remove(""));
        Assert.assertFalse(trie.contains(""));

        assertEmpty(true);
        assertContains(false);

        assertEmpty(false);
    }

    private void assertEmpty(boolean b) {
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(b, trie.remove(Integer.toBinaryString(i)));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveNull() {
        trie.remove(null);
    }

    @Test
    public void testHowManyStartsWithPrefix() {
        trie.add("abc");
        trie.add("abd");
        trie.add("waaa");
        trie.add("waab");
        trie.add("wabw");
        trie.add("jr");

        Assert.assertEquals(6, trie.howManyStartsWithPrefix(""));
        Assert.assertEquals(1, trie.howManyStartsWithPrefix("j"));
        Assert.assertEquals(3, trie.howManyStartsWithPrefix("wa"));
        Assert.assertEquals(1, trie.howManyStartsWithPrefix("abc"));
        Assert.assertEquals(2, trie.howManyStartsWithPrefix("ab"));
        Assert.assertEquals(0, trie.howManyStartsWithPrefix("waaaa"));

        trie.add("");
        Assert.assertEquals(7, trie.howManyStartsWithPrefix(""));

        trie.add("wa");
        Assert.assertEquals(4, trie.howManyStartsWithPrefix("wa"));

        trie.remove("waaa");
        Assert.assertEquals(3, trie.howManyStartsWithPrefix("wa"));

        trie.remove("waab");
        Assert.assertEquals(0, trie.howManyStartsWithPrefix("waa"));

        trie.add("waaj");
        Assert.assertEquals(1, trie.howManyStartsWithPrefix("waa"));

    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        fill();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        trie.serialize(outputStream);
        Trie trieCopy = new Trie();
        trieCopy.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));

        assertEqualTries(trie, trieCopy);
    }

    private void assertEqualTries(Trie trie, Trie trieCopy) {
        Assert.assertEquals(trie.size(), trieCopy.size());

        Assert.assertEquals(trie.contains(""), trieCopy.contains(""));
        Assert.assertEquals(trie.howManyStartsWithPrefix(""), trieCopy.howManyStartsWithPrefix(""));

        for (int i = 0; i < 10; i++) {
            final String element = Integer.toBinaryString(i);
            Assert.assertEquals(trie.contains(element), trieCopy.contains(element));
            Assert.assertEquals(trie.howManyStartsWithPrefix(element), trieCopy.howManyStartsWithPrefix(element));
        }

        for (int i = 10; i < 20; i++) {
            final String element = Integer.toBinaryString(i);
            Assert.assertEquals(trie.contains(element), trieCopy.contains(element));
            Assert.assertEquals(trie.howManyStartsWithPrefix(element), trieCopy.howManyStartsWithPrefix(element));
        }
    }

    private void fill() {
        trie.add("");
        for (int i = 0; i < 10; i++) {
            trie.add(Integer.toBinaryString(i));
        }
    }

}