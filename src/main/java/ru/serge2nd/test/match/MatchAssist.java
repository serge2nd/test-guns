package ru.serge2nd.test.match;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import ru.serge2nd.test.util.Alert;

import java.util.function.Function;

import static java.lang.invoke.MethodHandles.lookup;
import static ru.serge2nd.test.Assist.describeIdentity;
import static ru.serge2nd.test.Assist.errNotInstantiable;

/**
 * Helpers in composing {@link Matcher}s.
 */
public class MatchAssist {
    private MatchAssist() { throw errNotInstantiable(lookup()); }

    public static final SelfDescribing EMPTY  = $ -> {};
    public static final Alert<Object> NOTHING = ($0, $1) -> {};

    public static SelfDescribing descriptor(Object val)   { return d -> d.appendValue(val); }
    public static SelfDescribing idDescriptor(Object val) { return d -> d.appendText(describeIdentity(val)).appendText("->").appendValue(val); }

    public static SelfDescribing listDescriptors(String prefix, Iterable<? extends SelfDescribing> descriptors) {
        return d -> d.appendText(prefix).appendList("[", ", ", "]", descriptors);
    }

    public static SelfDescribing join(String delim, SelfDescribing... descriptors) {
        SelfDescribing result = EMPTY;

        for (SelfDescribing descriptor : descriptors)
            if (result == EMPTY && descriptor != null)
                result = descriptor;
            else if (descriptor != null && descriptor != EMPTY) {
                SelfDescribing old = result;
                result = d -> d.appendDescriptionOf(old).appendText(delim).appendDescriptionOf(descriptor);
            }

        return result;
    }

    public static <T> Alert<T> alertAs(Function<? super T, ?> f, Matcher<?> m) {
        return (v, d) -> m.describeMismatch(f.apply(v), d);
    }
}
