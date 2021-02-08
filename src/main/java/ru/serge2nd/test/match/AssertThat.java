package ru.serge2nd.test.match;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.serge2nd.test.util.ToRun;

import java.util.stream.Stream;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;
import static ru.serge2nd.test.Asserting.assertEach;
import static ru.serge2nd.test.Assist.errNotInstantiable;
import static ru.serge2nd.test.Env.EOL;

/**
 * Asserting with multiple actual-matcher pairs + extra.
 */
public class AssertThat {
    private AssertThat() { throw errNotInstantiable(lookup()); }

    /**
     * Equivalent of {@link #assertThat(String, Object, Matcher) assertThat("", actual, matcher)}.
     */
    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
        assertThat("", actual, matcher);
    }
    /**
     * Analogous to {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher) MatcherAssert.assertThat()}
     * but with some minor changes for a bit nicer output.
     */
    public static <T> void assertThat(String title, T actual, Matcher<? super T> matcher) {
        if (!matcher.matches(actual)) {
            Description description = new StringDescription()
                    .appendText(title).appendText(EOL)
                    .appendText("Expected: ").appendDescriptionOf(matcher).appendText(EOL)
                    .appendText("  Actual: ");matcher.describeMismatch(actual, description);

            throw new AssertionError(description.toString());
        }
    }

    /**
     * Equivalent of {@link #assertThat(String, Object, Matcher[]) assertThat("", actual, matchers)}.
     */
    @SafeVarargs
    public static <T> void assertThat(T actual, Matcher<? super T>... matchers) {
        assertThat("", actual, matchers);
    }

    /**
     * Maps multiple matchers to {@link #assertThat(Object, Matcher)} assertions
     * passed to {@link ru.serge2nd.test.Asserting#assertEach(String, Stream) Asserting.assertEach()}.
     */
    @SafeVarargs
    public static <T> void assertThat(String title, T actual, Matcher<? super T>... matchers) {
        assertEach(title, stream(matchers).map(matcher -> () -> assertThat(actual, matcher)));
    }

    //region Multiple actual-matcher pairs + extra

    //region 1 actual-matcher pair
    public static <T> void assertThat(T actual, Matcher<? super T> matcher,
                                      ToRun extra, ToRun... other) {
        assertThat("", actual, matcher, extra, other);
    }
    public static <T> void assertThat(String title, T actual, Matcher<? super T> matcher,
                                      ToRun extra, ToRun... other) {
        assertEach(title, concat(Stream.of(() -> assertThat(actual, matcher), extra), stream(other)));
    }
    //endregion

    //region 2 actual-matcher pairs
    public static <T, U> void assertThat(T actual1, Matcher<? super T> matcher1,
                                         U actual2, Matcher<? super U> matcher2,
                                         ToRun... extra) {
        assertThat("", actual1, matcher1, actual2, matcher2, extra);
    }
    public static <T, U> void assertThat(String title,
                                         T actual1, Matcher<? super T> matcher1,
                                         U actual2, Matcher<? super U> matcher2,
                                         ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher1),
                () -> assertThat(actual2, matcher2)), stream(extra)));
    }
    //endregion

    //region 3 actual-matcher pairs
    public static <T, U, V> void assertThat(T actual1, Matcher<? super T> matcher1,
                                            U actual2, Matcher<? super U> matcher2,
                                            V actual3, Matcher<? super V> matcher3,
                                            ToRun... extra) {
        assertThat("", actual1, matcher1, actual2, matcher2, actual3, matcher3, extra);
    }
    public static <T, U, V> void assertThat(String title,
                                            T actual1, Matcher<? super T> matcher1,
                                            U actual2, Matcher<? super U> matcher2,
                                            V actual3, Matcher<? super V> matcher3,
                                            ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher1),
                () -> assertThat(actual2, matcher2),
                () -> assertThat(actual3, matcher3)), stream(extra)));
    }
    //endregion

    //region 4 actual-matcher pairs
    public static <T, U, V, W> void assertThat(T actual1, Matcher<? super T> matcher1,
                                               U actual2, Matcher<? super U> matcher2,
                                               V actual3, Matcher<? super V> matcher3,
                                               W actual4, Matcher<? super W> matcher4,
                                               ToRun... extra) {
        assertThat("", actual1, matcher1, actual2, matcher2, actual3, matcher3, actual4, matcher4, extra);
    }
    public static <T, U, V, W> void assertThat(String title,
                                               T actual1, Matcher<? super T> matcher1,
                                               U actual2, Matcher<? super U> matcher2,
                                               V actual3, Matcher<? super V> matcher3,
                                               W actual4, Matcher<? super W> matcher4,
                                               ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher1),
                () -> assertThat(actual2, matcher2),
                () -> assertThat(actual3, matcher3),
                () -> assertThat(actual4, matcher4)), stream(extra)));
    }
    //endregion

    //region 5 actual-matcher pairs
    public static <T, U, V, W, X> void assertThat(T actual1, Matcher<? super T> matcher1,
                                                  U actual2, Matcher<? super U> matcher2,
                                                  V actual3, Matcher<? super V> matcher3,
                                                  W actual4, Matcher<? super W> matcher4,
                                                  X actual5, Matcher<? super X> matcher5,
                                                  ToRun... extra) {
        assertThat("", actual1, matcher1, actual2, matcher2, actual3, matcher3, actual4, matcher4, actual5, matcher5, extra);
    }
    public static <T, U, V, W, X> void assertThat(String title,
                                                  T actual1, Matcher<? super T> matcher1,
                                                  U actual2, Matcher<? super U> matcher2,
                                                  V actual3, Matcher<? super V> matcher3,
                                                  W actual4, Matcher<? super W> matcher4,
                                                  X actual5, Matcher<? super X> matcher5,
                                                  ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher1),
                () -> assertThat(actual2, matcher2),
                () -> assertThat(actual3, matcher3),
                () -> assertThat(actual4, matcher4),
                () -> assertThat(actual5, matcher5)), stream(extra)));
    }
    //endregion

    //region 6 actual-matcher pairs
    public static <T, U, V, W, X, Y> void assertThat(T actual1, Matcher<? super T> matcher1,
                                                     U actual2, Matcher<? super U> matcher2,
                                                     V actual3, Matcher<? super V> matcher3,
                                                     W actual4, Matcher<? super W> matcher4,
                                                     X actual5, Matcher<? super X> matcher5,
                                                     Y actual6, Matcher<? super Y> matcher6,
                                                     ToRun... extra) {
        assertThat("", actual1, matcher1, actual2, matcher2, actual3, matcher3, actual4, matcher4, actual5, matcher5, actual6, matcher6, extra);
    }
    public static <T, U, V, W, X, Y> void assertThat(String title,
                                                     T actual1, Matcher<? super T> matcher1,
                                                     U actual2, Matcher<? super U> matcher2,
                                                     V actual3, Matcher<? super V> matcher3,
                                                     W actual4, Matcher<? super W> matcher4,
                                                     X actual5, Matcher<? super X> matcher5,
                                                     Y actual6, Matcher<? super Y> matcher6,
                                                     ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher1),
                () -> assertThat(actual2, matcher2),
                () -> assertThat(actual3, matcher3),
                () -> assertThat(actual4, matcher4),
                () -> assertThat(actual5, matcher5),
                () -> assertThat(actual6, matcher6)), stream(extra)));
    }
    //endregion

    //region 7 actual-matcher pairs
    public static <T, U, V, W, X, Y, Z> void assertThat(T actual1, Matcher<? super T> matcher1,
                                                        U actual2, Matcher<? super U> matcher2,
                                                        V actual3, Matcher<? super V> matcher3,
                                                        W actual4, Matcher<? super W> matcher4,
                                                        X actual5, Matcher<? super X> matcher5,
                                                        Y actual6, Matcher<? super Y> matcher6,
                                                        Z actual7, Matcher<? super Z> matcher7,
                                                        ToRun... extra) {
        assertThat("", actual1, matcher1, actual2, matcher2, actual3, matcher3, actual4, matcher4, actual5, matcher5, actual6, matcher6, actual7, matcher7, extra);
    }
    public static <T, U, V, W, X, Y, Z> void assertThat(String title,
                                                        T actual1, Matcher<? super T> matcher1,
                                                        U actual2, Matcher<? super U> matcher2,
                                                        V actual3, Matcher<? super V> matcher3,
                                                        W actual4, Matcher<? super W> matcher4,
                                                        X actual5, Matcher<? super X> matcher5,
                                                        Y actual6, Matcher<? super Y> matcher6,
                                                        Z actual7, Matcher<? super Z> matcher7,
                                                        ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher1),
                () -> assertThat(actual2, matcher2),
                () -> assertThat(actual3, matcher3),
                () -> assertThat(actual4, matcher4),
                () -> assertThat(actual5, matcher5),
                () -> assertThat(actual6, matcher6),
                () -> assertThat(actual7, matcher7)), stream(extra)));
    }
    //endregion

    //region 8 actual-matcher pairs
    public static <T, U, V, W, X, Y, Z, A> void assertThat(T actual1, Matcher<? super T> matcher1,
                                                           U actual2, Matcher<? super U> matcher2,
                                                           V actual3, Matcher<? super V> matcher3,
                                                           W actual4, Matcher<? super W> matcher4,
                                                           X actual5, Matcher<? super X> matcher5,
                                                           Y actual6, Matcher<? super Y> matcher6,
                                                           Z actual7, Matcher<? super Z> matcher7,
                                                           A actual8, Matcher<? super A> matcher8,
                                                           ToRun... extra) {
        assertThat("", actual1, matcher1, actual2, matcher2, actual3, matcher3, actual4, matcher4, actual5, matcher5, actual6, matcher6, actual7, matcher7, actual8, matcher8, extra);
    }
    public static <T, U, V, W, X, Y, Z, A> void assertThat(String title,
                                                           T actual1, Matcher<? super T> matcher1,
                                                           U actual2, Matcher<? super U> matcher2,
                                                           V actual3, Matcher<? super V> matcher3,
                                                           W actual4, Matcher<? super W> matcher4,
                                                           X actual5, Matcher<? super X> matcher5,
                                                           Y actual6, Matcher<? super Y> matcher6,
                                                           Z actual7, Matcher<? super Z> matcher7,
                                                           A actual8, Matcher<? super A> matcher8,
                                                           ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher1),
                () -> assertThat(actual2, matcher2),
                () -> assertThat(actual3, matcher3),
                () -> assertThat(actual4, matcher4),
                () -> assertThat(actual5, matcher5),
                () -> assertThat(actual6, matcher6),
                () -> assertThat(actual7, matcher7),
                () -> assertThat(actual8, matcher8)), stream(extra)));
    }
    //endregion
    //endregion
}
