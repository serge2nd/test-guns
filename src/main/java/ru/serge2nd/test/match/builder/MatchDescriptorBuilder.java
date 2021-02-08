package ru.serge2nd.test.match.builder;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.Supplier;

import static ru.serge2nd.test.match.MatchAssist.descriptor;
import static ru.serge2nd.test.match.MatchAssist.idDescriptor;
import static ru.serge2nd.test.match.MatchAssist.listDescriptors;

/**
 * A mixin for {@link Matcher} builders to build the description of the matcher (picked by {@link Matcher#describeTo(Description) describeTo()}).
 * @param <Builder> the matcher builder actual type
 */
public interface MatchDescriptorBuilder<Builder extends MatchDescriptorBuilder<? super Builder>> {

    /** Appends the given descriptor to the end. */
    Builder describe(SelfDescribing sd);
    /** Appends the given descriptor to the start. */
    Builder describeFirst(SelfDescribing sd);

    default Builder describe(String prefix, Iterable<? extends SelfDescribing> sds)      { return describe(listDescriptors(prefix, sds)); }
    default Builder describeFirst(String prefix, Iterable<? extends SelfDescribing> sds) { return describeFirst(listDescriptors(prefix, sds)); }

    default Builder append(String text)                  { return describe(d -> d.appendText(text)); }
    default Builder append(String text, int $)           { return describeFirst(d -> d.appendText(text)); }

    default Builder append(Supplier<String> text)        { return describe(d -> d.appendText(text.get())); }
    default Builder append(Supplier<String> text, int $) { return describeFirst(d -> d.appendText(text.get())); }

    default Builder append(Object val)                   { return describe(descriptor(val)); }
    default Builder append(Object val, int $)            { return describeFirst(descriptor(val)); }

    default Builder appendId(Object val)                 { return describe(idDescriptor(val)); }
    default Builder appendId(Object val, int $)          { return describeFirst(idDescriptor(val)); }
}
