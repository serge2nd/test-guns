package ru.serge2nd.test;

import java.util.function.Predicate;

import static java.lang.invoke.MethodHandles.lookup;
import static ru.serge2nd.test.Assist.cast;
import static ru.serge2nd.test.Assist.errNotInstantiable;

public class Predicates {
    private Predicates() { throw errNotInstantiable(lookup()); }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> not(Predicate<? super T> p) { return (Predicate<T>)p.negate(); }
    public static <T> Predicate<T> anyone()                    { return $ -> true; }

    @SafeVarargs
    public static <T> Predicate<T> all(Predicate<? super T>... predicates) {
        if (predicates.length == 0) return anyone();
        Predicate<T> p = cast(predicates[0]);
        for (int i = 1; i < predicates.length; i++)
            p = p.and(predicates[i]);
        return p;
    }
}
