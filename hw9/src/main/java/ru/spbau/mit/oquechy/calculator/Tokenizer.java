package ru.spbau.mit.oquechy.calculator;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.StringTokenizer;

/**
 * Parses expression and returns tokens.
 */
public class Tokenizer {

    private final static ImmutableMap<String, Token> PARSED =
            ImmutableMap.<String, Token>builder()
                    .put("*", Token.MULTIPLICATION)
                    .put("/", Token.DIVISION)
                    .put("+", Token.ADDITION)
                    .put("-", Token.SUBTRACTION)
                    .put("(", Token.OPEN_BRACKET)
                    .put(")", Token.CLOSE_BRACKET)
                    .build();

    /**
     * Runs parser.
     * @param expression to be parsed
     * @return tokens of expression
     */
    @NotNull
    public static Token[] tokenize(@NotNull String expression) {
        @NotNull StringTokenizer tokenizer = new StringTokenizer(expression.replaceAll("\\s", ""), "+-*/()", true);

        @NotNull Token[] tokens = new Token[tokenizer.countTokens()];

        for (int i = 0; i < tokens.length; i++) {
            String t = tokenizer.nextToken();

            tokens[i] = PARSED.getOrDefault(t, Token.NUMBER);
            tokens[i].setValue(t);
        }

        return tokens;
    }
}
