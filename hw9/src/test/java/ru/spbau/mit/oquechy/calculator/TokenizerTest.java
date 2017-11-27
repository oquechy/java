package ru.spbau.mit.oquechy.calculator;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.junit.Assert.*;

public class TokenizerTest {

    private final static String EXPR = "(  1+ 165) *0.15-7.09\n";
    private final static String BRACKETS = "()(";

    @Test
    public void testTokenize() {
        @NotNull Token[] tokens = Tokenizer.tokenize(EXPR);

        assertThat(tokens, both(arrayWithSize(9)).and(arrayContaining(
                Token.OPEN_BRACKET,
                Token.NUMBER,
                Token.ADDITION,
                Token.NUMBER,
                Token.CLOSE_BRACKET,
                Token.MULTIPLICATION,
                Token.NUMBER,
                Token.SUBTRACTION,
                Token.NUMBER
        )));

        assertThat(tokens[1].getValue(), is(1.0));
        assertThat(tokens[3].getValue(), is(165.0));
        assertThat(tokens[6].getValue(), is(0.15));
        assertThat(tokens[8].getValue(), is(7.09));
    }

    @Test
    public void testTokenizeBrackets() {
        @NotNull Token[] tokens = Tokenizer.tokenize(EXPR);

        assertThat(tokens, both(arrayWithSize(9)).and(arrayContaining(
                Token.OPEN_BRACKET,
                Token.NUMBER,
                Token.ADDITION,
                Token.NUMBER,
                Token.CLOSE_BRACKET,
                Token.MULTIPLICATION,
                Token.NUMBER,
                Token.SUBTRACTION,
                Token.NUMBER
        )));

        assertThat(tokens[1].getValue(), is(1.0));
        assertThat(tokens[3].getValue(), is(165.0));
        assertThat(tokens[6].getValue(), is(0.15));
        assertThat(tokens[8].getValue(), is(7.09));
    }

}