package ru.serge2nd.test.match.builder;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import ru.serge2nd.test.util.Alert;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.lang.System.identityHashCode;
import static java.util.function.UnaryOperator.identity;
import static org.junit.Assert.*;

@SuppressWarnings("StringOperationCanBeSimplified")
public class MatcherBuildSupportTest {
    static final String S1 = "xyz", S2 = new String(S1), Z = "xyza", Y = "RIGHT";
    static final Predicate<String> P = S1::equals;
    static final SelfDescribing DESC = d -> d.appendText(Y);
    static final Alert<Object> ALERT = (v, d) -> d.appendValue(v);

    static final Matcher<?> NEXT = new BaseMatcher<Object>() {
        public boolean matches(Object actual) { return S1 == actual; }
        public void describeTo(Description d) { d.appendValue(identityHashCode(S1)); }
        public void describeMismatch(Object x, Description d) { d.appendValue(identityHashCode(x)); }
    };
    static final UnaryOperator<String> F = identity();

    @Before public void reset() { BUF.delete(0, BUF.length()); }

    @Test
    public void testPlain() {
        Matcher<?> m = new MatcherBuildSupport(false, null, null).build();
        assertTrue("unexpected mismatch", m.matches(S2));
        assertFalse("unexpected match", m.matches(Z));

        m.describeMismatch(S1, DESC_BUF);
        assertEquals('"'+ S1 +'"', BUF.toString());

        reset();
        m.describeTo(DESC_BUF);
        assertEquals(Y, BUF.toString());
    }
    @Test
    public void testPlainTyped() {
        Matcher<?> m = new MatcherBuildSupport(true, null, null).build();
        m.describeMismatch(CS, DESC_BUF);
        assertEquals("was a " + CS.getClass().getName() + " (<>)", BUF.toString());
    }

    @Test
    public void testComplexMatcher() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).build();
        assertTrue("unexpected mismatch", m.matches(S1));
        assertFalse("unexpected match", m.matches(Z));

        m.describeMismatch(S1, DESC_BUF);
        assertEquals('"'+ S1 +'"', BUF.toString());

        reset();
        assertFalse("unexpected match", m.matches(S2));
        m.describeMismatch(S1, DESC_BUF);
        assertEquals("<"+identityHashCode(S2)+">", BUF.toString());

        reset();
        m.describeTo(DESC_BUF);
        assertEquals(Y + " & <" + identityHashCode(S1) + ">", BUF.toString());
    }

    @Test
    public void testEndDesc() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).endDesc().build();
        m.describeTo(DESC_BUF);
        assertEquals(Y, BUF.toString());
    }
    @Test
    public void testEndAlert() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).endAlert().build();
        assertFalse("unexpected match", m.matches(S2));
        m.describeMismatch(S1, DESC_BUF);
        assertEquals('"'+ S1 +'"', BUF.toString());
    }
    @Test
    public void testEndAlertAndMatch() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).endAlert().endMatch().build();
        assertTrue("unexpected mismatch", m.matches(S2));
    }

    @Test
    public void testEndMatch() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).endMatch().build();
        assertTrue("unexpected mismatch", m.matches(S2));
        m.describeMismatch(S1, DESC_BUF);
        assertEquals('"'+ S1 +'"', BUF.toString());
    }
    @Test
    public void testFallAlert() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).fallAlert().build();
        assertFalse("unexpected match", m.matches(Z));
        m.describeMismatch(S1, DESC_BUF);
        assertEquals("<"+identityHashCode(Z)+">", BUF.toString());
    }
    @Test
    public void testStopMatch() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).stopMatch().build();
        assertTrue("unexpected mismatch", m.matches(S2));
        m.describeMismatch(S1, DESC_BUF);
        assertEquals("<"+identityHashCode(S2)+">", BUF.toString());
    }

    @Test
    public void testForceAlert() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).forceAlert().build();
        assertFalse("unexpected match", m.matches(S2));
        m.describeMismatch(S1, DESC_BUF);
        assertEquals('"'+ S1 +'"' + " & <"+identityHashCode(S2)+">", BUF.toString());
    }
    @Test
    public void testLongAlert() {
        Matcher<?> m = new MatcherBuildSupport(false, NEXT, F).longAlert().build();
        assertFalse("unexpected match", m.matches(Z));
        m.describeMismatch(S1, DESC_BUF);
        assertEquals('"'+ S1 +'"' + " & <"+identityHashCode(Z)+">", BUF.toString());
    }

    static class MatcherBuildSupport extends ru.serge2nd.test.match.builder.MatcherBuildSupport<String, MatcherBuildSupport> {
        final Class<String> type;

        public MatcherBuildSupport(boolean typed, Matcher<?> next, Function<String, ?> forNext) {
            this.type = typed ? String.class : null;
            this.predicate = P;
            this.descriptor = DESC;
            this.alert = ALERT;
            this.next = next;
            this.forNext = forNext;
        }

        public Class<String> getType() { return type; }
    }

    static final CharSequence CS = new CharSequence() {
        public int          length()                        { return 0; }
        public char         charAt(int index)               { return 0; }
        public CharSequence subSequence(int start, int end) { return ""; }
        public String       toString()                      { return ""; }
    };

    static final StringBuilder BUF = new StringBuilder();
    static final Description DESC_BUF = new StringDescription(BUF);
}