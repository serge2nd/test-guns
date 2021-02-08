package ru.serge2nd.test.util;

import org.hamcrest.Description;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface Alert<T> extends BiConsumer<T, Description> {
    default Alert<T> andThen(Alert<? super T> after) {
        return BiConsumer.super.andThen(after)::accept;
    }
}
