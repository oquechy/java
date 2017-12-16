package ru.spbau.mit.oquechy.reflect;


import com.google.common.base.Charsets;
import com.google.common.collect.Streams;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Analyses structure of @code{Class<?>} object.
 */
public class Reflector {

    private static final Collector<CharSequence, ?, String> genericParameters = Collectors.joining(
            ",",
            "<",
            ">"
    );

    /**
     * Prints structure of class to same named file.
     * @param someClass class to analyse
     * @throws IOException if failed to create file
     * @throws FormatterException if failed to generate code
     */
    public static void printStructure(@NotNull Class<?> someClass) throws IOException, FormatterException {
        @NotNull StringBuilder lines = new StringBuilder();

        printPackage(someClass, lines);
        printImports(someClass, lines);
        printClass(someClass, lines);
        writeResult(someClass.getSimpleName() + ".java", lines);
    }

    /**
     * Prints difference of two classes
     * @param classA first class to compare
     * @param classB second class to compare
     * @param writer stream to write to
     * @return true if difference was found
     */
    public static boolean diffClasses(@NotNull Class<?> classA, @NotNull Class<?> classB, @NotNull PrintStream writer) {
        return setMinusFields(classA, classB, writer, "+ ") |
                setMinusFields(classB, classA, writer, "- ") |
                setMinusMethods(classA, classB, writer, "+ ") |
                setMinusMethods(classB, classA, writer, "- ");
    }

    private static boolean setMinusMethods(@NotNull Class<?> classA, @NotNull Class<?> classB, @NotNull PrintStream writer, String prefix) {
        Function<Method, String> methodToString = Method::toGenericString;

        Set<String> methodsB = Arrays.stream(classB.getDeclaredMethods())
                .map(methodToString)
                .collect(Collectors.toCollection(HashSet::new));

        return Arrays.stream(classA.getDeclaredMethods())
                .map(methodToString)
                .filter(f -> !methodsB.contains(f))
                .peek(f -> writer.println(prefix + f)).count() > 0;
    }

    private static boolean setMinusFields(@NotNull Class<?> classA, @NotNull Class<?> classB, @NotNull PrintStream writer, String prefix) {
        @NotNull Function<Field, String> fieldToString = f -> getModifiers(f.getModifiers()) +
                getActualGenericType(f.getType(), f.getGenericType()) + ' ' + f.getName();

        Set<String> fieldsB = Arrays.stream(classB.getDeclaredFields())
                .map(fieldToString)
                .collect(Collectors.toCollection(HashSet::new));

        return Arrays.stream(classA.getDeclaredFields())
                .map(fieldToString)
                .filter(f -> !fieldsB.contains(f))
                .peek(f -> writer.println(prefix + f)).count() > 0;
    }

    private static void printClass(@NotNull Class<?> someClass, @NotNull StringBuilder lines) {
        printHeader(someClass, lines);

        printFields(someClass, lines);

        printConstructors(someClass, lines);

        printMethods(someClass, lines);

        for (@NotNull Class<?> inner : someClass.getDeclaredClasses()) {
            printClass(inner, lines);
        }

        printFooter(lines);
    }

    private static void printFooter(@NotNull StringBuilder lines) {
        lines.append('}');
    }

     private static void  printMethods(@NotNull Class<?> someClass, @NotNull StringBuilder lines) {
        for (@NotNull Method method : someClass.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            appendModifiers(modifiers, lines)
                    .append(getForeignGenericTypes(method))
                    .append(getGenericReturnType(method)).append(' ')
                    .append(method.getName())
                    .append(getParamList(method));
            if (!Modifier.isNative(modifiers) && !Modifier.isAbstract(modifiers)) {
                    lines.append("{").append(getBody(method)).append("}");
            } else {
                lines.append(";");
            }
        }
    }

    @NotNull
    private static String getForeignGenericTypes(@NotNull Executable method) {
        @NotNull Pattern pattern = Pattern.compile("(<.*?>)");
        String genericString = method.toGenericString();
        System.out.println(genericString);
        @NotNull Matcher matcher = pattern.matcher(genericString.substring(0, genericString.indexOf('(')));
        return matcher.find() ? matcher.group(1) : "";
    }

    @NotNull
    private static String getGenericReturnType(@NotNull Method method) {
        return getActualGenericType(method.getReturnType(), method.getGenericReturnType());
    }

    private static void printConstructors(@NotNull Class<?> someClass, @NotNull StringBuilder lines) {
        for (@NotNull Constructor<?> constructor : someClass.getDeclaredConstructors()) {
            appendModifiers(constructor.getModifiers(), lines)
                    .append(getForeignGenericTypes(constructor))
                    .append(someClass.getSimpleName())
                    .append(getParamList(constructor))
                    .append("{}");
        }
    }

    private static void printFields(@NotNull Class<?> someClass, @NotNull StringBuilder lines) {
        for (@NotNull Field field : someClass.getDeclaredFields()) {
            appendModifiers(field.getModifiers(), lines)
                    .append(getGenericFieldType(field))
                    .append(' ')
                    .append(field.getName())
                    .append(Modifier.isFinal(field.getModifiers()) ?
                            "= " + getDefaultValue(field.getType()) :
                            "")
                    .append(';');
        }
    }

    private static String getDefaultValue(@NotNull Class<?> type) {
        if (!type.isPrimitive()) {
            return "null";
        }

        if (type == boolean.class) {
            return "false";
        }

        return "0";
    }

    @NotNull
    private static String getGenericFieldType(@NotNull Field field) {
        return getActualGenericType(field.getType(), field.getGenericType());
    }

    private static void printHeader(@NotNull Class<?> someClass, @NotNull StringBuilder lines) {
        appendModifiers(someClass.getModifiers(), lines)
                .append(someClass.isInterface() ? "" : "class").append(' ')
                .append(getGenericClassName(someClass)).append(' ')
                .append(getExtended(someClass))
                .append(getImplemented(someClass))
                .append('{');
    }

    private static StringBuilder appendModifiers(int modifiers, @NotNull StringBuilder lines) {
        return lines.append(getModifiers(modifiers)).append(' ');
    }

    @NotNull
    private static String getModifiers(int modifiers) {
        @NotNull String s = Modifier.toString(modifiers);
        return s;
    }

    private static String getGenericClassName(@NotNull Class<?> someClass) {
        TypeVariable<? extends Class<?>>[] typeParameters = someClass.getTypeParameters();
        return someClass.getSimpleName() +
                (typeParameters.length > 0 ?
                        Arrays.stream(typeParameters)
                                .map(TypeVariable::getName)
                                .collect(genericParameters) :
                        "");
    }

    public static void main(String[] args) {
    }

    private static String getBody(@NotNull Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType == void.class) {
            return "";
        }

        return returnWrapper(getDefaultValue(returnType));
    }

    private static String returnWrapper(String value) {
        return "return " + value + ";";
    }

    private static String getParamList(@NotNull Executable executable) {

        return Streams.zip(
                Arrays.stream(executable.getParameters()),
                Arrays.stream(executable.getGenericParameterTypes()),
                (p, g) -> getModifiers(p.getModifiers()) + getActualGenericType(p.getType(), g) + ' ' + p.getName()
        ).collect(Collectors.joining(",", "(", ")"));
    }

    @NotNull
    private static String getActualGenericType(@NotNull Class<?> p, Type g) {
        return g instanceof TypeVariableImpl ?
                ((TypeVariableImpl) g).getName() :
                g instanceof ParameterizedTypeImpl ?
                        ((ParameterizedTypeImpl) g).getRawType().getSimpleName() +
                                Arrays.stream(((ParameterizedTypeImpl) g)
                                        .getActualTypeArguments())
                                        .map(Type::getTypeName)
                                        .collect(genericParameters) :
                        p.getSimpleName();
    }

    private static void writeResult(@NotNull String name, @NotNull StringBuilder lines) throws IOException, FormatterException {
        Path out = Paths.get(name);

        @NotNull String input = lines.toString();
        System.out.println("input = " + input);
        String formatted = new Formatter().formatSourceAndFixImports(input);
        Files.write(out, formatted.getBytes(Charsets.UTF_8));
    }

    private static void printImports(@NotNull Class<?> someClass, @NotNull StringBuilder lines) {
        @NotNull Set<String> imports = new HashSet<>();
        collectImports(someClass, imports);

        for (String packageLine : imports) {
            lines.append("import ").append(packageLine).append(';');
        }
    }

    private static void printPackage(@NotNull Class<?> someClass, @NotNull StringBuilder lines) {
        lines.append("package ").append(someClass.getPackage().getName()).append(";");
    }

    /**
     * Collects imported classes to set
     * @param someClass class to inject dependencies from
     * @param imports set to store dependencies
     */
    public static void collectImports(@NotNull Class<?> someClass, @NotNull Set<String> imports) {
        collectInheritedImports(someClass, imports);
        collectFieldImports(someClass, imports);
        collectMethodImports(someClass, imports);
        collectConstructorImports(someClass, imports);
        collectInnerImports(someClass, imports);
    }

    private static void collectInheritedImports(@NotNull Class<?> someClass, @NotNull Set<String> imports) {
        if (someClass.getSuperclass() != null) {
            addTypeToImports(imports, someClass.getSuperclass());
        }

        for (@NotNull Class<?> superclass : someClass.getInterfaces()) {
            addTypeToImports(imports, superclass);
        }
    }

    private static void collectInnerImports(@NotNull Class<?> someClass, @NotNull Set<String> imports) {
        for (@NotNull Class<?> inner : someClass.getDeclaredClasses()) {
            collectImports(inner, imports);
        }
    }

    private static void collectConstructorImports(@NotNull Class<?> someClass, @NotNull Set<String> imports) {
        for (@NotNull Constructor constructor : someClass.getDeclaredConstructors()) {
            for (@NotNull Class<?> parameter : constructor.getParameterTypes()) {
                addTypeToImports(imports, parameter);
            }
        }
    }

    private static void collectMethodImports(@NotNull Class<?> someClass, @NotNull Set<String> imports) {
        for (@NotNull Method method : someClass.getDeclaredMethods()) {
            for (@NotNull Class<?> parameter : method.getParameterTypes()) {
                addTypeToImports(imports, parameter);
            }

            addTypeToImports(imports, method.getReturnType());
        }
    }

    private static void addTypeToImports(@NotNull Set<String> imports, @NotNull Class<?> type) {
        if (!type.isPrimitive()) {
            imports.add(getPackageName(type));
        }
    }

    private static String getPackageName(@NotNull Class<?> parameter) {
        return parameter.getCanonicalName();
    }

    private static void collectFieldImports(@NotNull Class<?> someClass, @NotNull Set<String> imports) {
        for (@NotNull Field field : someClass.getDeclaredFields()) {
            addTypeToImports(imports, field.getType());
        }
    }

    private static String getExtended(@NotNull Class<?> someClass) {
        Type genericSuperclass = someClass.getGenericSuperclass();
        Class<?> superclass = someClass.getSuperclass();
        return superclass == null ? "" : "extends " + getActualGenericType(superclass, genericSuperclass) + " ";
    }

    private static String getImplemented(@NotNull Class<?> someClass) {
        Class<?>[] superclass = someClass.getInterfaces();
        Type[] genericInterfaces = someClass.getGenericInterfaces();
        return superclass.length == 0 ? "" :
                "implements " +
                        Streams.zip(Arrays.stream(superclass),
                                Arrays.stream(genericInterfaces),
                                Reflector::getActualGenericType)
                        .collect(Collectors.joining(", ")) +
                " ";
    }
}
