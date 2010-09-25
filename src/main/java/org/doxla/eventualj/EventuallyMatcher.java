package org.doxla.eventualj;

import com.google.code.tempusfugit.temporal.Duration;
import com.google.code.tempusfugit.temporal.Timeout;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.concurrent.TimeoutException;

import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static com.google.code.tempusfugit.temporal.WaitFor.waitOrTimeout;
import static java.lang.String.format;
import static org.doxla.eventualj.EventualCondition.condition;

class EventuallyMatcher<T> extends BaseMatcher<T> {
    private final T expected;
    private final RecordingEventualContext eventualContext;
    private Duration timeout;

    EventuallyMatcher(T expected, RecordingEventualContext<T> eventualContext) {
        this(expected, eventualContext, seconds(1L));
    }

    EventuallyMatcher(T expected, RecordingEventualContext<T> eventualContext, Duration timeout) {
        this.expected = expected;
        this.eventualContext = eventualContext;
        this.timeout = timeout;
    }

    public EventuallyMatcher<T> within(Duration duration) {
        this.timeout = duration;
        return this;
    }

    public boolean matches(Object o) {
        TimeoutException timedout = null;
        try {
            waitOrTimeout(condition(expected, eventualContext), timeout);
        } catch (TimeoutException e) {
            timedout = e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return timedout == null;
    }

    public void describeTo(Description description) {
        description.appendText(format("Timeout occurred before %s was returned", expected));
    }
}
