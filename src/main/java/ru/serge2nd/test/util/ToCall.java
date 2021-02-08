package ru.serge2nd.test.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static java.lang.String.valueOf;
import static ru.serge2nd.test.util.ToRun.throwSneaky;

@FunctionalInterface
public interface ToCall<R> extends ToRun, Callable<R>, Supplier<R> {

    R _call() throws Throwable;

    default void _run() throws Throwable { _call(); }

    default R call() { try {return _call();} catch(Throwable t) {throwSneaky(t);} return null; }

    default R get()  { return call(); }

    @SuppressWarnings("unchecked,rawtypes")
    default Iterable<R> asIterable(int size) { return () -> (Iterator<R>)new Iterator() {
        int i = 0;
        public boolean hasNext() { return i < size; }
        public Object next() {
            if (i >= size) throw new NoSuchElementException(valueOf(i));
            try { return _call(); }
            catch (Throwable t) { return t; }
            finally { i++; }
        }
    };}

    static <R> ToCall<R> of(Supplier<? extends R> s) { return s::get; }
}
