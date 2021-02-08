package ru.serge2nd.test.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class MultipleFailuresError extends AssertionError {
    protected final List<Failure> suppressed = new ArrayList<>();

    public MultipleFailuresError(Object msg) { super(msg); }

    /** @see #suppresses(Collection) */
    public final MultipleFailuresError suppresses(Failure... errors)                    { suppresses(asList(errors)); return this; }
    /** Adds suppressed errors avoiding appearance in the standard stacktrace. */
    public final MultipleFailuresError suppresses(Collection<? extends Failure> errors) { suppressed.addAll(errors); return this; }

    /** Returns entries added by {@link #suppresses(Failure...)}, {@link #suppresses(Collection)}. */
    public List<Failure> suppressed() { return unmodifiableList(suppressed); }
}
