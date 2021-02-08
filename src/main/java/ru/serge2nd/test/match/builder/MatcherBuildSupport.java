package ru.serge2nd.test.match.builder;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import ru.serge2nd.test.util.Alert;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static ru.serge2nd.test.Assist.cast;
import static ru.serge2nd.test.match.MatchAssist.join;
import static ru.serge2nd.test.match.MatcherFactory.matcher;
import static ru.serge2nd.test.match.MatchAssist.EMPTY;
import static ru.serge2nd.test.match.MatchAssist.NOTHING;

/**
 * Supports the building state for {@link Matcher} builders and provides the {@link #build()} method.
 * @param <T> the type for which the matcher is applicable
 * @param <Builder> the matcher builder actual type
 */
public abstract class MatcherBuildSupport<T, Builder extends MatcherBuildSupport<T, ? super Builder>> implements MatcherBuildOptions<Builder> {
    protected Predicate<? super T> predicate;
    protected SelfDescribing descriptor = EMPTY;
    protected Alert<Object> alert = NOTHING;
    protected int options;

    /** The next matcher. */
    protected Matcher<?> next;
    /** The value mapper for the next matcher. */
    protected Function<? super T, ?> forNext;

    /** The class for which instances the matcher is applicable (for anything if <code>null</code>). */
    public abstract Class<? extends T> getType();

    @Override
    public int     options()        { return options; }
    @Override
    public Builder set(int options) { this.options |= options; return cast(this); }

    //region Building

    public <Z> Matcher<Z> build() {
        if (next == null || forNext == null)
            return cast(matcher(getType(), descriptor, safePredicate(), cast(alert)));

        SelfDescribing finalDescriptor = is(END_DESC) ? descriptor : join(AND, descriptor, next);
        if (is(END_ALERT))
            return cast(matcher(getType(), finalDescriptor, is(END_MATCH) ? safePredicate() : safePredicate().and(v -> next.matches(forNext.apply(v))), cast(alert)));

        ThreadLocal sub = new ThreadLocal(); // caching value for the next matcher
        return cast(matcher(getType(), finalDescriptor, finalPredicate(sub), finalAlert(sub)));
    }

    Predicate<T> finalPredicate(ThreadLocal sub) {
        safePredicate();
        return is(FALL_ALERT)
        ? /* cache sub-value anyway */
            is(END_MATCH)
            ? v -> { sub.save(v, forNext)           ; return predicate.test(v); }
            : v -> { Object w = sub.save(v, forNext); return predicate.test(v) && next.matches(w); }
        : /* cache sub-value if predicate passed */
            is(END_MATCH)
            ? v -> { sub.reset(); return predicate.test(v); }
            : v -> { sub.reset(); return predicate.test(v) && next.matches(sub.save(v, forNext)); };
    }
    Alert<T> finalAlert(ThreadLocal sub) {
        if (!is(FORCE_ALERT) || alert == NOTHING) {
            // if the sub-value is present, describe it only,
            // otherwise describe the current
            return (v, d) -> sub.peek(
                    w -> next.describeMismatch(w, d),
                    () -> alert.accept(v, d));
        } else {
            // describe both the current and the next
            return cast(alert.andThen(($, d) -> sub.peek(
                    w -> next.describeMismatch(w, d.appendText(AND)))));
        }
    }
    Predicate<T> safePredicate() { return cast(requireNonNull(predicate, "null predicate")); }
    //endregion

    //region Helpers

    @SuppressWarnings("unchecked,rawtypes")
    static class ThreadLocal extends java.lang.ThreadLocal<Object> {
        ThreadLocal()                     { reset(); }
        Object save(Object v)             { set(v); return v; }
        Object save(Object u, Function f) { return save(f.apply(u)); }
        void   reset()                    { set(NULL); }

        void peek(Consumer<Object> consumer) {
            peek(consumer, () -> {});
        }
        void peek(Consumer<Object> consumer, Runnable orElse) {
            Object v = get();
            if (v != NULL)
                consumer.accept(v);
            else
                orElse.run();
        }
    }

    static final Object NULL = new Object();
    static final String AND  = " & ";
    //endregion
}
