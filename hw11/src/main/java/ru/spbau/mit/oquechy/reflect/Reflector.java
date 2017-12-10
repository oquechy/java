package ru.spbau.mit.oquechy.reflect;


import com.google.common.base.Charsets;
import com.google.common.collect.Streams;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
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

public class Reflector {

    private static final Collector<CharSequence, ?, String> genericParameters = Collectors.joining(
            ",",
            "<",
            ">"
    );

    public static void printStructure(Class<?> someClass) throws IOException, FormatterException {
        StringBuilder lines = new StringBuilder();

        printPackage(someClass, lines);
        printImports(someClass, lines);
        printClass(someClass, lines);
        writeResult(someClass.getSimpleName() + ".java", lines);
    }

    public static boolean diffClasses(Class<?> classA, Class<?> classB, PrintStream writer) {
        return setminusFields(classA, classB, writer, "+ ") |
                setminusFields(classB, classA, writer, "- ") |
                setminusMethods(classA, classB, writer, "+ ") |
                setminusMethods(classB, classA, writer, "- ");
    }

    private static boolean setminusMethods(Class<?> classA, Class<?> classB, PrintStream writer, String prefix) {
        Function<Method, String> methodToString = f -> getActualGenericType(f.getType(), f.getGenericType()) + ' ' + f.getName();

        Set<String> fieldsB = Arrays.stream(classB.getDeclaredFields())
                .map(methodToString)
                .collect(Collectors.toCollection(HashSet::new));

        return Arrays.stream(classA.getDeclaredFields())
                .map(methodToString)
                .filter(f -> !fieldsB.contains(f))
                .peek(f -> writer.println(prefix + f)).count() > 0;
    }

    private static boolean setminusFields(Class<?> classA, Class<?> classB, PrintStream writer, String prefix) {
        Function<Field, String> fieldToString = f -> getModifiers(f.getModifiers()) + getActualGenericType(f.getType(), f.getGenericType()) + ' ' + f.getName();

        Set<String> fieldsB = Arrays.stream(classB.getDeclaredFields())
                .map(fieldToString)
                .collect(Collectors.toCollection(HashSet::new));

        return Arrays.stream(classA.getDeclaredFields())
                .map(fieldToString)
                .filter(f -> !fieldsB.contains(f))
                .peek(f -> writer.println(prefix + f)).count() > 0;
    }

    private static void printClass(Class<?> someClass, StringBuilder lines) {
        printHeader(someClass, lines);

        printFields(someClass, lines);

        printConstructors(someClass, lines);

        printMethods(someClass, lines);

        for (Class<?> inner : someClass.getDeclaredClasses()) {
            printClass(inner, lines);
        }

        printFooter(lines);
    }

    private static void printFooter(StringBuilder lines) {
        lines.append('}');
    }

     private static void  printMethods(Class<?> someClass, StringBuilder lines) {
        for (Method method : someClass.getDeclaredMethods()) {
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

    private static String getForeignGenericTypes(Executable method) {
        Pattern pattern = Pattern.compile("(<.*?>)");
        String genericString = method.toGenericString();
        System.out.println(genericString);
        Matcher matcher = pattern.matcher(genericString.substring(0, genericString.indexOf('(')));
        return matcher.find() ? matcher.group(1) : "";
    }

    private static String getGenericReturnType(Method method) {
        return getActualGenericType(method.getReturnType(), method.getGenericReturnType());
    }

    private static void printConstructors(Class<?> someClass, StringBuilder lines) {
        for (Constructor<?> constructor : someClass.getDeclaredConstructors()) {
            appendModifiers(constructor.getModifiers(), lines)
                    .append(getForeignGenericTypes(constructor))
                    .append(someClass.getSimpleName())
                    .append(getParamList(constructor))
                    .append("{}");
        }
    }

    private static void printFields(Class<?> someClass, StringBuilder lines) {
        for (Field field : someClass.getDeclaredFields()) {
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

    private static String getDefaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return "null";
        }

        if (type == boolean.class) {
            return "false";
        }

        return "0";
    }

    private static String getGenericFieldType(Field field) {
        return getActualGenericType(field.getType(), field.getGenericType());
    }

    private static void printHeader(Class<?> someClass, StringBuilder lines) {
        appendModifiers(someClass.getModifiers(), lines)
                .append(someClass.isInterface() ? "" : "class").append(' ')
                .append(getGenericClassName(someClass)).append(' ')
                .append(getExtended(someClass))
                .append(getImplemented(someClass))
                .append('{');
    }

    private static StringBuilder appendModifiers(int modifiers, StringBuilder lines) {
        return lines.append(getModifiers(modifiers)).append(' ');
    }

    private static String getModifiers(int modifiers) {
        String s = Modifier.toString(modifiers);
        return s;
    }

    private static String getGenericClassName(Class<?> someClass) {
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

    private static String getBody(Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType == void.class) {
            return "";
        }

        return returnWrapper(getDefaultValue(returnType));
    }

    private static String returnWrapper(String value) {
        return "return " + value + ";";
    }

    private static String getParamList(Executable executable) {

        return Streams.zip(
                Arrays.stream(executable.getParameters()),
                Arrays.stream(executable.getGenericParameterTypes()),
                (p, g) -> getModifiers(p.getModifiers()) + getActualGenericType(p.getType(), g) + ' ' + p.getName()
        ).collect(Collectors.joining(",", "(", ")"));
    }

    private static String getActualGenericType(Class<?> p, Type g) {
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

    private static void writeResult(String name, StringBuilder lines) throws IOException, FormatterException {
        Path out = Paths.get(name);

        String input = lines.toString();
        System.out.println("input = " + input);
        String formatted = new Formatter().formatSourceAndFixImports(input);
        Files.write(out, formatted.getBytes(Charsets.UTF_8));
    }

    private static void printImports(Class<?> someClass, StringBuilder lines) {
        Set<String> imports = new HashSet<>();
        collectImports(someClass, imports);

        for (String packageLine : imports) {
            lines.append("import ").append(packageLine).append(';');
        }
    }

    private static void printPackage(Class<?> someClass, StringBuilder lines) {
        lines.append("package ").append(someClass.getPackage().getName()).append(";");
    }

    public static void collectImports(Class<?> someClass, Set<String> imports) {
        collectInheritedImports(someClass, imports);
        collectFieldImports(someClass, imports);
        collectMethodImports(someClass, imports);
        collectConstructorImports(someClass, imports);
        collectInnerImports(someClass, imports);
    }

    private static void collectInheritedImports(Class<?> someClass, Set<String> imports) {
        if (someClass.getSuperclass() != null) {
            addTypeToImports(imports, someClass.getSuperclass());
        }

        for (Class<?> superclass : someClass.getInterfaces()) {
            addTypeToImports(imports, superclass);
        }
    }

    private static void collectInnerImports(Class<?> someClass, Set<String> imports) {
        for (Class<?> inner : someClass.getDeclaredClasses()) {
            collectImports(inner, imports);
        }
    }

    private static void collectConstructorImports(Class<?> someClass, Set<String> imports) {
        for (Constructor constructor : someClass.getDeclaredConstructors()) {
            for (Class<?> parameter : constructor.getParameterTypes()) {
                addTypeToImports(imports, parameter);
            }
        }
    }

    private static void collectMethodImports(Class<?> someClass, Set<String> imports) {
        for (Method method : someClass.getDeclaredMethods()) {
            for (Class<?> parameter : method.getParameterTypes()) {
                addTypeToImports(imports, parameter);
            }

            addTypeToImports(imports, method.getReturnType());
        }
    }

    private static void addTypeToImports(Set<String> imports, Class<?> type) {
        if (!type.isPrimitive()) {
            imports.add(getPackageName(type));
        }
    }

    private static String getPackageName(Class<?> parameter) {
        return parameter.getCanonicalName();
    }

    public static void collectFieldImports(Class<?> someClass, Set<String> imports) {
        for (Field field : someClass.getDeclaredFields()) {
            addTypeToImports(imports, field.getType());
        }
    }

    public static String getExtended(Class<?> someClass) {
        Type genericSuperclass = someClass.getGenericSuperclass();
        Class<?> superclass = someClass.getSuperclass();
        return superclass == null ? "" : "extends " + getActualGenericType(superclass, genericSuperclass) + " ";
    }

    public static String getImplemented(Class<?> someClass) {
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
