package ru.serge2nd.test.match;

import org.hamcrest.Matcher;

import java.util.stream.Stream;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.stream;
import static ru.serge2nd.test.Asserting.assertEach;
import static ru.serge2nd.test.Assist.errNotInstantiable;
import static ru.serge2nd.test.match.AssertThat.assertThat;

/**
 * Asserting with multiple matchers + many actuals.
 */
public class AssertForMany {
    private AssertForMany() { throw errNotInstantiable(lookup()); }

    //region 1 matcher
    @SafeVarargs
    public static <T> void assertForMany(Matcher<? super T> matcher, T... actuals) {
        assertForMany("", matcher, actuals);
    }
    @SafeVarargs
    public static <T> void assertForMany(String title, Matcher<? super T> matcher, T... actuals) {
        assertEach(title, stream(actuals).map(actual -> () -> assertThat(actual, matcher)));
    }
    //endregion

    //region 2 matchers
    @SafeVarargs
    public static <T> void assertForMany(Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         T... actuals) {
        assertForMany("", matcher1, matcher2, actuals);
    }
    @SafeVarargs
    public static <T> void assertForMany(String title,
                                         Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         T... actuals) {
        assertEach(title, stream(actuals).flatMap(actual -> Stream.of(
                () -> assertThat(actual, matcher1),
                () -> assertThat(actual, matcher2))));
    }
    //endregion

    //region 3 matchers
    @SafeVarargs
    public static <T> void assertForMany(Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         T... actuals) {
        assertForMany("", matcher1, matcher2, matcher3, actuals);
    }
    @SafeVarargs
    public static <T> void assertForMany(String title,
                                         Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         T... actuals) {
        assertEach(title, stream(actuals).flatMap(actual -> Stream.of(
                () -> assertThat(actual, matcher1),
                () -> assertThat(actual, matcher2),
                () -> assertThat(actual, matcher3))));
    }
    //endregion

    //region 4 matchers
    @SafeVarargs
    public static <T> void assertForMany(Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         T... actuals) {
        assertForMany("", matcher1, matcher2, matcher3, matcher4, actuals);
    }
    @SafeVarargs
    public static <T> void assertForMany(String title,
                                         Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         T... actuals) {
        assertEach(title, stream(actuals).flatMap(actual -> Stream.of(
                () -> assertThat(actual, matcher1),
                () -> assertThat(actual, matcher2),
                () -> assertThat(actual, matcher3),
                () -> assertThat(actual, matcher4))));
    }
    //endregion

    //region 5 matchers
    @SafeVarargs
    public static <T> void assertForMany(Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         Matcher<? super T> matcher5,
                                         T... actuals) {
        assertForMany("", matcher1, matcher2, matcher3, matcher4, matcher5, actuals);
    }
    @SafeVarargs
    public static <T> void assertForMany(String title,
                                         Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         Matcher<? super T> matcher5,
                                         T... actuals) {
        assertEach(title, stream(actuals).flatMap(actual -> Stream.of(
                () -> assertThat(actual, matcher1),
                () -> assertThat(actual, matcher2),
                () -> assertThat(actual, matcher3),
                () -> assertThat(actual, matcher4),
                () -> assertThat(actual, matcher5))));
    }
    //endregion

    //region 6 matchers
    @SafeVarargs
    public static <T> void assertForMany(Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         Matcher<? super T> matcher5,
                                         Matcher<? super T> matcher6,
                                         T... actuals) {
        assertForMany("", matcher1, matcher2, matcher3, matcher4, matcher5, matcher6, actuals);
    }
    @SafeVarargs
    public static <T> void assertForMany(String title,
                                         Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         Matcher<? super T> matcher5,
                                         Matcher<? super T> matcher6,
                                         T... actuals) {
        assertEach(title, stream(actuals).flatMap(actual -> Stream.of(
                () -> assertThat(actual, matcher1),
                () -> assertThat(actual, matcher2),
                () -> assertThat(actual, matcher3),
                () -> assertThat(actual, matcher4),
                () -> assertThat(actual, matcher5),
                () -> assertThat(actual, matcher6))));
    }
    //endregion

    //region 7 matchers
    @SafeVarargs
    public static <T> void assertForMany(Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         Matcher<? super T> matcher5,
                                         Matcher<? super T> matcher6,
                                         Matcher<? super T> matcher7,
                                         T... actuals) {
        assertForMany("", matcher1, matcher2, matcher3, matcher4, matcher5, matcher6, matcher7, actuals);
    }
    @SafeVarargs
    public static <T> void assertForMany(String title,
                                         Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         Matcher<? super T> matcher5,
                                         Matcher<? super T> matcher6,
                                         Matcher<? super T> matcher7,
                                         T... actuals) {
        assertEach(title, stream(actuals).flatMap(actual -> Stream.of(
                () -> assertThat(actual, matcher1),
                () -> assertThat(actual, matcher2),
                () -> assertThat(actual, matcher3),
                () -> assertThat(actual, matcher4),
                () -> assertThat(actual, matcher5),
                () -> assertThat(actual, matcher6),
                () -> assertThat(actual, matcher7))));
    }
    //endregion

    //region 8 matchers
    @SafeVarargs
    public static <T> void assertForMany(Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         Matcher<? super T> matcher5,
                                         Matcher<? super T> matcher6,
                                         Matcher<? super T> matcher7,
                                         Matcher<? super T> matcher8,
                                         T... actuals) {
        assertForMany("", matcher1, matcher2, matcher3, matcher4, matcher5, matcher6, matcher7, matcher8, actuals);
    }
    @SafeVarargs
    public static <T> void assertForMany(String title,
                                         Matcher<? super T> matcher1,
                                         Matcher<? super T> matcher2,
                                         Matcher<? super T> matcher3,
                                         Matcher<? super T> matcher4,
                                         Matcher<? super T> matcher5,
                                         Matcher<? super T> matcher6,
                                         Matcher<? super T> matcher7,
                                         Matcher<? super T> matcher8,
                                         T... actuals) {
        assertEach(title, stream(actuals).flatMap(actual -> Stream.of(
                () -> assertThat(actual, matcher1),
                () -> assertThat(actual, matcher2),
                () -> assertThat(actual, matcher3),
                () -> assertThat(actual, matcher4),
                () -> assertThat(actual, matcher5),
                () -> assertThat(actual, matcher6),
                () -> assertThat(actual, matcher7),
                () -> assertThat(actual, matcher8))));
    }
    //endregion
}
