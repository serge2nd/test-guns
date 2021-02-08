package ru.serge2nd.test.match;

import org.hamcrest.Matcher;
import ru.serge2nd.test.util.Alert;
import ru.serge2nd.test.match.builder.MatcherBuilder;
import ru.serge2nd.test.util.ToCall;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.Matchers.contains;
import static ru.serge2nd.test.Assist.errNotInstantiable;
import static ru.serge2nd.test.Predicates.not;
import static ru.serge2nd.test.match.ArrayMatch.asList;
import static ru.serge2nd.test.match.CoreMatch.asMatchers;
import static ru.serge2nd.test.match.CoreMatch.equalTo;
import static ru.serge2nd.test.match.CoreMatch.sameAs;
import static ru.serge2nd.test.match.MatchAssist.alertAs;
import static ru.serge2nd.test.match.builder.MatcherBuilder.matcherBuilder;

/**
 * Matchers of items polled from some source.
 */
public class PollMatch {
    private PollMatch() { throw errNotInstantiable(lookup()); }

    //region java.util.Iterator matchers

    public static Matcher<Iterator<?>> hasNext() {
        return new MatcherBuilder<Iterator<?>>(){}.matchIf(Iterator::hasNext).append("next is present").alert("was absent").build();
    }
    public static Matcher<Iterator<?>> noNext() {
        return new MatcherBuilder<Iterator<?>>(){}.matchIf(not(Iterator::hasNext)).append("next is absent").alert("was present").build();
    }

    public static <T> Matcher<Iterator<? extends T>> nextIs(Matcher<? super T> matcher) {
        return new MatcherBuilder<Iterator<? extends T>>(hasNext()){}
                .then(Iterator::next, matcher)
                .build();
    }
    public static <T> Matcher<Iterator<? extends T>> nextIs(T expected)     { return nextIs(equalTo(expected)); }
    public static <T> Matcher<Iterator<? extends T>> nextIsSame(T expected) { return nextIs(sameAs(expected)); }
    //endregion

    //region Supplier matchers

    public static <T> Matcher<Supplier<? extends T>> gives(List<Matcher<? super T>> matchers) {
        ThreadLocalList actuals = new ThreadLocalList(matchers.size());
        return supplierMatcher(
                accumulatingContains(matchers, actuals), matchers,
                alertAs($->actuals.get(), contains(matchers)));
    }
    @SafeVarargs
    public static <T> Matcher<Supplier<? extends T>> gives(Matcher<? super T> first, Matcher<? super T>... other) { return gives(asList(first, other)); }
    @SafeVarargs
    public static <T> Matcher<Supplier<? extends T>> gives(T... expected)     { return gives(asMatchers(CoreMatch::equalTo, expected)); }
    @SafeVarargs
    public static <T> Matcher<Supplier<? extends T>> givesSame(T... expected) { return gives(asMatchers(CoreMatch::sameAs, expected)); }
    //endregion

    //region Throwing supplier matchers

    public static <T> Matcher<ToCall<? extends T>> emits(List<Matcher<? super T>> matchers) {
        ThreadLocalList actuals = new ThreadLocalList(matchers.size());
        return callMatcher(
                accumulatingContains(matchers, actuals), matchers,
                alertAs($->actuals.get(), contains(matchers)));
    }
    @SafeVarargs
    public static <T> Matcher<ToCall<? extends T>> emits(Matcher<? super T> first, Matcher<? super T>... other) { return emits(asList(first, other)); }
    @SafeVarargs
    public static <T> Matcher<ToCall<? extends T>> emits(T... expected)     { return emits(asMatchers(CoreMatch::equalTo, expected)); }
    @SafeVarargs
    public static <T> Matcher<ToCall<? extends T>> emitsSame(T... expected) { return emits(asMatchers(CoreMatch::sameAs, expected)); }
    //endregion

    static <T> Matcher<Supplier<? extends T>> supplierMatcher(Matcher<Iterable<T>> joint,
                                                              List<Matcher<? super T>> matchers,
                                                              Alert<Iterable<T>> alert) {
        return new MatcherBuilder<Supplier<T>>(){}
                .then(s -> ToCall.of(s).asIterable(matchers.size()), joint)
                .blank().describe("gives ", matchers)
                .calm().alert(alert)
                .build();
    }
    static <T> Matcher<ToCall<? extends T>> callMatcher(Matcher<Iterable<T>> joint,
                                                        List<Matcher<? super T>> matchers,
                                                        Alert<Iterable<T>> alert) {
        return new MatcherBuilder<ToCall<T>>(){}
                .then(c -> c.asIterable(matchers.size()), joint)
                .blank().describe("emits ", matchers)
                .calm().alert(alert)
                .build();
    }

    static <T> Matcher<Iterable<T>> accumulatingContains(List<Matcher<? super T>> matchers, ThreadLocalList actuals) {
        List<Matcher<? super T>> accumulatingMatchers = new ArrayList<>(matchers.size());
        for (Matcher<? super T> matcher : matchers)
            accumulatingMatchers.add(matcherBuilder().<T>then(actuals::add, matcher).build());

        Matcher<Iterable<? extends T>> delegate = contains(accumulatingMatchers);
        return matcherBuilder(delegate).first($->actuals.clear()).and($->actuals.clear()).build();
    }

    @SuppressWarnings("unchecked")
    static class ThreadLocalList extends ThreadLocal<List<Object>> {
        ThreadLocalList(int capacity) { set(new ArrayList<>(capacity)); }
        <T>   T add(Object e)         { get().add(e); return (T)e; }
        boolean clear()               { get().clear(); return true; }
    }
}
