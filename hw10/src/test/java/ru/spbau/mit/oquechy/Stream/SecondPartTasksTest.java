package ru.spbau.mit.oquechy.Stream;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    private static final String[] HEMINGWAY_QUOTES = {
            "My cat is very fat.\n",
            "Never go on trips with anyone you do not love.\n",
            "Courage is grace under pressure.\n",
            "Happiness in intelligent people is the rarest thing I know.\n",
            "All things truly wicked start from innocence.\n",
            "Never mistake motion for action.\n",
            "I never had to choose a subject - my subject rather chose me.\n",
            "Somebody just back of you while you are fishing is as bad as someone\n" +
                    " looking over your shoulder while you write a letter to your girl.\n",
            "That terrible mood of depression of whether it's any good\n" +
                    "or not is what is known as The Artist's Reward.\n",
            "I know only that what is moral is what you feel good\n" +
                    "after and what is immoral is what you feel bad after.\n",
            "\n is",
            ""
    };

    private static final String[] ANSWERS = {
            "My cat is very fat.",
            "Courage is grace under pressure.",
            "Happiness in intelligent people is the rarest thing I know.",
            "Never mistake motion for action.",
            "Somebody just back of you while you are fishing is as bad as someone",
            "or not is what is known as The Artist's Reward.",
            "I know only that what is moral is what you feel good",
                    "after and what is immoral is what you feel bad after.",
            " is"
    };

    @Test
    public void testFindQuotes() throws IOException {
        List<String> fileNames = new ArrayList<>();

        for (int i = 0; i < HEMINGWAY_QUOTES.length; i += 2) {
            File temp = File.createTempFile("quote", ".tmp");

            fileNames.add(temp.getPath());

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
                bw.write(HEMINGWAY_QUOTES[i]);
                bw.write(HEMINGWAY_QUOTES[i + 1]);
            }
        }

        List<String> is = SecondPartTasks.findQuotes(fileNames, "is");

        assertEquals(ANSWERS.length, is.size());
        for (int i = 0; i < is.size(); i++) {
            assertEquals(ANSWERS[i], is.get(i));
        }

    }

    private static final double CIRCLE_AREA = Math.PI / 4;

    @Test
    public void testPiDividedBy4() {
        assertEquals(CIRCLE_AREA, SecondPartTasks.piDividedBy4(), 0.01);
        assertEquals(CIRCLE_AREA, SecondPartTasks.piDividedBy4(), 0.01);
        assertEquals(CIRCLE_AREA, SecondPartTasks.piDividedBy4(), 0.01);
    }

    @Test
    public void testFindPrinter() {

        Map<String, List<String>> hemingway = ImmutableMap.of(
                "Young Hemingway",
                Arrays.asList(HEMINGWAY_QUOTES[0], HEMINGWAY_QUOTES[1], HEMINGWAY_QUOTES[2]),
                "Old Hemingway",
                Arrays.asList(HEMINGWAY_QUOTES[7], HEMINGWAY_QUOTES[8], HEMINGWAY_QUOTES[9]),
                "Mature Hemingway",
                Arrays.asList(HEMINGWAY_QUOTES[3], HEMINGWAY_QUOTES[4], HEMINGWAY_QUOTES[5], HEMINGWAY_QUOTES[6])
                );

        assertEquals("Old Hemingway", SecondPartTasks.findPrinter(hemingway));

    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> spar = ImmutableMap.of(
                "Apple juice", 3,
                "Pasta", 6
        );

        Map<String, Integer> prisma = ImmutableMap.of(
                "Apple juice", 2,
                "Corn flakes", 4,
                "Pasta", 6,
                "Pizza", 2
        );

        Map<String, Integer> okay = ImmutableMap.of(
                "Apple juice", 6,
                "Pizza", 1,
                "Taco", 3,
                "Corn flakes", 3
        );

        Map<String, Integer> summary = ImmutableMap.of(
                "Apple juice", 11,
                "Pasta", 12,
                "Corn flakes", 7,
                "Pizza", 3,
                "Taco", 3
        );

        assertEquals(summary, SecondPartTasks.calculateGlobalOrder(Arrays.asList(spar, prisma, okay)));
    }
}