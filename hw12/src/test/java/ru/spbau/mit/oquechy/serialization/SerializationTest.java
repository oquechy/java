package ru.spbau.mit.oquechy.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class SerializationTest {

    public static class StringEntry {
        @Nullable private String s1 = null;
        @Nullable private String s2 = null;
        @Nullable private String s3 = null;
        @Nullable private String s4 = null;
        @Nullable private String s5 = null;

        public StringEntry(@Nullable String s1, @Nullable String s2, @Nullable String s3,
                            @Nullable String s4, @Nullable String s5) {
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
            this.s4 = s4;
            this.s5 = s5;
        }

        public StringEntry() { }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            @Nullable StringEntry that = (StringEntry) o;

            return (s1 != null ? s1.equals(that.s1) : that.s1 == null)
                    && (s2 != null ? s2.equals(that.s2) : that.s2 == null)
                    && (s3 != null ? s3.equals(that.s3) : that.s3 == null)
                    && (s4 != null ? s4.equals(that.s4) : that.s4 == null)
                    && (s5 != null ? s5.equals(that.s5) : that.s5 == null);
        }

        @Override
        public int hashCode() {
            int result = s1 != null ? s1.hashCode() : 0;
            result = 31 * result + (s2 != null ? s2.hashCode() : 0);
            result = 31 * result + (s3 != null ? s3.hashCode() : 0);
            result = 31 * result + (s4 != null ? s4.hashCode() : 0);
            result = 31 * result + (s5 != null ? s5.hashCode() : 0);
            return result;
        }
    }

    public static class PrimitiveEntry {

        private int f01 = 0;
        private int f04 = -2;
        private int f15 = 0x1000;
        private int f16 = -0x2000;

        private char f09 = 0;
        private char f02 = 'a';

        private byte f03 = 0;
        private byte f06 = 0x70;

        private boolean f08 = false;
        private boolean f05 = true;

        private float f07 = 0;
        private float f10 = 7000.6f;

        private double f13 = 0;
        private double f12 = -10.0000000000000000001;

        private short f11 = 0;
        private short f14 = -1;

        public PrimitiveEntry(int f1, int f4, int f15, int f16,
                               char f9, char f2,
                               byte f3, byte f6,
                               boolean f8, boolean f5,
                               float f7, float f10,
                               double f13, double f12,
                               short f11, short f14) {
            this.f01 = f1;
            this.f04 = f4;
            this.f15 = f15;
            this.f16 = f16;
            this.f09 = f9;
            this.f02 = f2;
            this.f03 = f3;
            this.f06 = f6;
            this.f08 = f8;
            this.f05 = f5;
            this.f07 = f7;
            this.f10 = f10;
            this.f13 = f13;
            this.f12 = f12;
            this.f11 = f11;
            this.f14 = f14;
        }

        public PrimitiveEntry() { }

        void method() { }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            @Nullable PrimitiveEntry entry = (PrimitiveEntry) o;

            return f01 == entry.f01 && f04 == entry.f04 && f15 == entry.f15 && f16 == entry.f16 && f09 == entry.f09
                    && f02 == entry.f02 && f03 == entry.f03 && f06 == entry.f06 && f08 == entry.f08 && f05 == entry.f05
                    && Float.compare(entry.f07, f07) == 0 && Float.compare(entry.f10, f10) == 0
                    && Double.compare(entry.f13, f13) == 0 && Double.compare(entry.f12, f12) == 0
                    && f11 == entry.f11 && f14 == entry.f14;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = f01;
            result = 31 * result + f04;
            result = 31 * result + f15;
            result = 31 * result + f16;
            result = 31 * result + (int) f09;
            result = 31 * result + (int) f02;
            result = 31 * result + (int) f03;
            result = 31 * result + (int) f06;
            result = 31 * result + (f08 ? 1 : 0);
            result = 31 * result + (f05 ? 1 : 0);
            result = 31 * result + (f07 != +0.0f ? Float.floatToIntBits(f07) : 0);
            result = 31 * result + (f10 != +0.0f ? Float.floatToIntBits(f10) : 0);
            temp = Double.doubleToLongBits(f13);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(f12);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + (int) f11;
            result = 31 * result + (int) f14;
            return result;
        }
    }

    public static class EmptyEntry {
        @Override
        public boolean equals(@Nullable Object o) {
            return  (this == o || o != null && getClass() == o.getClass());
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    @Test
    public void testSerializeEmptyEntry() throws IOException, InstantiationException, IllegalAccessException {
        @NotNull ByteArrayOutputStream source = new ByteArrayOutputStream();

        @NotNull EmptyEntry sourceEntry = new EmptyEntry();
        Serialization.serialize(sourceEntry, source);

        @NotNull ByteArrayInputStream result = new ByteArrayInputStream(source.toByteArray());
        EmptyEntry resultEntry = Serialization.deserialize(result, EmptyEntry.class);

        assertThat(resultEntry, equalTo(sourceEntry));
    }

    @Test
    public void testSerializePrimitiveEntry() throws IOException, InstantiationException, IllegalAccessException {
        @NotNull ByteArrayOutputStream source = new ByteArrayOutputStream();

        @NotNull PrimitiveEntry sourceEntry = new PrimitiveEntry();
        Serialization.serialize(sourceEntry, source);

        @NotNull ByteArrayInputStream result = new ByteArrayInputStream(source.toByteArray());
        PrimitiveEntry resultEntry = Serialization.deserialize(result, PrimitiveEntry.class);

        assertThat(resultEntry, equalTo(sourceEntry));
    }

    @Test
    public void testSerializeStringEntry() throws IOException, InstantiationException, IllegalAccessException {
        @NotNull ByteArrayOutputStream source = new ByteArrayOutputStream();

        @NotNull StringEntry sourceEntry = new StringEntry("lisa", "or", "the", "fox", "?");
        Serialization.serialize(sourceEntry, source);

        @NotNull ByteArrayInputStream result = new ByteArrayInputStream(source.toByteArray());
        StringEntry resultEntry = Serialization.deserialize(result, StringEntry.class);

        assertThat(resultEntry, equalTo(sourceEntry));
    }

    @Test
    public void testSerializeModifiedEntry() throws IOException, InstantiationException, IllegalAccessException {
        @NotNull ByteArrayOutputStream source = new ByteArrayOutputStream();

        @NotNull PrimitiveEntry sourceEntry = new PrimitiveEntry(4, 1, 8, -16, '\1', '0', (byte) -0x1, (byte) 1, true,
                false, -7.7f, 7.7f, -7.7, 7.7, (short) 1, (short) -1);
        Serialization.serialize(sourceEntry, source);

        @NotNull ByteArrayInputStream result = new ByteArrayInputStream(source.toByteArray());
        PrimitiveEntry resultEntry = Serialization.deserialize(result, PrimitiveEntry.class);

        assertThat(resultEntry, equalTo(sourceEntry));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDeserialization() throws IOException, InstantiationException, IllegalAccessException {
        @NotNull ByteArrayOutputStream source = new ByteArrayOutputStream();

        @NotNull PrimitiveEntry sourceEntry = new PrimitiveEntry(4, 1, 8, -16, '\1', '0', (byte) -0x1, (byte) 1, true,
                false, -7.7f, 7.7f, -7.7, 7.7, (short) 1, (short) -1);
        Serialization.serialize(sourceEntry, source);

        @NotNull ByteArrayInputStream result = new ByteArrayInputStream(source.toByteArray());
        EmptyEntry resultEntry = Serialization.deserialize(result, EmptyEntry.class);
    }

}