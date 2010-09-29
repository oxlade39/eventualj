package org.doxla.eventualj;

import org.doxla.eventualj.matcher.EventuallyMatcher;

import static org.doxla.eventualj.Eventually.state;

public final class EventuallyMatchers {

    private EventuallyMatchers(){}

    public static <T> EventuallyMatcher<T> willReturn(final T expected) {
        return new EventuallyMatcher<T>(expected, state.getAndClear());
    }

    public static <T> EventuallyMatcher<T> willBe(final T expected) {
        return new EventuallyMatcher<T>(expected, state.getAndClear());
    }
}
