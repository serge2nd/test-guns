package ru.serge2nd.test;

import ru.serge2nd.test.util.ToRun;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.lang.Integer.toHexString;
import static java.lang.System.identityHashCode;
import static java.lang.invoke.MethodHandles.lookup;

@SuppressWarnings("StringBufferReplaceableByString")
public class Assist {
    private Assist() { throw errNotInstantiable(lookup()); }

    //region Type helpers

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) { return (T)obj; }

    /**
     * Example:
     * <pre>
     *     Object x = new ArrayList&lt;Byte&gt;() <b>{}</b>;
     *     assert (Class)Byte.class == extractTypeArg(x.getClass());
     *
     *     x = new ArrayList&lt;Byte&gt;();
     *     try {
     *         extractTypeArg(x.getClass());
     *         assert false; // (unreached)
     *     } catch (Exception e) {
     *         assert e instanceof IllegalArgumentException;
     *     }
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> extractTypeArg(Class<?> cls) {
        Type t = cls.getGenericSuperclass();

        while (t != Object.class) {
            if (t instanceof ParameterizedType) {
                ParameterizedType p = (ParameterizedType)t;
                Type arg = p.getActualTypeArguments()[0];

                if (arg instanceof Class)
                    return (Class<T>)arg;
                if (arg instanceof ParameterizedType)
                    return (Class<T>)((ParameterizedType)arg).getRawType();

                t = ((Class<?>)p.getRawType()).getGenericSuperclass();
            } else {
                t = ((Class<?>)t).getGenericSuperclass();
            }
        }

        throw new IllegalArgumentException("no concrete type as type argument: " + cls);
    }
    //endregion

    //region Throwable checks

    public static Throwable catchThrowable(ToRun toRun) {
        try {
            toRun._run();
        } catch (Throwable t) {
            return t;
        }
        return null;
    }

    public static boolean has(Throwable t, Class<? extends Throwable> cls) {
        return has(t, cls, null);
    }
    public static boolean has(Throwable t, Class<? extends Throwable> cls, String msg) {
        return cls.isInstance(t) && (msg == null || msg.equals(t.getMessage()));
    }
    public static boolean hasExact(Throwable t, Class<? extends Throwable> cls) {
        return hasExact(t, cls, null);
    }
    public static boolean hasExact(Throwable t, Class<? extends Throwable> cls, String msg) {
        return t != null && t.getClass() == cls && (msg == null || msg.equals(t.getMessage()));
    }
    //endregion

    //region Descriptions

    public static String describe(Throwable t) {
        return t != null ? describe(t.getClass(), t.getMessage()) : "nothing";
    }
    public static String describe(Class<? extends Throwable> t, String msg) {
        return className(t) + (msg != null ? "(" + msg + ")" : "");
    }

    public static String describeIdentity(Object obj) {
        if (obj == null) return "nothing";
        String name = className(obj.getClass());
        int hash = obj.hashCode(), id = identityHashCode(obj);
        String sHash = toHexString(hash), sId = toHexString(id);

        return new StringBuilder(name.length() + sId.length() + sHash.length() + 4)
                .append(name).append('@').append(sId)
                .append(id != hash ? "_" + sHash : "")
                .toString();
    }
    public static String className(Class<?> cls) {
        return !cls.isAnonymousClass() && !cls.isLocalClass() && !cls.isMemberClass()
                && cls.getPackage() == Class.class.getPackage()
                ? cls.getSimpleName() : cls.getName();
    }
    //endregion

    public static UnsupportedOperationException errNotInstantiable(Lookup $) { return new UnsupportedOperationException("non-instantiable: " + $.lookupClass()); }
}
