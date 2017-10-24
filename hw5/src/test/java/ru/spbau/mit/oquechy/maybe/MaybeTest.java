package ru.spbau.mit.oquechy.maybe;

import org.junit.Assert;
import org.junit.Test;

public class MaybeTest {

    @Test
    public void testJust() {
        Maybe<Integer> integerMaybe = Maybe.just(1);
        Maybe<String> stringMaybe = Maybe.just("");
        Maybe<Exception> exceptionMaybe = Maybe.just(null);
    }

    @Test
    public void testNothing() throws AccessToNothingException {
        Maybe<Integer> integerMaybe = Maybe.nothing();
        Maybe<String> stringMaybe = Maybe.nothing();
        Maybe<Exception> exceptionMaybe = Maybe.nothing();
    }

    @Test(expected = AccessToNothingException.class)
    public void testGetNothing() throws AccessToNothingException {
        Maybe<Integer> integerMaybe = Maybe.nothing();
        integerMaybe.get();
    }

    @Test
    public void testGet() throws AccessToNothingException {
        Maybe<Integer> integerMaybe = Maybe.just(1);
        Maybe<String> stringMaybe = Maybe.just("");
        Maybe<Exception> exceptionMaybe = Maybe.just(null);

        Assert.assertEquals(new Integer(1), integerMaybe.get());
        Assert.assertEquals("", stringMaybe.get());
        Assert.assertEquals(null, exceptionMaybe.get());
    }

    @Test
    public void testIsPresent() {
        {
            Maybe<Integer> integerMaybe = Maybe.just(1);
            Maybe<String> stringMaybe = Maybe.just("");
            Maybe<Exception> exceptionMaybe = Maybe.just(null);

            Assert.assertTrue(integerMaybe.isPresent());
            Assert.assertTrue(stringMaybe.isPresent());
            Assert.assertTrue(exceptionMaybe.isPresent());
        }

        {
            Maybe<Integer> integerMaybe = Maybe.nothing();
            Maybe<String> stringMaybe = Maybe.nothing();
            Maybe<Exception> exceptionMaybe = Maybe.nothing();

            Assert.assertFalse(integerMaybe.isPresent());
            Assert.assertFalse(stringMaybe.isPresent());
            Assert.assertFalse(exceptionMaybe.isPresent());
        }
    }

    private class InputBase {
    }

    private class InputDerived extends InputBase {
    }

    private class OutputBase {
    }

    private class OutputDerived extends OutputBase {
    }


    @Test
    public void testMap() throws AccessToNothingException {
        Assert.assertEquals(Maybe.just(4), Maybe.just(2).map(x -> x * 2));
        Assert.assertEquals(Maybe.<Integer>nothing(), Maybe.<Integer>nothing().map(x -> x * 2));
        Assert.assertEquals(Maybe.<Integer>nothing(), Maybe.<Integer>nothing().map(x -> x * 2));

        final Maybe<InputBase> justBase = Maybe.just(new InputDerived());
        final Maybe<OutputBase> mappedBase = justBase.map(id -> {
            Assert.assertEquals(InputDerived.class, id.getClass());
            return new OutputDerived();
        });

        Assert.assertEquals(OutputDerived.class, mappedBase.get().getClass());
    }
}