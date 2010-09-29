package org.doxla.eventualj.matcher;

import com.google.code.tempusfugit.temporal.Condition;
import org.doxla.eventualj.RecordingEventualContext;

class EventualCondition<T> implements Condition {
    private final T expected;
    private final RecordingEventualContext eventualContext;

    public static <T> EventualCondition<T> condition(T expected, RecordingEventualContext<T> context) {
        return new EventualCondition<T>(expected, context);
    }

    private EventualCondition(T expected, RecordingEventualContext eventualContext) {
        this.expected = expected;
        this.eventualContext = eventualContext;
    }

    public boolean isSatisfied() {
        return expected.equals(eventualContext.tryMethod());
    }
}
