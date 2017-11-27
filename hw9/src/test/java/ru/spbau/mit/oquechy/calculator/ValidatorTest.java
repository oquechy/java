package ru.spbau.mit.oquechy.calculator;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    public void testValidate() {
        { // (1)
            @NotNull Token[] tokens = {
                    Token.OPEN_BRACKET,
                    Token.NUMBER,
                    Token.CLOSE_BRACKET
            };
            Validator.validate(tokens);
        }

        { // (1+1)
            @NotNull Token[] tokens = {
                    Token.OPEN_BRACKET,
                    Token.NUMBER,
                    Token.ADDITION,
                    Token.NUMBER,
                    Token.CLOSE_BRACKET
            };
            Validator.validate(tokens);
        }

        { // 1+((2+21) * 0.1 - 0.3) * 0.1 / 0.1
            @NotNull Token[] tokens = {
                    Token.NUMBER,
                    Token.ADDITION,
                    Token.OPEN_BRACKET,
                    Token.OPEN_BRACKET,
                    Token.NUMBER,
                    Token.ADDITION,
                    Token.NUMBER,
                    Token.CLOSE_BRACKET,
                    Token.MULTIPLICATION,
                    Token.NUMBER,
                    Token.SUBTRACTION,
                    Token.NUMBER,
                    Token.CLOSE_BRACKET,
                    Token.MULTIPLICATION,
                    Token.NUMBER,
                    Token.DIVISION,
                    Token.NUMBER
            };
            Validator.validate(tokens);
        }
    }

    @Test
    public void testValidateOfIncorrect() {
        { // (
            @NotNull Token[] tokens = {
                    Token.OPEN_BRACKET,
            };
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Validator.validate(tokens),
                    "Incorrect number of brackets"
            );
        }

        { // (1)(
            @NotNull Token[] tokens = {
                    Token.OPEN_BRACKET,
                    Token.NUMBER,
                    Token.CLOSE_BRACKET,
                    Token.OPEN_BRACKET
            };
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Validator.validate(tokens),
                    "Incorrect number of brackets"
                    );
        }

        { // (1++1)
            @NotNull Token[] tokens = {
                    Token.OPEN_BRACKET,
                    Token.NUMBER,
                    Token.ADDITION,
                    Token.ADDITION,
                    Token.NUMBER,
                    Token.CLOSE_BRACKET
            };
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Validator.validate(tokens),
                    "Incorrect order of tokens"
            );
        }

        { // 1(+1)
            @NotNull Token[] tokens = {
                    Token.NUMBER,
                    Token.OPEN_BRACKET,
                    Token.ADDITION,
                    Token.ADDITION,
                    Token.NUMBER,
                    Token.CLOSE_BRACKET
            };
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Validator.validate(tokens),
                    "Incorrect order of tokens"
            );
        }

        { // 1+((2+21) * 0.1 - 0.3 *) 0.1 / 0.1
            @NotNull Token[] tokens = {
                    Token.NUMBER,
                    Token.ADDITION,
                    Token.OPEN_BRACKET,
                    Token.OPEN_BRACKET,
                    Token.NUMBER,
                    Token.ADDITION,
                    Token.NUMBER,
                    Token.CLOSE_BRACKET,
                    Token.MULTIPLICATION,
                    Token.NUMBER,
                    Token.SUBTRACTION,
                    Token.NUMBER,
                    Token.MULTIPLICATION,
                    Token.CLOSE_BRACKET,
                    Token.NUMBER,
                    Token.DIVISION,
                    Token.NUMBER
            };
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Validator.validate(tokens),
                    "Incorrect number of brackets"
            );
        }
    }
}