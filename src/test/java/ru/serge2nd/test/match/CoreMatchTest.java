package ru.serge2nd.test.match;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import ru.serge2nd.test.util.ToRun;

import java.net.BindException;
import java.util.*;

import static java.lang.String.format;
import static java.lang.System.identityHashCode;
import static java.util.Collections.*;
import static org.hamcrest.StringDescription.asString;
import static org.junit.Assert.*;
import static ru.serge2nd.test.match.CoreMatch.absent;
import static ru.serge2nd.test.match.CoreMatch.presented;

@SuppressWarnings("StringOperationCanBeSimplified,OptionalUsedAsFieldOrParameterType")
public class CoreMatchTest {
    static final String S1 = "abc";
    static final String S2 = new String(S1);
    static final String S3 = "abcd";
    static final Optional<String> O1 = Optional.of(S1);
    static final Optional<String> O2 = Optional.of(S2);
    static final Optional<String> O3 = Optional.of(S3);
    static final List<?> L = new ArrayList<>(singleton(S1));
    static final Throwable E1 = new Throwable();
    static final Throwable E2 = new Throwable();

    static final Matcher<String>      EQ            = CoreMatch.equalTo(S1);
    static final Matcher<String>      SAME          = CoreMatch.sameAs(S1);
    static final Matcher<Object>      NOT_NULL      = CoreMatch.notNullRef(S1);
    static final Matcher<Object>      SAME_CLS      = CoreMatch.sameClass(L);
    static final Matcher<Optional<?>> PRESENT       = CoreMatch.presented(S1);
    static final Matcher<Optional<?>> PRESENT_SAME  = CoreMatch.presentedSame(S1);
    static final Matcher<ToRun>       FAILS         = CoreMatch.fails(ArrayStoreException.class, S1);
    static final Matcher<ToRun>       FAILS_ANY_MSG = CoreMatch.fails(BindException.class);
    static final Matcher<ToRun>       FAILS_SAME    = CoreMatch.fails(E1);
    static final Matcher<ToRun>       NO_FAIL       = CoreMatch.noFail();

    @Before public void setUp() { BUF.delete(0, BUF.length()); }

    @Test
    public void testEqualTo() {
        assertTrue("unexpected mismatch", EQ.matches(S2));
        assertEquals(quote(S1), asString(EQ));
    }
    @Test
    public void testNotEqualTo() {
        assertFalse("unexpected match", EQ.matches(S3));
        EQ.describeMismatch(S2, DESC_BUF);
        assertEquals(quote(S2), BUF.toString());
    }

    @Test
    public void testSameAs() {
        assertTrue("unexpected mismatch", SAME.matches(S1));
        assertEquals(format("%s@%x_%x->%s", S1.getClass().getSimpleName(), identityHashCode(S1), S1.hashCode(), quote(S1)), asString(SAME));
    }
    @Test
    public void testNotSameAs() {
        assertFalse("unexpected match", SAME.matches(S2));
        SAME.describeMismatch(S3, DESC_BUF);
        assertEquals(format("%s@%x_%x->%s", S3.getClass().getSimpleName(), identityHashCode(S3), S3.hashCode(), quote(S3)), BUF.toString());
    }

    @Test
    public void testNotNullRef() {
        assertTrue("unexpected mismatch", NOT_NULL.matches(NOT_NULL));
        assertEquals(S1, asString(NOT_NULL));
    }
    @Test
    public void testNotNotNullRef() {
        assertFalse("unexpected match", NOT_NULL.matches(null));
        NOT_NULL.describeMismatch(S3, DESC_BUF);
        assertEquals(quote(S3), BUF.toString());
    }

    @Test
    public void testSameClass() {
        assertTrue("unexpected mismatch", SAME_CLS.matches(new ArrayList<>()));
        assertEquals("exactly a " + ArrayList.class.getName(), asString(SAME_CLS));
    }
    @Test
    public void testNotSameClass() {
        assertFalse("unexpected match", SAME_CLS.matches(new ArrayListSub<>()));
        SAME_CLS.describeMismatch(L, DESC_BUF);
        assertEquals("present a " + ArrayList.class.getName(), BUF.toString());
    }
    @Test
    public void testNullSameClass() {
        assertFalse("unexpected match", SAME_CLS.matches(null));
        SAME_CLS.describeMismatch(null, DESC_BUF);
        assertEquals("was null", BUF.toString());
    }

    @Test
    public void testPresented() {
        assertTrue("unexpected mismatch", presented().matches(O1));
        assertEquals("presented", asString(presented()));
    }
    @Test
    public void testNotPresented() {
        assertFalse("unexpected match", presented().matches(Optional.empty()));
        presented().describeMismatch(O1, DESC_BUF);
        assertEquals(angle(O1), BUF.toString());
    }

    @Test
    public void testAbsent() {
        assertTrue("unexpected mismatch", absent().matches(Optional.empty()));
        assertEquals("not presented", asString(absent()));
    }
    @Test
    public void testNotAbsent() {
        assertFalse("unexpected match", absent().matches(O1));
        absent().describeMismatch(O1, DESC_BUF);
        assertEquals(angle(O1), BUF.toString());
    }

    @Test
    public void testPresentedIs() {
        assertTrue("unexpected mismatch", PRESENT.matches(O2));
        assertEquals(quote(S1), asString(PRESENT));
    }
    @Test
    public void testNotPresentedIs() {
        assertFalse("unexpected match", PRESENT.matches(O3));
        PRESENT.describeMismatch(O1, DESC_BUF);
        assertEquals(quote(S3), BUF.toString());
    }

    @Test
    public void testPresentedSame() {
        assertTrue("unexpected mismatch", PRESENT_SAME.matches(O1));
        assertEquals(format("%s@%x_%x->%s", S1.getClass().getSimpleName(), identityHashCode(S1), S1.hashCode(), quote(S1)), asString(PRESENT_SAME));
    }
    @Test
    public void testNotPresentedSame() {
        assertFalse("unexpected match", PRESENT_SAME.matches(O2));
        PRESENT_SAME.describeMismatch(O3, DESC_BUF);
        assertEquals(format("%s@%x_%x->%s", S2.getClass().getSimpleName(), identityHashCode(S2), S2.hashCode(), quote(S2)), BUF.toString());
    }

    @Test
    public void testFails() {
        assertTrue("unexpected mismatch", FAILS.matches((ToRun)() -> {throw new ArrayStoreException(S1);}));
        assertEquals(format("%s(%s)", ArrayStoreException.class.getSimpleName(), S1), asString(FAILS));
    }
    @Test
    public void testFailsAnyMsg() {
        assertTrue("unexpected mismatch", FAILS_ANY_MSG.matches((ToRun)() -> {throw new BindException(S1);}));
        assertEquals(BindException.class.getName(), asString(FAILS_ANY_MSG));
    }
    @Test
    public void testFailsWrongError() {
        assertFalse("unexpected match", FAILS.matches((ToRun)() -> {throw new BindException(S1);}));
        FAILS.describeMismatch(NOTHING, DESC_BUF);
        assertEquals(format("%s(%s)", BindException.class.getName(), S1), BUF.toString());
    }
    @Test
    public void testFailsAnyMsgWrongError() {
        assertFalse("unexpected match", FAILS_ANY_MSG.matches((ToRun)() -> {throw new ArrayStoreException(S3); }));
        FAILS_ANY_MSG.describeMismatch(NOTHING, DESC_BUF);
        assertEquals(format("%s(%s)", ArrayStoreException.class.getSimpleName(), S3), BUF.toString());
    }
    @Test
    public void testFailsWrongMsg() {
        assertFalse("unexpected match", FAILS.matches((ToRun)() -> {throw new ArrayStoreException(S3);}));
        FAILS.describeMismatch(NOTHING, DESC_BUF);
        assertEquals(format("%s(%s)", ArrayStoreException.class.getSimpleName(), S3), BUF.toString());
    }
    @Test
    public void testNotFails() {
        assertFalse("unexpected match", FAILS.matches(NOTHING));
        FAILS.describeMismatch(NOTHING, DESC_BUF);
        assertEquals("nothing", BUF.toString());
    }

    @Test
    public void testFailsSame() {
        assertTrue("unexpected mismatch", FAILS_SAME.matches((ToRun)() -> {throw E1;}));
        assertEquals(format("%s@%x->%s", E1.getClass().getSimpleName(), E1.hashCode(), angle(E1.getClass().getName())), asString(FAILS_SAME));
    }
    @Test
    public void testNotFailsSame() {
        assertFalse("unexpected match", FAILS_SAME.matches((ToRun)() -> {throw E2;}));
        FAILS_SAME.describeMismatch(NOTHING, DESC_BUF);
        assertEquals(format("%s@%x->%s", E2.getClass().getSimpleName(), E2.hashCode(), angle(E2.getClass().getName())), BUF.toString());
    }

    @Test
    public void testNoFail() {
        assertTrue("unexpected mismatch", NO_FAIL.matches(NOTHING));
        assertEquals("not Throwable", asString(NO_FAIL));
    }
    @Test
    public void testNotNoFail() {
        assertFalse("unexpected match", NO_FAIL.matches((ToRun)() -> {throw E1;}));
        NO_FAIL.describeMismatch(NOTHING, DESC_BUF);
        assertEquals(E1.getClass().getSimpleName(), BUF.toString());
    }

    static String quote(String s) { return '"' + s + '"'; }
    static String angle(Object o) { return "<" + o + ">"; }

    static final StringBuilder BUF = new StringBuilder();
    static final Description DESC_BUF = new StringDescription(BUF);
    static final ToRun NOTHING = () -> {};

    static class ArrayListSub<E> extends ArrayList<E> {}
}