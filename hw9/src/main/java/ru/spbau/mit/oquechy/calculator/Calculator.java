package ru.spbau.mit.oquechy.calculator;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.oquechy.stack.Stack;

import static ru.spbau.mit.oquechy.calculator.Token.NUMBER;


/**
 * Implementation of calculator which uses
 * postfix notation to compute result
 */
public class Calculator {
    private Stack<Token> operations;
    private Stack<Double> numbers;

    /**
     * Initializes calculator with stacks for storing numbers and operations
     * @param operations stack to store operations
     * @param numbers stack to store numbers
     */
    public Calculator(Stack<Double> numbers, Stack<Token> operations) {
        this.operations = operations;
        this.numbers = numbers;
    }

    /**
     * Runs validation and evaluation of expression using postfix notation.
     * Expression can contain:
     *  - numbers of type double, <br>
     *  - operations: *, /, +, -, <br>
     *  - brackets: (, ) <br>
     *  - whitespaces <br>
     * @param expression expression to be parsed and evaluated
     * @return result of computations
     */
    public double evaluate(String expression) {
        operations.clear();
        numbers.clear();
        NUMBER.reset();

        @NotNull Token[] tokens = Tokenizer.tokenize("(" + expression + ")");
        Validator.validate(tokens);
        compute(tokens);

        if (numbers.size() > 1) {
            throw new RuntimeException("Computation was incorrect. Stack is not empty");
        }

        return numbers.peek() == null ? 0 : numbers.peek();
    }

    private void compute(@NotNull Token[] tokens) {
        for (@NotNull Token t : tokens) {
            switch (t) {
                case OPEN_BRACKET:
                    operations.push(t);
                    break;
                case CLOSE_BRACKET:
                    while (!operations.isEmpty() && operations.peek() != Token.OPEN_BRACKET) {
                        apply(operations.pop(), numbers.pop(), numbers.pop());
                    }
                    operations.pop();
                    break;
                case NUMBER:
                    numbers.push(t.getValue());
                    break;
                default:
                    if (!t.isOperation()) {
                        throw new UnsupportedOperationException("Unknown token");
                    }

                    while (!operations.isEmpty()
                            && operations.peek().isOperation()
                            && t.getPriority() <= operations.peek().getPriority()) {
                        apply(operations.pop(), numbers.pop(), numbers.pop());
                    }
                    operations.push(t);
            }
        }
    }

    private void apply(@NotNull Token operation, Double r, Double l) {
            numbers.push(operation.apply(l, r));
    }
}
