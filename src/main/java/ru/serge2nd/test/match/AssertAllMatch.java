package ru.serge2nd.test.match;

import org.hamcrest.Matcher;
import ru.serge2nd.test.util.ToRun;

import java.util.stream.Stream;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;
import static ru.serge2nd.test.Asserting.assertEach;
import static ru.serge2nd.test.Assist.errNotInstantiable;
import static ru.serge2nd.test.match.AssertThat.assertThat;

/**
 * Asserting with multiple actuals + extra.
 */
public class AssertAllMatch {
    private AssertAllMatch() { throw errNotInstantiable(lookup()); }

    //region 2 actuals
    public static <T> void assertAllMatch(Matcher<? super T> matcher, T actual1, T actual2, ToRun... extra) {
        assertAllMatch("", matcher, actual1, actual2, extra);
    }
    public static <T> void assertAllMatch(String title, Matcher<? super T> matcher, T actual1, T actual2, ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher),
                () -> assertThat(actual2, matcher)), stream(extra)));
    }
    //endregion

    //region 3 actuals
    public static <T> void assertAllMatch(Matcher<? super T> matcher, T actual1, T actual2, T actual3, ToRun... extra) {
        assertAllMatch("", matcher, actual1, actual2, actual3, extra);
    }
    public static <T> void assertAllMatch(String title, Matcher<? super T> matcher, T actual1, T actual2, T actual3, ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher),
                () -> assertThat(actual2, matcher),
                () -> assertThat(actual3, matcher)), stream(extra)));
    }
    //endregion

    //region 4 actuals
    public static <T> void assertAllMatch(Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, ToRun... extra) {
        assertAllMatch("", matcher, actual1, actual2, actual3, actual4, extra);
    }
    public static <T> void assertAllMatch(String title, Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher),
                () -> assertThat(actual2, matcher),
                () -> assertThat(actual3, matcher),
                () -> assertThat(actual4, matcher)), stream(extra)));
    }
    //endregion

    //region 5 actuals
    public static <T> void assertAllMatch(Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, T actual5, ToRun... extra) {
        assertAllMatch("", matcher, actual1, actual2, actual3, actual4, actual5, extra);
    }
    public static <T> void assertAllMatch(String title, Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, T actual5, ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher),
                () -> assertThat(actual2, matcher),
                () -> assertThat(actual3, matcher),
                () -> assertThat(actual4, matcher),
                () -> assertThat(actual5, matcher)), stream(extra)));
    }
    //endregion

    //region 6 actuals
    public static <T> void assertAllMatch(Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, T actual5, T actual6, ToRun... extra) {
        assertAllMatch("", matcher, actual1, actual2, actual3, actual4, actual5, actual6, extra);
    }
    public static <T> void assertAllMatch(String title, Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, T actual5, T actual6, ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher),
                () -> assertThat(actual2, matcher),
                () -> assertThat(actual3, matcher),
                () -> assertThat(actual4, matcher),
                () -> assertThat(actual5, matcher),
                () -> assertThat(actual6, matcher)), stream(extra)));
    }
    //endregion

    //region 7 actuals
    public static <T> void assertAllMatch(Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, T actual5, T actual6, T actual7, ToRun... extra) {
        assertAllMatch("", matcher, actual1, actual2, actual3, actual4, actual5, actual6, actual7, extra);
    }
    public static <T> void assertAllMatch(String title, Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, T actual5, T actual6, T actual7, ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher),
                () -> assertThat(actual2, matcher),
                () -> assertThat(actual3, matcher),
                () -> assertThat(actual4, matcher),
                () -> assertThat(actual5, matcher),
                () -> assertThat(actual6, matcher),
                () -> assertThat(actual7, matcher)), stream(extra)));
    }
    //endregion

    //region 8 actuals
    public static <T> void assertAllMatch(Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, T actual5, T actual6, T actual7, T actual8, ToRun... extra) {
        assertAllMatch("", matcher, actual1, actual2, actual3, actual4, actual5, actual6, actual7, actual8, extra);
    }
    public static <T> void assertAllMatch(String title, Matcher<? super T> matcher, T actual1, T actual2, T actual3, T actual4, T actual5, T actual6, T actual7, T actual8, ToRun... extra) {
        assertEach(title, concat(Stream.of(
                () -> assertThat(actual1, matcher),
                () -> assertThat(actual2, matcher),
                () -> assertThat(actual3, matcher),
                () -> assertThat(actual4, matcher),
                () -> assertThat(actual5, matcher),
                () -> assertThat(actual6, matcher),
                () -> assertThat(actual7, matcher),
                () -> assertThat(actual8, matcher)), stream(extra)));
    }
    //endregion
}
