package ru.spbau.mit.oquechy.maybe;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class MainTest {

    private final String INPUT = "l1s4\n" +
            "1s\n" +
            "a\n" +
            "pr3tty\n" +
            "\n" +
            "0\n" +
            "\n" +
            "\"\"\n" +
            "g00d\n" +
            "pr0gramm3r\n" +
            "-5\n" +
            "11\n" +
            ",\n" +
            "1sn't\n" +
            "8he\n" +
            "?\n";

    private final String OUTPUT = "null\n" +
            "null\n" +
            "null\n" +
            "null\n" +
            "null\n" +
            "0\n" +
            "null\n" +
            "null\n" +
            "null\n" +
            "null\n" +
            "25\n" +
            "121\n" +
            "null\n" +
            "null\n" +
            "null\n" +
            "null\n";

    @Test
    public void testEmpty() throws IOException, AccessToNothingException {
        final File in = File.createTempFile("MaybeTest", ".tmp");
        in.deleteOnExit();

        final File out = File.createTempFile("MaybeTest", ".tmp");
        out.deleteOnExit();

        Main.main(new String[]{in.getAbsolutePath(), out.getAbsolutePath()});

        Assert.assertEquals("", new String(Files.readAllBytes(out.toPath())));
    }

    @Test
    public void test() throws IOException, AccessToNothingException {
        final File in = File.createTempFile("MaybeTest", ".tmp");
        in.deleteOnExit();
        File out;
        try (FileWriter writer = new FileWriter(in)) {
            writer.write(INPUT);
        }

        out = File.createTempFile("MaybeTest", ".tmp");
        out.deleteOnExit();

        Main.main(new String[]{in.getAbsolutePath(), out.getAbsolutePath()});

        Assert.assertEquals(OUTPUT, new String(Files.readAllBytes(out.toPath())));
    }
}