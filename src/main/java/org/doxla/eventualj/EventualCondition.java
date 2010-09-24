package org.doxla.eventualj;

import com.google.code.tempusfugit.temporal.Condition;

class EventualCondition<T> implements Condition {
    private final T expected;
    private final RecordingEventualContext eventualContext;

    public static <T> EventualCondition<T> condition(T expected, RecordingEventualContext<T> context) {
        return new EventualCondition<T>(expected, context);
    }

    EventualCondition(T expected, RecordingEventualContext eventualContext) {
        this.expected = expected;
        this.eventualContext = eventualContext;
    }

    public boolean isSatisfied() {
        Object valueAtThisPointInTime = eventualContext.tryMethod();
        return expected.equals(valueAtThisPointInTime);
    }
}
