package ru.serge2nd.test.match;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import ru.serge2nd.test.Assist;
import ru.serge2nd.test.match.builder.MatcherBuilder;
import ru.serge2nd.test.util.ToRun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.asList;
import static java.util.Objects.deepEquals;
import static java.util.Objects.requireNonNull;
import static ru.serge2nd.test.Assist.describe;
import static ru.serge2nd.test.Assist.errNotInstantiable;
import static ru.serge2nd.test.Assist.has;
import static ru.serge2nd.test.Assist.hasExact;
import static ru.serge2nd.test.match.builder.MatcherBuilder.matcherBuilder;

/**
 * Matchers for some common cases.
 */
public class CoreMatch {
    private CoreMatch() { throw errNotInstantiable(lookup()); }

    //region Plain instance matchers

    /**
     * Same as {@link Matchers#equalTo(Object)} except that {@code equals()} is called on the <b>expected</b> object if not null.
     */
    public static <T> Matcher<T> equalTo(T expected) {
        return matcherBuilder().matchIf(actual -> deepEquals(expected, actual)).append(expected).alert().build();
    }
    public static <T> Matcher<T> sameAs(T expected) {
        return matcherBuilder().matchIf(actual -> expected == actual).appendId(expected).alertId().build();
    }

    public static Matcher<Object> notNullRef(String label) {
        return matcherBuilder().matchIf(Objects::nonNull).append(label).alert().build();
    }
    public static Matcher<Object> nullRef(String label) {
        return matcherBuilder().matchIf(Objects::isNull).append(label).alert().build();
    }

    public static <T> Matcher<T> sameClass(Class<? extends T> cls) {
        return matcherBuilder(cls)
                .matchIf(actual -> actual.getClass() == cls)
                .append(() ->
                    "exactly a " + cls.getName())
                .alert(actual ->
                    "present a " + actual.getClass().getName())
                .build();
    }
    @SuppressWarnings("unchecked")
    public static <T, U extends T> Matcher<T> sameClass(U expected) {
        return sameClass((Class<U>)requireNonNull(expected, "<null> has no class").getClass());
    }

    public static <T> Matcher<T> not(Matcher<T> matcher) {
        return matcherBuilder(matcher).reject().append("not ", 0).build();
    }
    //endregion

    //region java.util.Optional matchers

    public static Matcher<Optional<?>> presented() {
        return new MatcherBuilder<Optional<?>>(){}
                .matchIf(Optional::isPresent)
                .append("presented").alert()
                .build();
    }
    public static <T> Matcher<Optional<? extends T>> presented(Matcher<? super T> matcher) {
        return new MatcherBuilder<Optional<? extends T>>(){}
                .matchIf(Optional::isPresent)
                .then(Optional::get, matcher)
                .build();
    }
    public static <T> Matcher<Optional<? extends T>> presented(T actual)     { return presented(equalTo(actual)); }
    public static <T> Matcher<Optional<? extends T>> presentedSame(T actual) { return presented(sameAs(actual)); }
    public static     Matcher<Optional<?>>           absent()                { return not(presented()); }
    //endregion

    //region java.lang.Throwable matchers

    @SuppressWarnings("Convert2MethodRef")
    public static Matcher<Throwable> thrown(Class<? extends Throwable> expected, String expectedMsg) {
        return MatcherBuilder.<Throwable>create()
                .matchIf(t -> has(t, expected, expectedMsg))
                .append(describe(expected, expectedMsg))
                .alert(t -> describe(t))
                .build();
    }
    @SuppressWarnings("Convert2MethodRef")
    public static Matcher<Throwable> thrownExact(Class<? extends Throwable> expected, String expectedMsg) {
        return MatcherBuilder.<Throwable>create()
                .matchIf(t -> hasExact(t, expected, expectedMsg))
                .append(
                    "exactly ").append(describe(expected, expectedMsg))
                .alert(
                    "present ").alert(t -> describe(t))
                .build();
    }
    //endregion

    //region Fail matchers

    public static                       Matcher<ToRun> noFail()                      { return not(fails(Throwable.class)); }
    public static                       Matcher<ToRun> illegalArgument()             { return fails(IllegalArgumentException.class); }
    public static                       Matcher<ToRun> illegalState()                { return fails(IllegalStateException.class); }
    public static                       Matcher<ToRun> noSuchElement()               { return fails(NoSuchElementException.class); }
    public static                       Matcher<ToRun> unsupported()                 { return fails(UnsupportedOperationException.class); }
    public static <T extends Throwable> Matcher<ToRun> fails(Class<T> expected)      { return fails(expected, null); }
    public static <T extends Throwable> Matcher<ToRun> failsExact(Class<T> expected) { return failsExact(expected, null); }

    public static <T extends Throwable> Matcher<ToRun> fails(Class<T> expected, String expectedMsg) {
        return new MatcherBuilder<ToRun>(){}
                .then(Assist::catchThrowable, thrown(expected, expectedMsg))
                .build();
    }
    public static <T extends Throwable> Matcher<ToRun> failsExact(Class<T> expected, String expectedMsg) {
        return new MatcherBuilder<ToRun>(){}
                .then(Assist::catchThrowable, thrownExact(expected, expectedMsg))
                .build();
    }
    public static Matcher<ToRun> fails(Throwable expected) {
        return new MatcherBuilder<ToRun>(){}
                .then(Assist::catchThrowable, sameAs(expected))
                .build();
    }
    //endregion

    @SuppressWarnings("unchecked")
    public static <T> List<Matcher<? super T>> asMatchers(Function<? super T, ? extends Matcher<? super T>> f, T... expected) {
        Matcher<? super T>[] result = new Matcher[expected.length];
        for (int i = 0; i < expected.length; i++) result[i] = f.apply(expected[i]);
        return asList(result);
    }
}
