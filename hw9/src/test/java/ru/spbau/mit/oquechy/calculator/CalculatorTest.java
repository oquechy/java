package ru.spbau.mit.oquechy.calculator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.spbau.mit.oquechy.stack.Stack;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CalculatorTest {
    @Mock
    private Stack<Token> operations;
    @Mock
    private Stack<Double> numbers;

    private Calculator calculator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        calculator = new Calculator(numbers, operations);
    }

    @Test
    public void testIncorrectInput() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.evaluate("(1 + 2+ )3")
            );

        setup();
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.evaluate("1+ 2+3/")
        );

        setup();
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.evaluate(".71+2 v6+3/")
        );

        setup();
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.evaluate(".!!\n\t@")
        );
    }

    @Test
    public void testSimple() {
        @NotNull String expression = "1+2";

        when(operations.pop())
                .thenReturn(
                        Token.ADDITION,
                        Token.OPEN_BRACKET)
                .thenThrow(new RuntimeException("Pop of empty operations stack"));

        when(operations.isEmpty()).thenReturn(false);

        when(operations.peek())
                .thenReturn(
                        Token.OPEN_BRACKET,
                        Token.ADDITION,
                        Token.OPEN_BRACKET)
                .thenThrow(new RuntimeException("Peek of empty operations stack"));

        when(numbers.pop()).thenReturn(2.0, 1.0)
                .thenThrow(new RuntimeException("Extra pop"));

        when(numbers.size()).thenReturn(1);

        when(numbers.peek()).thenReturn(3.0, 3.0);

        assertThat(calculator.evaluate(expression), is(3.0));

        InOrder calculation = inOrder(operations, numbers);

        calculation.verify(operations).clear();
        calculation.verify(numbers).clear();
        calculation.verify(operations).push(Token.OPEN_BRACKET);
        calculation.verify(numbers).push(1.0);
        calculation.verify(operations).push(Token.ADDITION);
        calculation.verify(numbers).push(2.0);
        calculation.verify(operations).pop();
        calculation.verify(numbers, times(2)).pop();
        calculation.verify(numbers).push(3.0);
        calculation.verify(operations).pop();
    }

    @Test
    public void testComplicated() {
        @NotNull String expression = "1+(2.000-3*4)/5-6.05";

        when(numbers.pop()).thenReturn(
                4.0, 3.0,
                12.0, 2.0,
                5.0, -10.0,
                -2.0, 1.0,
                6.05, -1.0)
                .thenThrow(new RuntimeException("Extra pop"));

        when(numbers.peek()).thenReturn(
                -7.05, -7.05)
                .thenThrow(new RuntimeException("Extra pop"));

        when(operations.pop()).thenReturn(
                Token.MULTIPLICATION,
                Token.SUBTRACTION,
                Token.OPEN_BRACKET,
                Token.DIVISION,
                Token.ADDITION,
                Token.SUBTRACTION,
                Token.OPEN_BRACKET
        );

        when(operations.isEmpty()).thenReturn(false);
        when(operations.peek()).thenReturn(
                Token.OPEN_BRACKET,
                Token.OPEN_BRACKET,
                Token.SUBTRACTION,
                Token.SUBTRACTION,
                Token.MULTIPLICATION,
                Token.SUBTRACTION,
                Token.OPEN_BRACKET,
                Token.ADDITION,
                Token.ADDITION,
                Token.DIVISION,
                Token.DIVISION,
                Token.ADDITION,
                Token.ADDITION,
                Token.OPEN_BRACKET,
                Token.SUBTRACTION,
                Token.OPEN_BRACKET
        );

        assertThat(calculator.evaluate(expression), is(-7.05));

        InOrder calculation = inOrder(operations, numbers);

        calculation.verify(operations).push(Token.OPEN_BRACKET);
        calculation.verify(numbers).push(1.0);
        calculation.verify(operations).push(Token.ADDITION);
        calculation.verify(operations).push(Token.OPEN_BRACKET);
        calculation.verify(numbers).push(2.0);
        calculation.verify(operations).push(Token.SUBTRACTION);
        calculation.verify(numbers).push(3.0);
        calculation.verify(operations).push(Token.MULTIPLICATION);
        calculation.verify(numbers).push(4.0);
        calculation.verify(operations).pop();                  // *
        calculation.verify(numbers, times(2)).pop();
        calculation.verify(numbers).push(12.0);
        calculation.verify(operations).pop();                  // -
        calculation.verify(numbers, times(2)).pop();
        calculation.verify(numbers).push(-10.0);
        calculation.verify(operations).pop();                  // (
        calculation.verify(operations).push(Token.DIVISION);
        calculation.verify(numbers).push(5.0);
        calculation.verify(operations).pop();                  // /
        calculation.verify(numbers, times(2)).pop();
        calculation.verify(numbers).push(-2.0);
        calculation.verify(operations).pop();                  // +
        calculation.verify(numbers, times(2)).pop();
        calculation.verify(numbers).push(-1.0);
        calculation.verify(operations).push(Token.SUBTRACTION);
        calculation.verify(operations).pop();                 // -
        calculation.verify(numbers, times(2)).pop();
        calculation.verify(numbers).push(-7.05);
        calculation.verify(operations).pop();
    }
}