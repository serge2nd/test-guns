package ru.serge2nd.test;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.toHexString;
import static java.lang.System.identityHashCode;
import static org.junit.Assert.*;

public class AssistTest {
    static final String S1 = "abcde";
    static final String S2 = S1.substring(0, S1.length() - 1);

    @Test
    public void testExtractTypeArgFromParent() {
        assertSame(Byte.class, Assist.extractTypeArg(new ArrayList<Byte>(){}.getClass()));
    }
    @Test
    public void testExtractTypeArgFromAncestor() {
        assertSame(Long.class, Assist.extractTypeArg(A.class));
    }
    @Test
    public void testExtractTypeArgNotFound() {
        assertThrows(IllegalArgumentException.class, ()->Assist.extractTypeArg(ArrayList.class));
    }

    @Test
    public void testHas() {
        assertTrue("false negative with error type", Assist.has(new ArrayStoreException(), RuntimeException.class));
    }
    @Test
    public void testHasMsg() {
        assertTrue("false negative with error type and msg", Assist.has(new ArrayStoreException(S1), RuntimeException.class, S1));
    }
    @Test
    public void testHasOtherMsg() {
        assertFalse("false positive with same error type and other msg", Assist.has(new ArrayStoreException(S1), RuntimeException.class, S2));
    }
    @Test
    public void testHasOtherType() {
        assertFalse("false positive with other error type and same msg", Assist.has(new ArrayStoreException(S1), IOException.class, S1));
    }

    @Test
    public void testHasExact() {
        assertTrue("false negative with error type", Assist.hasExact(new ArrayStoreException(), ArrayStoreException.class));
    }
    @Test
    public void testHasExactMsg() {
        assertTrue("false negative with error type and msg", Assist.hasExact(new ArrayStoreException(S1), ArrayStoreException.class, S1));
    }
    @Test
    public void testHasExactOtherMsg() {
        assertFalse("false positive with same error type and other msg", Assist.hasExact(new ArrayStoreException(S1), ArrayStoreException.class, S2));
    }
    @Test
    public void testHasExactOtherType() {
        assertFalse("false positive with other error type and same msg", Assist.hasExact(new ArrayStoreException(S1), RuntimeException.class, S1));
    }

    @Test
    public void testDescribeNull() {
        assertEquals("nothing", Assist.describe(null));
    }
    @Test
    public void testDescribe() {
        assertEquals(ArrayStoreException.class.getSimpleName() + "(" + S1 + ")", Assist.describe(new ArrayStoreException(S1)));
    }
    @Test
    public void testDescribeMsg() {
        assertEquals(ArrayStoreException.class.getSimpleName() + "(" + S1 + ")", Assist.describe(ArrayStoreException.class, S1));
    }

    @Test
    public void testDescribeNullIdentity() {
        assertEquals("nothing", Assist.describeIdentity(null));
    }
    @Test
    public void testDescribeJavaLangIdentity() {
        assertEquals(S1.getClass().getSimpleName() + "@" + toHexString(identityHashCode(S1)) + "_" + toHexString(S1.hashCode()), Assist.describeIdentity(S1));
    }
    @Test
    public void testDescribeIdentity() {
        List<Byte> l = new ArrayList<>(); l.add((byte)0xCA);
        assertEquals(l.getClass().getName() + "@" + toHexString(identityHashCode(l)) + "_" + toHexString(l.hashCode()), Assist.describeIdentity(l));
    }
    @Test
    public void testDescribeShortIdentity() {
        I id = new I();
        assertEquals(id.getClass().getName() + "@" + toHexString(identityHashCode(id)), Assist.describeIdentity(id));
    }

    static class A extends ArrayList<Long> {}

    static class I {}
}