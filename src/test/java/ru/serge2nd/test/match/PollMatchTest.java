package ru.serge2nd.test.match;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.System.identityHashCode;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static java.util.Collections.singleton;
import static org.hamcrest.StringDescription.asString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static ru.serge2nd.test.match.CoreMatch.equalTo;
import static ru.serge2nd.test.match.CoreMatch.sameClass;
import static ru.serge2nd.test.match.CoreMatchTest.*;
import static ru.serge2nd.test.match.PollMatch.hasNext;
import static ru.serge2nd.test.match.PollMatch.noNext;

public class PollMatchTest {
    static final Matcher<Iterator<? extends String>> NEXT_IS   = PollMatch.nextIs(S1);
    static final Matcher<Iterator<? extends String>> NEXT_SAME = PollMatch.nextIsSame(S1);

    @Before public void setUp() { BUF.delete(0, BUF.length()); }

    @Test
    public void testHasNext() {
        assertTrue("unexpected mismatch", hasNext().matches(L.iterator()));
        assertEquals("next is present", asString(hasNext()));
    }
    @Test
    public void testNotHasNext() {
        assertFalse("unexpected match", hasNext().matches(emptyIterator()));
        hasNext().describeMismatch(L.iterator(), new StringDescription(BUF));
        assertEquals("was absent", BUF.toString());
    }

    @Test
    public void testNoNext() {
        assertTrue("unexpected mismatch", noNext().matches(emptyIterator()));
        assertEquals("next is absent", asString(noNext()));
    }
    @Test
    public void testNotNoNext() {
        assertFalse("unexpected match", noNext().matches(L.iterator()));
        noNext().describeMismatch(emptyIterator(), new StringDescription(BUF));
        assertEquals("was present", BUF.toString());
    }

    @Test
    public void testNextIs() {
        assertTrue("unexpected mismatch", NEXT_IS.matches(singleton(S2).iterator()));
        assertEquals(format("next is present & \"%s\"", S1), asString(NEXT_IS));
    }
    @Test
    public void testNotNextIs() {
        assertFalse("unexpected match", NEXT_IS.matches(emptyIterator()));
        NEXT_IS.describeMismatch(emptyIterator(), new StringDescription(BUF));
        assertEquals("was absent", BUF.toString());
    }

    @Test
    public void testNextIsSame() {
        assertTrue("unexpected mismatch", NEXT_SAME.matches(singleton(S1).iterator()));
        assertEquals(format("next is present & %s@%x_%x->%s", S1.getClass().getSimpleName(), identityHashCode(S1), S1.hashCode(), quote(S1)), asString(NEXT_SAME));
    }
    @Test
    public void testNotNextIsSame() {
        assertFalse("unexpected match", NEXT_SAME.matches(singleton(S2).iterator()));
        NEXT_SAME.describeMismatch(singleton(S1).iterator(), new StringDescription(BUF));
        assertEquals(format("%s@%x_%x->%s", S1.getClass().getSimpleName(), identityHashCode(S2), S1.hashCode(), quote(S1)), BUF.toString());
    }

    @Test
    public void testGives() {
        Matcher<Supplier<? extends String>> m = PollMatch.gives(S3, S1);
        assertTrue("unexpected mismatch", m.matches(s(asList(S3, S1).iterator()::next)));
        assertEquals(format("gives [%s, %s]", quote(S3), quote(S1)), asString(m));
    }
    @Test
    public void testGivesThrows() {
        Matcher<? super Supplier<? extends String>> m = PollMatch.gives(equalTo(S3), equalTo(S1), sameClass(NoSuchElementException.class));
        assertTrue("unexpected mismatch", m.matches(s(asList(S3, S1).iterator()::next)));
        assertEquals(format("gives [%s, %s, exactly a %s]", quote(S3), quote(S1), NoSuchElementException.class.getName()), asString(m));
    }
    @Test
    public void testNotGives() {
        Matcher<Supplier<? extends String>> m = PollMatch.gives(S3, S1, null);
        assertFalse("unexpected match", m.matches(s(asList(S3, S1).iterator()::next)));
        m.describeMismatch(NULL, new StringDescription(BUF));
        assertEquals("item 2: " + angle(NoSuchElementException.class.getName()), BUF.toString());
    }

    static Supplier<String> s(Supplier<String> s) { return s; }

    static final StringBuilder BUF = new StringBuilder();

    static final Supplier<String> NULL = ()->null;
}