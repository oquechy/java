package ru.spbau.mit.oquechy.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Saves and restores state of object with fields of primitive types or Strings.
 */
public class Serialization {

    private static final String[] TYPES = {"byte", "short", "int", "long", "float", "double", "char", "boolean", "String"};

    private static <T> Collector<T, ?, List<T>> toSortedList(Comparator<? super T> comparator) {
        return Collectors.collectingAndThen(
                Collectors.toCollection(ArrayList::new), l -> {
                    l.sort(comparator);
                    return l;
                }
        );
    }

    /**
     * Puts serialized object to out, using reflection.
     * All fields are written to output, regardless of the modifiers.
     * It is assumed that the number of fields of each type fits in int.
     * Values are ordered by the names of fields.
     * @param object object to be serialized
     * @param out output stream
     * @throws IOException when fails with OutputStream
     */
    public static void serialize(@NotNull Object object, OutputStream out) throws IOException {
        try (@NotNull DataOutputStream dataOut = new DataOutputStream(out)) {

            Class<?> type = object.getClass();
            Map<String, List<Field>> fieldsByTypes = getAccessibleFieldsByTypes(type);

            writeFields(object, dataOut, fieldsByTypes);
        }
    }

    private static HashMap<String, List<Field>> getAccessibleFieldsByTypes(@NotNull Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.groupingBy(
                        f -> f.getType().getSimpleName(),
                        HashMap::new,
                        toSortedList(Comparator.comparing(Field::getName)))
                );
    }


    private static void writeFields(Object object, @NotNull DataOutputStream dataOut, @NotNull Map<String, List<Field>> fieldsByTypes) throws IOException {
        for (String primitive : TYPES) {
            List<Field> fields = fieldsByTypes.getOrDefault(primitive, Collections.emptyList());

            dataOut.writeInt(fields.size());
            for (@NotNull Field field : fields) {
                putField(object, field, dataOut);
            }
        }
    }

    /**
     * I thought that a better solution would be to create an array of
     * consumers writing fields to the stream, but I could not come
     * up with a good way to handle exceptions: either they had to be ignored
     * inside lambdas, or I would have to throw runtime, or additional classes
     * should be created. Is there a better way to do this?
     */
    private static void putField(Object object, @NotNull Field field, @NotNull DataOutputStream out) throws IOException {
        try {
            Class<?> type = field.getType();

            if (type == int.class) {
                out.writeInt((int) field.get(object));
            } else if (type == byte.class) {
                out.writeByte((byte) field.get(object));
            } else if (type == short.class) {
                out.writeShort((short) field.get(object));
            } else if (type == float.class) {
                out.writeFloat((float) field.get(object));
            } else if (type == double.class) {
                out.writeDouble((double) field.get(object));
            } else if (type == long.class) {
                out.writeLong((long) field.get(object));
            } else if (type == char.class) {
                out.writeChar((char) field.get(object));
            } else if (type == boolean.class) {
                out.writeBoolean((boolean) field.get(object));
            } else if (type == String.class) {
                out.writeUTF((String) field.get(object));
            } else {
                throw new RuntimeException("Unsupported class " + type + " was found in list of supported ones???");
            }

        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
    }

    /**
     * Restores state of serialized object into new instance of given class.
     * The class must contain the same number of fields of each primitive type and
     * String as the serialized class. Values are written in order of reading.
     * @param in stream to read from
     * @param type class of needed instance
     * @param <T> type of needed instance
     * @return new instance of given class
     * @throws IOException when problems with reading occur
     * @throws IllegalAccessException when fails to access constructor of given class
     * @throws InstantiationException when fails to run constructor of given class
     */
    public static <T> T deserialize(@NotNull InputStream in, @NotNull Class<T> type)
            throws IOException, IllegalAccessException, InstantiationException {

        HashMap<String, List<Field>> fieldsByTypes = getAccessibleFieldsByTypes(type);

        T object = type.newInstance();

        try (@NotNull DataInputStream dataIn = new DataInputStream(in)) {
            setFields(fieldsByTypes, object, dataIn);
        }

        return object;
    }

    private static <T> void setFields(@NotNull HashMap<String, List<Field>> fieldsByTypes, T object, @NotNull DataInputStream dataIn) throws IOException {
        for (String primitive : TYPES) {
            List<Field> fields = fieldsByTypes.getOrDefault(primitive, Collections.emptyList());
            int n = dataIn.readInt();

            if (fields.size() != n) {
                throw new IllegalArgumentException("Wrong number of " + primitive + " fields");
            }

            for (@NotNull Field field : fields) {
                setField(object, field, dataIn);
            }
        }
    }

    private static <T> void setField(T object, @NotNull Field field, @NotNull DataInputStream in) throws IOException {
        try {
            Object value;
            Class<?> type = field.getType();
            if (type == int.class) {
                value = in.readInt();
            } else if (type == byte.class) {
                value = in.readByte();
            } else if (type == short.class) {
                value = in.readShort();
            } else if (type == float.class) {
                value = in.readFloat();
            } else if (type == double.class) {
                value = in.readDouble();
            } else if (type == long.class) {
                value = in.readLong();
            } else if (type == char.class) {
                value = in.readChar();
            } else if (type == boolean.class) {
                value = in.readBoolean();
            } else if (type == String.class) {
                value = in.readUTF();
            } else {
                throw new RuntimeException("Unsupported class " + type + " was found in list of supported ones???");
            }

            field.set(object, value);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
