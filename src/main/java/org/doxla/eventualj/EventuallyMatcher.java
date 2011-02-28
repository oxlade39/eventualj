package org.doxla.eventualj;

import com.google.code.tempusfugit.temporal.Duration;
import com.google.code.tempusfugit.temporal.Timeout;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static com.google.code.tempusfugit.temporal.WaitFor.waitOrTimeout;
import static java.lang.String.format;
import static org.doxla.eventualj.EventualCondition.condition;
import static org.hamcrest.core.IsEqual.equalTo;

class EventuallyMatcher<T> extends BaseMatcher<T> {
    private final Matcher<T> expected;
    private final RecordingEventualContext<T> eventualContext;
    private Duration timeout;

    EventuallyMatcher(T expected, RecordingEventualContext<T> eventualContext) {
        this(equalTo(expected), eventualContext, seconds(1L));
    }

    EventuallyMatcher(T expected, RecordingEventualContext<T> eventualContext, Duration timeout) {
        this(equalTo(expected), eventualContext, timeout);
    }

    EventuallyMatcher(Matcher<T> subMatcher, RecordingEventualContext<T> eventualContext) {
        this(subMatcher, eventualContext, seconds(1L));
    }

    EventuallyMatcher(Matcher<T> subMatcher, RecordingEventualContext<T> eventualContext, Duration timeout) {
        this.expected = subMatcher;
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
