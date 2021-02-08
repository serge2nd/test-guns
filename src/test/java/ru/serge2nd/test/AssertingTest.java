package ru.serge2nd.test;

import org.junit.Test;
import ru.serge2nd.test.util.Failure;
import ru.serge2nd.test.util.MultipleFailuresError;
import ru.serge2nd.test.util.ToRun;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static ru.serge2nd.test.Asserting.MESSAGES_END;
import static ru.serge2nd.test.Asserting.MESSAGES_START;
import static ru.serge2nd.test.Asserting.assertEach;
import static ru.serge2nd.test.Assist.catchThrowable;
import static ru.serge2nd.test.Env.EOL;

public class AssertingTest {
    static final Failure[] ERRORS = new Failure[] {
            new Failure(1, new AssertionError("sorry")),
            new Failure(3, new IOException("ha-ha"))
    };
    static final ToRun[] A = {
            () -> {},
            () -> {throw ERRORS[0].getError();},
            () -> {},
            () -> {throw ERRORS[1].getError();}
    };

    static final String H = "not so bad";
    static final String TOTAL = " (2 failures, indexes 1, 3)" + EOL;
    static final String MSGS = stream(ERRORS)
            .map(e -> format("[%d] %s: %s%s", e.getIdx(), e.getTypeName(), e.getMessage(), EOL))
            .collect(joining("", MESSAGES_START, MESSAGES_END));

    @Test
    public void testArray()             { testAssertEachCommon(() -> assertEach(A), null); }
    @Test
    public void testArrayWithTitle()    { testAssertEachCommon(() -> assertEach(H, A), H); }
    @Test
    public void testIterable()          { testAssertEachCommon(() -> assertEach(asList(A)), null); }
    @Test
    public void testIterableWithTitle() { testAssertEachCommon(() -> assertEach(H, asList(A)), H); }
    @Test
    public void testStream()            { testAssertEachCommon(() -> assertEach(Stream.of(A)), null); }
    @Test
    public void testStreamWithTitle()   { testAssertEachCommon(() -> assertEach(H, Stream.of(A)), H); }

    @Test @SuppressWarnings("ConstantConditions")
    public void testNullAssertion() {
        Throwable thrown = null; try {
            assertEach(Stream.of((ToRun)null));
        } catch (Throwable t) { thrown = t; }

        assertNotNull(thrown); thrown.printStackTrace(); err.println();
        assertSame(MultipleFailuresError.class, thrown.getClass());

        List<Failure> suppressed = ((MultipleFailuresError)thrown).suppressed();
        assertEquals(1, suppressed.size());

        assertEquals(0, suppressed.get(0).getIdx().intValue());
        assertSame(NullPointerException.class, suppressed.get(0).getError().getClass());
    }

    static void testAssertEachCommon(Runnable assertEach, String title) {
        Throwable thrown = catchThrowable(assertEach::run);

        assertNotNull(thrown); thrown.printStackTrace(); err.println();
        assertSame(MultipleFailuresError.class, thrown.getClass());

        assertThat(thrown.getMessage(), startsWith((title != null ? title : "") + TOTAL));
        assertThat(thrown.getMessage(), containsString(MSGS));
    }
}