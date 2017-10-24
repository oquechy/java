package zip;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UnzipTest {

    final private byte DATA[][] = {
            {1, 2, 3, 4, 5, 6},
            {'L', 'i', 's', 'a'},
            {'a', 'a', 'a', 'a'},
            {10, 20, 30, 40, 50, 60},
            {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}
    };

    @Test
    public void testOneFile() throws IOException {
        final String testName = "testOneFile";

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(testName + ".zip"))) {
            putEntry(out, testName, DATA[0]);
        }

        Unzip.main(new String[]{testName + ".zip", testName});

        Path path = Paths.get(testName);
        Assert.assertTrue(Arrays.equals(DATA[0], Files.readAllBytes(path)));

        Files.delete(path);
        Files.delete(Paths.get(testName + ".zip"));
    }

    private void putEntry(ZipOutputStream out, String fileName, byte[] data) throws IOException {
        out.putNextEntry(new ZipEntry(fileName));
        out.write(data, 0, data.length);
        out.closeEntry();
    }

    @Test
    public void testZeroDepthFiles() throws IOException {
        final String testName = "testZeroDepthFiles";

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(testName + ".zip"))) {
            for (int i = 0; i < DATA.length; i++) {
                putEntry(out, testName + i, DATA[i]);
            }
        }

        Unzip.main(new String[]{testName + ".zip", testName + "\\d"});

        for (int i = 0; i < DATA.length; i++) {
            assertExtracted(testName + i, DATA[i]);
        }

        Files.delete(Paths.get(testName + ".zip"));
    }

    @Test
    public void testDirectories() throws IOException {
        final String testName = "testDirectories";

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(testName + ".zip"))) {
            putEntry(out, testName + "0", DATA[0]);
            out.putNextEntry(new ZipEntry("d1/"));
            putEntry(out, "d1/" + testName + "1", DATA[1]);
            out.putNextEntry(new ZipEntry("d1/d2/"));
            putEntry(out, "d1/d2/" + testName + "2", DATA[2]);
            out.closeEntry();
            out.putNextEntry(new ZipEntry("d1/d3/"));
            putEntry(out, "d1/d3/" + testName + "3", DATA[3]);
            out.closeEntry();
            out.closeEntry();
            out.putNextEntry(new ZipEntry("d4/"));
            putEntry(out, "d4/" + testName + "4", DATA[4]);
            out.closeEntry();
        }

        Unzip.main(new String[]{testName + ".zip", testName + "\\d"});

        for (int i = 0; i < DATA.length; i++) {
            assertExtracted(testName + i, DATA[i]);
        }

        Files.delete(Paths.get(testName + ".zip"));
    }

    private void assertExtracted(String file, byte[] data) throws IOException {
        Path path = Paths.get(file);
        Assert.assertTrue(Arrays.equals(data, Files.readAllBytes(path)));
        Files.delete(path);
    }

}