package org.doxla.eventualj;

import com.google.code.tempusfugit.temporal.Condition;
import org.hamcrest.Matcher;

class EventualCondition<T> implements Condition {
    private final Matcher<T> expected;
    private final RecordingEventualContext eventualContext;

    public static <T> EventualCondition<T> condition(Matcher<T> expected, RecordingEventualContext<T> context) {
        return new EventualCondition<T>(expected, context);
    }

    private EventualCondition(Matcher<T> expected, RecordingEventualContext eventualContext) {
        this.expected = expected;
        this.eventualContext = eventualContext;
    }

    public boolean isSatisfied() {
        return expected.matches(eventualContext.tryMethod());
    }
}
