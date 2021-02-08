package ru.serge2nd.test.match;

import org.hamcrest.Matcher;
import ru.serge2nd.test.util.Alert;
import ru.serge2nd.test.match.builder.MatcherBuilder;
import ru.serge2nd.test.util.Values;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.arraycopy;
import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static ru.serge2nd.test.Assist.errNotInstantiable;
import static ru.serge2nd.test.match.CoreMatch.asMatchers;
import static ru.serge2nd.test.match.builder.MatcherBuilder.matcherBuilder;
import static ru.serge2nd.test.util.Values.box;

/**
 * Matchers of exact array content.
 */
public class ArrayMatch {
    private ArrayMatch() { throw errNotInstantiable(lookup()); }

    //region items()
    public static <T> Matcher<Object> items(List<Matcher<? super T>> matchers) {
        Matcher<T[]> delegate = arrayMatcher(matchers);
        return matcherBuilder(delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static <T> Matcher<Object> items(Matcher<? super T> first, Matcher<? super T>... other) { return items(asList(first, other)); }
    @SafeVarargs
    public static <T> Matcher<Object> items(T... vals) { return items(asMatchers(CoreMatch::equalTo, vals)); }
    //endregion

    //region flags()
    public static Matcher<Object> flags(List<Matcher<? super Boolean>> matchers) {
        Matcher<Boolean[]> delegate = arrayMatcher(matchers);
        return new MatcherBuilder<boolean[]>(){}
                .then(Values::box, delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static Matcher<Object> flags(Matcher<? super Boolean> first, Matcher<? super Boolean>... other) { return flags(asList(first, other)); }
    public static Matcher<Object> flags(boolean... vals)                                                   { return flags(asMatchers(CoreMatch::equalTo, box(vals))); }
    //endregion

    //region chars()
    public static Matcher<Object> chars(List<Matcher<? super Character>> matchers) {
        Matcher<Character[]> delegate = arrayMatcher(matchers);
        return new MatcherBuilder<char[]>(){}
                .then(Values::box, delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static Matcher<Object> chars(Matcher<? super Character> first, Matcher<? super Character>... other) { return chars(asList(first, other)); }
    public static Matcher<Object> chars(char... vals)                                                          { return chars(asMatchers(CoreMatch::equalTo, box(vals))); }
    //endregion

    //region bytes()
    public static Matcher<Object> bytes(List<Matcher<? super Byte>> matchers) {
        Matcher<Byte[]> delegate = arrayMatcher(matchers);
        return new MatcherBuilder<byte[]>(){}
                .then(Values::box, delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static Matcher<Object> bytes(Matcher<? super Byte> first, Matcher<? super Byte>... other) { return bytes(asList(first, other)); }
    public static Matcher<Object> bytes(byte... vals)                                                { return bytes(asMatchers(CoreMatch::equalTo, box(vals))); }
    //endregion

    //region shorts()
    public static Matcher<Object> shorts(List<Matcher<? super Short>> matchers) {
        Matcher<Short[]> delegate = arrayMatcher(matchers);
        return new MatcherBuilder<short[]>(){}
                .then(Values::box, delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static Matcher<Object> shorts(Matcher<? super Short> first, Matcher<? super Short>... other) { return shorts(asList(first, other)); }
    public static Matcher<Object> shorts(short... vals)                                                 { return shorts(asMatchers(CoreMatch::equalTo, box(vals))); }
    //endregion

    //region ints()
    public static Matcher<Object> ints(List<Matcher<? super Integer>> matchers) {
        Matcher<Integer[]> delegate = arrayMatcher(matchers);
        return new MatcherBuilder<int[]>(){}
                .then(Values::box, delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static Matcher<Object> ints(Matcher<? super Integer> first, Matcher<? super Integer>... other) { return ints(asList(first, other)); }
    public static Matcher<Object> ints(int... vals)                                                       { return ints(asMatchers(CoreMatch::equalTo, box(vals))); }
    //endregion

    //region longs()
    public static Matcher<Object> longs(List<Matcher<? super Long>> matchers) {
        Matcher<Long[]> delegate = arrayMatcher(matchers);
        return new MatcherBuilder<long[]>(){}
                .then(Values::box, delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static Matcher<Object> longs(Matcher<? super Long> first, Matcher<? super Long>... other) { return longs(asList(first, other)); }
    public static Matcher<Object> longs(long... vals)                                                { return longs(asMatchers(CoreMatch::equalTo, box(vals))); }
    //endregion

    //region floats()
    public static Matcher<Object> floats(List<Matcher<? super Float>> matchers) {
        Matcher<Float[]> delegate = arrayMatcher(matchers);
        return new MatcherBuilder<float[]>(){}
                .then(Values::box, delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static Matcher<Object> floats(Matcher<? super Float> first, Matcher<? super Float>... other) { return floats(asList(first, other)); }
    public static Matcher<Object> floats(float... vals)                                                 { return floats(asMatchers(CoreMatch::equalTo, box(vals))); }
    //endregion

    //region doubles()
    public static Matcher<Object> doubles(List<Matcher<? super Double>> matchers) {
        Matcher<Double[]> delegate = arrayMatcher(matchers);
        return new MatcherBuilder<double[]>(){}
                .then(Values::box, delegate).calm().alert(detailed(delegate)).build();
    }
    @SafeVarargs
    public static Matcher<Object> doubles(Matcher<? super Double> first, Matcher<? super Double>... other) { return doubles(asList(first, other)); }
    public static Matcher<Object> doubles(double... vals)                                                  { return doubles(asMatchers(CoreMatch::equalTo, box(vals))); }
    //endregion

    static <T> Matcher<T[]> arrayMatcher(List<Matcher<? super T>> matchers) {
        return matchers.size() > 0 ? arrayContaining(matchers) : emptyArray();
    }

    static <T> Alert<T> detailed(Matcher<?> m) { return (v, d) -> {
        d.appendValue(v).appendText(" (");
        m.describeMismatch(v, d);
        d.appendText(")");
    };}

    @SuppressWarnings("unchecked")
    static <T> List<T> asList(T first, T... other) {
        T[] a = (T[])Array.newInstance(other.getClass().getComponentType(), other.length + 1);
        a[0] = first; arraycopy(other, 0, a, 1, other.length);
        return Arrays.asList(a);
    }
}
