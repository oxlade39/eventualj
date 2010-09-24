package org.doxla.eventualj;

import ch.lambdaj.proxy.ProxyUtil;
import org.hamcrest.Matcher;

import static com.google.code.tempusfugit.temporal.Timeout.timeout;
import static com.google.code.tempusfugit.temporal.WaitFor.waitOrTimeout;
import static java.lang.String.format;

public class Eventually {

    private static RecordingEventualContext context;

    public static <T> T eventually(T value) {
        if(context != null) throw new IllegalStateException("You must call a matcher on each eventual before creating a new eventual");

        Class<T> valueClass = (Class<T>) value.getClass();
        RecordingEventualContext<T> eventualContext = new RecordingEventualContext<T>(value);
        T proxy = ProxyUtil.createArgumentProxy(new MatchingIvocationInterceptor<T>(
                eventualContext), valueClass);
        context = eventualContext;
        return proxy;
    }

    public static <T> Matcher<T> willReturn(final T expected) {
        return new EventuallyMatcher<T>(expected, getAndClear());
    }

    public static <T> Matcher<T> willBe(final T expected) {
        return new EventuallyMatcher<T>(expected, getAndClear());
    }

    public static RecordingEventualContext getAndClear() {
        RecordingEventualContext local = context;
        context = null;
        return local;
    }


}
