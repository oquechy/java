package ru.spbau.mit.oquechy.calculator;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testIsOperation() {
        assertThat(Arrays.stream(Token.values()).map(Token::isOperation).collect(Collectors.toList()), contains(
                false,
                false,
                false,
                true,
                true,
                true,
                true
        ));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPriorityOfNotOperationToken() {
        Token.NUMBER.getPriority();
    }

    @Test
    public void testGetPriority() {
        assertThat(Arrays.stream(Token.values()).skip(3).map(Token::getPriority).collect(Collectors.toList()), contains(
                2,
                2,
                1,
                1
        ));
    }

    @Test
    public void testSetGetValue() {
        for (int i = 0; i < 5; i++) {
            Token.NUMBER.setValue((i + 1) + "." + i);
        }

        for (int i = 0; i < 5; i++) {
            assertThat(Token.NUMBER.getValue(), equalTo(i + 1 + .1 * i));
        }
    }

    @Test
    public void testSetValueOfNotNumericToken() {
        Arrays.stream(Token.values()).skip(1).forEach(t -> t.setValue("coco"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueOfNotNumericToken() {
        Token.OPEN_BRACKET.getValue();
    }

    @Test(expected = RuntimeException.class)
    public void testGetValueTooMuchTimes() {
        for (int i = 0; i < 5; i++) {
            Token.NUMBER.setValue((i + 1) + "." + i);
        }

        for (int i = 0; i < 6; i++) {
            assertThat(Token.NUMBER.getValue(), equalTo(i + 1 + .1 * i));
        }
    }

    @Test
    public void testApply() {
        assertThat(Arrays.stream(Token.values()).skip(3).map(t -> t.apply(3., 4.)).collect(Collectors.toList()), contains(
                3. / 4,
                3. * 4,
                3. + 4,
                3. - 4
        ));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyNotOperationToken() {
        Token.CLOSE_BRACKET.apply(2., 0.);
    }
}