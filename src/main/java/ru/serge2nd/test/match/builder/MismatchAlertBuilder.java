package ru.serge2nd.test.match.builder;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.serge2nd.test.util.Alert;

import java.util.function.Function;

import static java.util.function.Function.identity;
import static ru.serge2nd.test.Assist.cast;
import static ru.serge2nd.test.match.MatchAssist.descriptor;
import static ru.serge2nd.test.match.MatchAssist.idDescriptor;

/**
 * A mixin for {@link Matcher} builders to build the mismatch alert (picked by {@link Matcher#describeMismatch(Object, Description) describeMismatch()}).
 * @param <T> the type for which the matcher is applicable
 * @param <Builder> the matcher builder actual type
 */
public interface MismatchAlertBuilder<T, Builder extends MismatchAlertBuilder<T, ? super Builder>> {

    /** Appends the given alert to the end. */
    Builder alert(Alert<? super T> r);
    /** Appends the given alert to the start. */
    Builder alertFirst(Alert<? super T> r);

    default Builder alert(String text)                      { return alert((v, d) -> d.appendText(text)); }
    default Builder alert(String text, int $)               { return alertFirst((v, d) -> d.appendText(text)); }

    default Builder alert(Function<T, String> toStr)        { return alert((v, d) -> d.appendText(toStr.apply(v))); }
    default Builder alert(Function<T, String> toStr, int $) { return alertFirst((v, d) -> d.appendText(toStr.apply(v))); }

    default Builder alert()        { return alertOf(identity()); }
    default Builder alert(int $)   { return alertOf(identity(), $); }

    default Builder alertId()      { return alertIdOf(identity()); }
    default Builder alertId(int $) { return alertIdOf(identity(), $); }

    default Builder alertOf(Function<? super T, ?> f)          { return cast(alert((v, d) -> descriptor(f.apply(v)).describeTo(d))); }
    default Builder alertOf(Function<? super T, ?> f, int $)   { return cast(alertFirst((v, d) -> descriptor(f.apply(v)).describeTo(d))); }

    default Builder alertIdOf(Function<? super T, ?> f)        { return cast(alert((v, d) -> idDescriptor(f.apply(v)).describeTo(d))); }
    default Builder alertIdOf(Function<? super T, ?> f, int $) { return cast(alertFirst((v, d) -> idDescriptor(f.apply(v)).describeTo(d))); }
}
