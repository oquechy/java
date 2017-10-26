package zip;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * created by Lisa Vasilenko
 * on 27.09.17
 *
 * extracts files, which match regex, from given zips
 * to current or specified directory
 *
 * if there are different files with same name,
 * these files will be renamed such as
 * [name].[ext] -> [name] (num).[ext]
 *
 */
public class Unzip {

    private static final String USAGE = "usage: <path to zip file or directory> " +
            "<regex for file name> " +
            "[output directory]";

    public static void main(String[] args) throws IOException {

        if (args.length != 2 && args.length != 3) {
            System.err.println(USAGE);
        }

        File file = new File(args[0]);
        List<File> zips = new ArrayList<>();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (isZip(f)) {
                        zips.add(f);
                    }
                }
            }
        } else if (isZip(file)) {
            zips.add(file);
        } else {
            System.err.println(USAGE);
            return;
        }


        for (File zip : zips) {
            System.out.println("Processing " + zip.getName() + ":");
            extractFile(zip, args[1], args.length == 3 ? toDir(args[2]) : "");
            System.out.println();
        }
    }

    private static String toDir(String arg) {
        return arg.endsWith("/") ? arg : arg + "/";
    }

    private static boolean isZip(File file) throws IOException {
        if (file.isDirectory()) return false;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long n = raf.readInt();
            return n == 0x504B0304;
        }
    }

    private static void extractFile(File zipPath, String filePattern, String outputDirectory) throws IOException {
        List<String> toExtract = new ArrayList<>();

        try (ZipFile zipFile = new ZipFile(zipPath)) {
            Enumeration zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                String name = ((ZipEntry) zipEntries.nextElement()).getName();
                String fileName = cutPath(name);
                if (Pattern.matches(filePattern, fileName)) {
                    toExtract.add(name);
                }
            }
        }

        for (String file : toExtract) {
            try (FileSystem fileSystem = FileSystems.newFileSystem(zipPath.toPath(), null)) {
                Path fileToExtract = fileSystem.getPath(file);
                System.out.print('\t' + file);
                System.out.println(" --> " + makeCopy(outputDirectory + cutPath(file), fileToExtract));
            }
        }

    }

    private static String cutPath(String name) {
        int i = name.lastIndexOf('/');
        return i == -1 ? name : name.substring(i + 1);
    }

    private static String makeCopy(String file, Path fileToExtract) throws IOException {
        int i = 1;
        boolean copied = false;
        String name = file;

        while (!copied) {
            try {
                Files.copy(fileToExtract, Paths.get(name));
                copied = true;
            } catch (FileAlreadyExistsException e) {
                name = rename(file, i++);
            }
        }
        return name;
    }

    private static String rename(String file, int i) {
        String name = removeExt(file);
        String ext = getExt(file);
        return name + " (" + i + ")" + ext;
    }

    private static String getExt(String file) {
        int i = file.lastIndexOf('.');
        return i == -1 ? "" : file.substring(i);
    }

    private static String removeExt(String file) {
        int i = file.lastIndexOf('.');
        return i == -1 ? file : file.substring(0, i);
    }

}
