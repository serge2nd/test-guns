package ru.serge2nd.test.match.builder;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import ru.serge2nd.test.match.CoreMatch;
import ru.serge2nd.test.util.Alert;

import java.util.function.Function;
import java.util.function.Predicate;

import static ru.serge2nd.test.Assist.cast;
import static ru.serge2nd.test.Assist.extractTypeArg;
import static ru.serge2nd.test.Predicates.all;
import static ru.serge2nd.test.Predicates.anyone;
import static ru.serge2nd.test.Predicates.not;
import static ru.serge2nd.test.match.MatchAssist.EMPTY;
import static ru.serge2nd.test.match.MatchAssist.NOTHING;

/**
 * A builder of a {@link Matcher}. Constructs the description, the matching condition and the mismatch alert of the matcher.<br>
 * See {@link CoreMatch} and other matcher factories in the same package for usage examples.
 * @param <T> the type for which the matcher is applicable
 * @see MatchDescriptorBuilder
 * @see MismatchAlertBuilder
 */
public abstract class MatcherBuilder<T> extends MatcherBuildSupport<T, MatcherBuilder<T>>
        implements MatchDescriptorBuilder<MatcherBuilder<T>>, MismatchAlertBuilder<T, MatcherBuilder<T>> {
    protected final Class<? extends T> type;

    //region Instantiating

    public static <T> MatcherBuilder<T> create()                                                             { return matcherBuilder(); }
    public static <T> MatcherBuilder<T> matcherBuilder()                                                     { return new WithExplicitType<>(null); }
    public static <T> MatcherBuilder<T> matcherBuilder(Class<? extends T> type)                              { return new WithExplicitType<>(type); }
    public static <T> MatcherBuilder<T> matcherBuilder(Matcher<? super T> delegate)                          { return new WithExplicitType<>(delegate, null); }
    public static <T> MatcherBuilder<T> matcherBuilder(Matcher<? super T> delegate, Class<? extends T> type) { return new WithExplicitType<>(delegate, type); }

    public MatcherBuilder()                            { this.type = extractTypeArg(getClass()); }
    public MatcherBuilder(Matcher<? super T> delegate) { this(); init(delegate); }
    //endregion

    //region Hidden constructors

    protected MatcherBuilder(Class<? extends T> type) {
        this.type = type;
    }
    protected MatcherBuilder(Matcher<? super T> delegate, Class<? extends T> type) {
        this(type); init(delegate);
    }
    protected void init(Matcher<? super T> delegate) {
        if (delegate != null) {
            descriptor = delegate;
            predicate = delegate::matches;
            alert = delegate::describeMismatch;
        }
    }
    //endregion

    //region Descriptor

    public MatcherBuilder<T> blank()                          { descriptor = EMPTY; return this; }
    public MatcherBuilder<T> describe(SelfDescribing sd)      { SelfDescribing old = descriptor; descriptor = d -> d.appendDescriptionOf(old).appendDescriptionOf(sd); return this; }
    public MatcherBuilder<T> describeFirst(SelfDescribing sd) { SelfDescribing old = descriptor; descriptor = d -> d.appendDescriptionOf(sd).appendDescriptionOf(old); return this; }
    //endregion

    //region Predicate

    public MatcherBuilder<T> matchIf(Predicate<? super T> p) { predicate = p; return this; }
    public MatcherBuilder<T> reject()                        { predicate = not(predicate); return this; }
    public MatcherBuilder<T> first(Predicate<? super T> p)   { predicate = all(p, predicate); return this; }
    public MatcherBuilder<T> and(Predicate<? super T> p)     { predicate = all(predicate, p); return this; }
    //endregion

    //region Mismatch report

    public MatcherBuilder<T> calm()                         { alert = NOTHING; return this; }
    public MatcherBuilder<T> alert(Alert<? super T> r)      { alert = alert.andThen(cast(r)); return this; }
    public MatcherBuilder<T> alertFirst(Alert<? super T> r) { alert = cast(r.andThen(alert)); return this; }
    //endregion

    //region Subsequent match

    public <V> MatcherBuilder<V> then(Function<? super T, ? extends V> f)                          { return then(f, null, null); }
    public <V> MatcherBuilder<V> then(Function<? super T, ? extends V> f, Class<? extends V> type) { return then(f, null, type); }
    public <V> MatcherBuilder<V> then(Function<? super T, ? extends V> f, Matcher<? super V> m)    { return then(f, m, null); }
    public <V> MatcherBuilder<V> then(Function<? super T, ? extends V> f, Matcher<? super V> m, Class<? extends V> type) {
        if (predicate == null) predicate = anyone();
        MatcherBuilder<T> origin = this; forNext = f;

        return new MatcherBuilder<V>(m, type) {
            @Override
            public <Z> Matcher<Z> build() { origin.next = super.build(); return origin.build(); }
        };
    }
    //endregion

    @Override
    public Class<? extends T> getType() { return type; }

    static class WithExplicitType<T> extends MatcherBuilder<T> {
        WithExplicitType(Matcher<? super T> delegate, Class<? extends T> type) { super(delegate, type); }
        WithExplicitType(Class<? extends T> type) { super(type); }
    }
}
