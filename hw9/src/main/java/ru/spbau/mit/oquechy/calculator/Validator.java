package ru.spbau.mit.oquechy.calculator;

import org.jetbrains.annotations.NotNull;

/**
 * Checks correctness of sequence of tokens.
 */
public class Validator {

    /**
     * Runs validation. Sequence is correct if it contains
     * equal numbers of open and closed brackets and has right
     * order of operations, brackets and numbers.
     * @param tokens sequence to be checked
     */
    public static void validate(@NotNull Token[] tokens) {
        validateOrder(tokens);
        validateBrackets(tokens);
    }

    private static void validateBrackets(@NotNull Token[] tokens) {
        int openCnt = 0;
        int closeCnt = 0;

        for (Token token : tokens) {
            openCnt += token == Token.OPEN_BRACKET ? 1 : 0;
            closeCnt += token == Token.CLOSE_BRACKET ? 1 : 0;
        }

        if (openCnt != closeCnt) {
            throw new IllegalArgumentException("Incorrect number of brackets");
        }
    }

    private static void validateOrder(@NotNull Token[] tokens) {
        boolean waitForNum = true;

        for (@NotNull Token token : tokens) {
            if (token != Token.CLOSE_BRACKET && token.isOperation() ^ waitForNum) {
                waitForNum = token != Token.NUMBER;
            } else if (token == Token.CLOSE_BRACKET && !waitForNum){
                waitForNum = false;
            } else {
                throw new IllegalArgumentException("Incorrect order of tokens");
            }
        }
    }
}
