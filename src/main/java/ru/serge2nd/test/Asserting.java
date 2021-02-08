package ru.serge2nd.test;

import ru.serge2nd.test.util.Failure;
import ru.serge2nd.test.util.MultipleFailuresError;
import ru.serge2nd.test.util.ToRun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static ru.serge2nd.test.Assist.errNotInstantiable;
import static ru.serge2nd.test.Env.EOL;
import static ru.serge2nd.test.Env.EOL2;
import static ru.serge2nd.test.Env.STK_TRC_TOP;
import static ru.serge2nd.test.Env.SUB_STK_TRC_TOP;

@SuppressWarnings("StringBufferReplaceableByString")
public class Asserting {
    private Asserting() { throw errNotInstantiable(lookup()); }

    //region assertEach() overloads

    public static void assertEach(ToRun... assertions)               { assertEach(null, asList(assertions).iterator()); }
    public static void assertEach(String title, ToRun... assertions) { assertEach(title, asList(assertions).iterator()); }

    public static void assertEach(Iterable<? extends ToRun> assertions)               { assertEach(null, assertions.iterator()); }
    public static void assertEach(String title, Iterable<? extends ToRun> assertions) { assertEach(title, assertions.iterator()); }

    public static void assertEach(Stream<? extends ToRun> assertions)               { assertEach(null, assertions.iterator()); }
    public static void assertEach(String title, Stream<? extends ToRun> assertions) { assertEach(title, assertions.iterator()); }

    public static void assertEach(String title, Iterator<? extends ToRun> assertions) {
        List<Failure> failures = run(assertions);

        if (!failures.isEmpty()) {
            String summary       = extractSummary(failures);
            String stackTraceTop = extractStackTrace(new Throwable(), NOT_THIS, STK_TRC_TOP, Asserting::stackTraceSuffix);
            String msgs          = joinMessages(failures);
            String stackTraces   = joinStackTraces(failures);

            String fullMsg = new StringBuilder((title != null ? title.length() : 0) + summary.length() + stackTraceTop.length() + msgs.length() + stackTraces.length() + 64)
                    .append(title != null ? title : "").append(' ')
                    .append(summary).append(EOL)
                    .append(stackTraceTop).append(EOL)
                    .append(msgs)
                    .append(stackTraces)
                    .append(">>> SEE FULL STACKTRACE BELOW...")
                    .toString();

            throw new MultipleFailuresError(fullMsg).suppresses(failures);
        }
    }
    //endregion

    //region Hidden helpers

    static List<Failure> run(Iterator<? extends ToRun> it) {
        List<Failure> failures = new ArrayList<>();
        for (int i = 0; it.hasNext(); i++) { try {
            requireNonNull(it.next(), "assertion must not be null")._run();
        } catch (Throwable t) {
            failures.add(new Failure(i, t));
        }}
        return failures;
    }

    static String extractSummary(List<Failure> failures) {
        int n = failures.size();
        return failures.stream()
                .map(f -> f.getIdx().toString())
                .collect(joining(", ", "(" + n + (n == 1 ? " failure" : " failures") + ", indexes ", ")"));
    }

    static String extractStackTrace(Throwable t, Predicate<StackTraceElement> filter, int top, BiFunction<Integer, Integer, String> suffix) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        int len = stackTrace.length;

        return stream(stackTrace)
                .filter(filter).limit(top)
                .map(e -> "\tat " + e)
                .collect(joining(EOL, "", suffix.apply(len, top)));
    }

    static String joinMessages(List<Failure> failures) {
        return failures.stream()
                .map(Failure::toString)
                .collect(joining("", MESSAGES_START, MESSAGES_END));
    }

    static String joinStackTraces(List<Failure> failures) {
        return failures.stream()
                .flatMap(f -> Stream.of(
                    STACKTRACE_START.replace(N, valueOf(f.getIdx())),
                    extractStackTrace(f.getError(), ALL, SUB_STK_TRC_TOP, Asserting::subStackTraceSuffix),
                    STACKTRACE_END.replace(N, valueOf(f.getIdx()))))
                .collect(joining());
    }

    static String stackTraceSuffix(int total, int top)    { return total > top ? STK_TRC_SUFFIX.replace(N, valueOf(total - top)) : ""; }
    static String subStackTraceSuffix(int total, int top) { return total > top ? SUB_STK_TRC_SUFFIX.replace(N, valueOf(total - top)) : ""; }
    //endregion

    static final String N = "#";

    static final String STK_TRC_SUFFIX     = EOL + "\t... (" + N + " frames more - see at the end)" + EOL;
    static final String SUB_STK_TRC_SUFFIX = EOL + "\t... (" + N + " frames more)" + EOL;

    static final String MESSAGES_START    = "--- MESSAGES ---"                 + EOL;
    static final String MESSAGES_END      = "--- END MESSAGES ---"             + EOL2;
    static final String STACKTRACE_START  = "--- STACKTRACE "     + N + " ---" + EOL;
    static final String STACKTRACE_END    = "--- END STACKTRACE " + N + " ---" + EOL2;

    static final Predicate<StackTraceElement> NOT_THIS = e -> !e.getClassName().equals(Asserting.class.getName());
    static final Predicate<StackTraceElement> ALL      = e -> true;
}
