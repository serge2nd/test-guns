package ru.serge2nd.test.util;

@FunctionalInterface
public interface ToRun extends Runnable {

    void _run() throws Throwable;

    default void run() { try {_run();} catch(Throwable t) {throwSneaky(t);} }

    @SuppressWarnings("unchecked")
    static <T extends Throwable> void throwSneaky(Throwable t) throws T { throw (T)t; }
}
