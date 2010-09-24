package org.doxla.eventualj;

import ch.lambdaj.proxy.InvocationInterceptor;

import java.lang.reflect.Method;

class MatchingIvocationInterceptor<T> extends InvocationInterceptor {
    private final RecordingEventualContext<T> recordingEventualContext;

    public MatchingIvocationInterceptor(RecordingEventualContext<T> tRecordingEventualContext) {
        recordingEventualContext = tRecordingEventualContext;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        recordingEventualContext.record(proxy, method, args);
        return null;
    }
}
