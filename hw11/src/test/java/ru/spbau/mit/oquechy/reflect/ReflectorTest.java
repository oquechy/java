package ru.spbau.mit.oquechy.reflect;

import com.google.googlejavaformat.java.FormatterException;
import com.sun.org.apache.bcel.internal.classfile.LineNumber;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.openjdk.tools.javac.code.Type;
import sun.misc.Timer;
import sun.swing.LightweightContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.security.KeyStore;
import java.security.Security;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;


public class ReflectorTest {

    @Test
    public void testCollectImports() throws IOException, FormatterException {
        Set<String> imports = new HashSet<>();
        Reflector.collectImports(ImportsExample.class, imports);
        System.out.println(imports);
        assertThat(imports, hasSize(16));
        assertThat(imports, containsInAnyOrder(
                "sun.swing.LightweightContent",
                "sun.misc.Timer",
                "com.sun.org.apache.bcel.internal.classfile.LineNumber",
                "java.security.Security",
                "java.util.ArrayList",
                "java.util.LinkedList",
                "java.util.jar.JarFile",
                "java.util.function.Function",
                "java.util.stream.Stream",
                "java.nio.Buffer",
                "org.junit.rules.Timeout",
                "java.io.BufferedReader",
                "ru.spbau.mit.oquechy.reflect.ImportsExample",
                "org.openjdk.tools.javac.code.Type.UnknownType",
                "java.security.KeyStore.Entry",
                "java.lang.Object"
        ));
        Reflector.printStructure(ImportsExample.class);
    }

    @Test
    public void testModifiers() throws IOException, FormatterException {
        Reflector.printStructure(ModifiersExample.class);
    }

    @Test
    public void testPrintStructure() throws IOException, FormatterException {
        Reflector.printStructure(ImportsExample.class);
    }

    @Test
    public void testReturnValues() throws IOException, FormatterException {
        Reflector.printStructure(ReturnValuesExample.class);
    }

    @Test
    public void testInnerDefinitions() throws IOException, FormatterException {
        Reflector.printStructure(InnerExamples.class);
    }

    @Test
    public void testGenerics() throws IOException, FormatterException {
        Reflector.printStructure(GenericsExample.class);
    }

    @Test
    public void testFinalInitializing() throws IOException, FormatterException {
        Reflector.printStructure(FinalInitializingExample.class);
    }

    @Test
    public void testDiff() throws IOException, FormatterException {
        assertThat(Reflector.diffClasses(GenericsExample.class, GenericsExample.class, System.out),
                Matchers.is(false));
        assertThat(Reflector.diffClasses(GenericsExample.class, ImportsExample.class, System.out),
                Matchers.is(true));
    }

    @Test
    public void main() {
        System.err.println("modifiers: " + Modifier.toString(ModifiersExample.class.getModifiers()));
        System.err.println("toGenericString: " + GenericsExample.class.toGenericString());
    }

}

class ReturnValuesExample {
    void method1() {}
    byte method2() {return 0;}
    short method3() {return 0;}
    int method4() {return 0;}
    long method5() {return 0;}
    float method6() {return 0;}
    char method7() {return 0;}
    boolean method8() {return false;}
    Void method9() {return null;}
}

class FinalInitializingExample {
    final Void defaultVoid = null;
    final byte defaultByte = 0;
    final char defaultChar = 0;
    final short defaultShort = 0;
    final int defaultInt = 0;
    final long defaultLong = 0;
    final float defaultFloat = 0;
    final double defaultDouble = 0;
    final boolean defaultBoolean = false;
}

class ImportsExample extends Type.UnknownType implements KeyStore.Entry {
    ArrayList<Integer> arrayList;
    LinkedList<List<Double>> linkedListOfLists;
    LinkedList linkedList;

    ImportsExample(Function<?, ?> f) {}
    ImportsExample(Function<?, ?> f, Stream<Integer> s) {}
    ImportsExample() {}

    void method1() {}
    void method2(BufferedReader br) {}
    Buffer method3(JarFile jf) { return null; }

    class Inner {
        Security security;

        Inner(Timer timer) {}

        LineNumber getLineNumber() {return null;}
        LightweightContent getLightweightContent(Timeout timeout) {return null;}
    }
}

abstract  class ModifiersExample {
    public final static ArrayList<Integer> arrayList = null;
    private static volatile LinkedList<List<Double>> linkedListOfLists = null;
    protected transient final static LinkedList linkedList = null;

    ModifiersExample(final Function<?, ?> f) {}

    public final static synchronized void method1() {}
    public native static void method2(final BufferedReader br);
    public strictfp static Void method3() { return null; }

    protected abstract static class Inner{
        volatile float f = 0;

        native static synchronized final void method();
    }
}

class InnerExamples {
    class Inner {
        class InnerInner {
            class InnerInnerInner {
            }
        }
    }

    static class Nested {
        static class NestedNested {
            static class NestedNestedNested {

            }
        }
    }

    static interface Interface {
        interface InterfaceInterface {
            static interface InterfaceInterfaceInterface {

            }
        }
    }
}

interface Interface<U, E> {

}

abstract  class  Base<W, E> {

}

class GenericsExample<E, V, T, U> extends Base<E, V> implements Interface<T, U> {
    ArrayList<U> arrayList;
    LinkedList<List<V>> linkedListOfLists;
    LinkedList linkedList;

    <T> GenericsExample(Base<?, ? extends T> f, V t) {}
    <U> GenericsExample(U f, Stream<? super T> s) {}
    <W> GenericsExample() {}

    <W> W method1(W w, U u, ArrayList<? super W> arrayList) {return null;}
    final <K> void method2(K k) {}
    <Q, N> ArrayList<? extends Q> method3(T t1, T t2) { return null; }

    class Inner<F, R, U> implements Interface <T, U> {
        ArrayList<U> arrayList;
        <T> Inner(Base<?, ? extends T> f, V t) {}
        <Q, N> ArrayList<? extends Q> method3(T t1, T t2) { return null; }
    }
}


