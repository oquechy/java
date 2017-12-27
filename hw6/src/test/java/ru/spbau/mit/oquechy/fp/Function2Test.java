package ru.spbau.mit.oquechy.fp;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class Function2Test {
    private static final Object[] OBJECTS = {1, 4, "lisa", "", new int[]{1, 2, 3}, new int[]{}, 6.00001, -5.5};
    private static final String[] STRINGS = {"apple", "obey", "incomplete", "mood", "parted"};

    private Function2<String, Integer, Character> getChar;
    private Function2<Object, Object, Boolean> equalClasses;

    @Before
    public void setUp() {
        getChar = String::charAt;
        equalClasses = (o1, o2) -> o1.getClass() == o2.getClass();
    }

    @Test
    public void testApply() {
            for (int i = 0; i < 5; i++) {
                assertThat(getChar.apply(STRINGS[i], i), equalTo((char) ('a' + i)));
            }

            for (int i = 1; i < OBJECTS.length; i++) {
                assertThat(equalClasses.apply(OBJECTS[i], OBJECTS[i - 1]), equalTo((i & 1) != 0));
            }
    }

    @Test
    public void testCompose() {
            @NotNull boolean[] ans = {true, false, false, false, true};

            @NotNull Predicate<Character> isVowel = c -> (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u');
            for (int i = 0; i < 5; i++) {
                assertThat(getChar.compose(isVowel).apply(STRINGS[i], i), equalTo(ans[i]));
            }

            @NotNull
            Function1<Object, String> toLiteral = Object::toString;
            for (int i = 1; i < OBJECTS.length; i++) {
                assertThat(equalClasses.compose(toLiteral).apply(OBJECTS[i], OBJECTS[i - 1]),
                        equalTo(Boolean.toString((i & 1) != 0)));
            }
    }

    @Test
    public void testBind1() {
        String incomplete = STRINGS[2];
        @NotNull Function1<Integer, Character> getCharOfIncomplete = getChar.bind1(incomplete);

        for (int i = 0; i < incomplete.length(); i++) {
            assertThat(getCharOfIncomplete.apply(i), equalTo(incomplete.charAt(i)));
        }

    }

    @Test
    public void testBind2() {
        @NotNull String ice = "lyodt";
        @NotNull Function1<String, Character> getFourthChar = getChar.bind2(3);

        for (int i = 0; i < ice.length(); i++) {
            assertThat(getFourthChar.apply(STRINGS[i]), equalTo(ice.charAt(i)));
        }

    }

    @Test
    public void testCurry() {
        @NotNull String ice = "lyodt";
        @NotNull Function1<String, Character> getFourthChar = getChar.curry(3);

        for (int i = 0; i < ice.length(); i++) {
            assertThat(getFourthChar.apply(STRINGS[i]), equalTo(ice.charAt(i)));
        }
    }

}