package ru.serge2nd.test.match;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;
import ru.serge2nd.test.util.Alert;

import java.util.function.Predicate;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Objects.requireNonNull;
import static ru.serge2nd.test.Assist.errNotInstantiable;

/**
 * Creates {@link Matcher} instances consisting of the passed components
 * (the match target type, the description, the matching condition, the mismatch alert).
 */
public class MatcherFactory {
    private MatcherFactory() { throw errNotInstantiable(lookup()); }

    public static <T> Matcher<T> matcher(SelfDescribing descriptor, Predicate<? super T> predicate) {
        return matcher(descriptor, predicate, null);
    }
    public static <T> Matcher<T> matcher(SelfDescribing descriptor, Predicate<? super T> predicate, Alert<? super T> alert) {
        requireNonNull(descriptor, "null descriptor");
        requireNonNull(predicate, "null predicate");

        return new BaseMatcher<T>() {
        @Override
        public void describeTo(Description d) { descriptor.describeTo(d); }
        @Override @SuppressWarnings("unchecked")
        public boolean matches(Object actual) { return predicate.test((T)actual); }
        @Override @SuppressWarnings("unchecked")
        public void describeMismatch(Object actual, Description d) {
            if (alert != null)
                alert.accept((T)actual, d);
            else
                d.appendValue(actual);
        }
    };}

    public static <T> Matcher<T> matcher(Class<? extends T> type, SelfDescribing descriptor, Predicate<? super T> predicate) {
        return matcher(type, descriptor, predicate, null);
    }
    public static <T> Matcher<T> matcher(Class<? extends T> type, SelfDescribing descriptor, Predicate<? super T> predicate, Alert<? super T> alert) {
        if (type == null) return matcher(descriptor, predicate, alert);
        requireNonNull(descriptor, "null descriptor");
        requireNonNull(predicate, "null predicate");

        return new TypeSafeMatcher<T>(type) {
        @Override
        public void describeTo(Description d) { descriptor.describeTo(d); }
        @Override
        public boolean matchesSafely(T actual) { return predicate.test(actual); }
        @Override
        public void describeMismatchSafely(T actual, Description d) {
            if (alert != null)
                alert.accept(actual, d);
            else
                d.appendValue(actual);
        }
    };}
}
