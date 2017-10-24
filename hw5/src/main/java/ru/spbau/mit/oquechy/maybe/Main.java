package ru.spbau.mit.oquechy.maybe;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

/**
 * reads input file (given in first argument) line by line
 * tries to parse Integer from each line
 * result is stored in Maybe class
 *
 * each parsed Integer will be put in output file (second argument) squared
 * other lines will be replaced with "null"
 */
public class Main {

    final static String USAGE = "<input file> <output file>";

    public static void main(@NotNull String[] args) throws IOException, AccessToNothingException {
        if (args.length != 2) {
            System.err.println(USAGE);
            return;
        }

        ArrayList<Maybe<Integer>> maybes;
        try(BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            maybes = parseLines(in);
        }

        maybes = square(maybes);
        try (FileWriter out = new FileWriter(args[1])) {
            putMaybes(maybes, out);
        }

    }

    private static void putMaybes(@NotNull ArrayList<Maybe<Integer>> maybes, @NotNull FileWriter out)
            throws AccessToNothingException, IOException {
        for (Maybe<Integer> maybe : maybes) {
            out.write(maybe.isPresent() ? String.valueOf(maybe.get()) + '\n': "null\n");
        }
    }

    @NotNull
    private static ArrayList<Maybe<Integer>> square(@NotNull ArrayList<Maybe<Integer>> maybes) {
        final ArrayList<Maybe<Integer>> maybesCopy = new ArrayList<>();
        for (Maybe<Integer> maybe : maybes) {
            maybesCopy.add(maybe.map(x -> x * x));
        }
        return maybesCopy;
    }

    @NotNull
    private static ArrayList<Maybe<Integer>> parseLines(@NotNull BufferedReader br) throws IOException {
        ArrayList<Maybe<Integer>> maybes = new ArrayList<>();
            for (String line; (line = br.readLine()) != null; ) {
                final Integer i = parseInt(line);
                maybes.add(i == null ? Maybe.nothing() : Maybe.just(i));
            }
        return maybes;
    }

    private static Integer parseInt(@NotNull String line) {
        try {
            return Integer.valueOf(line);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
