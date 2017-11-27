package ru.spbau.mit.oquechy;


import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.oquechy.calculator.Calculator;
import ru.spbau.mit.oquechy.stack.ArrayStack;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Console application for reading and calculating expressions line by line.
 */
public class Main {
    public static void main(String[] args) {
        try (@NotNull PrintWriter out = new PrintWriter(System.out);
             @NotNull Scanner in = new Scanner(System.in)) {

            @NotNull Calculator calculator = new Calculator(new ArrayStack<>(), new ArrayStack<>());

            while (in.hasNext()) {
                try {
                    String expression = in.nextLine();
                    double result = calculator.evaluate(expression);
                    out.println(result);
                    out.flush();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }
}
