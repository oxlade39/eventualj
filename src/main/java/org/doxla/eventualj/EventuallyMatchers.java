package org.doxla.eventualj;

import org.hamcrest.Matcher;

import static org.doxla.eventualj.Eventually.state;

public final class EventuallyMatchers {

    private EventuallyMatchers(){}

    public static <T> Matcher<T> willReturn(final T expected) {
        return new EventuallyMatcher<T>(expected, state.getAndClear());
    }

    public static <T> Matcher<T> willBe(final T expected) {
        return new EventuallyMatcher<T>(expected, state.getAndClear());
    }
}
