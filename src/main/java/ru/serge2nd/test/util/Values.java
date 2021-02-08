package ru.serge2nd.test.util;

import java.lang.reflect.Array;
import java.util.function.IntFunction;

import static java.lang.invoke.MethodHandles.lookup;
import static ru.serge2nd.test.Assist.errNotInstantiable;

public class Values {
    private Values() { throw errNotInstantiable(lookup()); }

    public static Boolean[]   box(boolean[] a) { return box(a, Boolean[]::new  , Array::getBoolean); }
    public static Character[] box(char[] a)    { return box(a, Character[]::new, Array::getChar); }
    public static Byte[]      box(byte[] a)    { return box(a, Byte[]::new     , Array::getByte); }
    public static Short[]     box(short[] a)   { return box(a, Short[]::new    , Array::getShort); }
    public static Integer[]   box(int[] a)     { return box(a, Integer[]::new  , Array::getInt); }
    public static Long[]      box(long[] a)    { return box(a, Long[]::new     , Array::getLong); }
    public static Float[]     box(float[] a)   { return box(a, Float[]::new    , Array::getFloat); }
    public static Double[]    box(double[] a)  { return box(a, Double[]::new   , Array::getDouble); }

    public static <E> E[] box(Object a, IntFunction<E[]> constructor, ObjIntFunction<Object, E> elemGetter) {
        if (a == null) return null;
        int len = Array.getLength(a);

        E[] boxed = constructor.apply(len);
        for (int i = 0; i < len; i++) boxed[i] = elemGetter.apply(a, i);

        return boxed;
    }

    @FunctionalInterface
    public interface ObjIntFunction<T, R> {
        R apply(T obj, int i);
    }
}
