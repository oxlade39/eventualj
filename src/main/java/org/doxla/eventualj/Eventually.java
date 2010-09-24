package org.doxla.eventualj;

import static ch.lambdaj.proxy.ProxyUtil.createArgumentProxy;

public final class Eventually {

    static final State state = new State();

    private Eventually(){}

    public static <T> T eventually(T value) {
        sanityCheckState();

        Class<T> valueClass = (Class<T>) value.getClass();
        RecordingEventualContext<T> eventualContext = new RecordingEventualContext<T>(value);
        T proxy = createArgumentProxy(new MatchingIvocationInterceptor<T>(
                eventualContext), valueClass);
        state.setContext(eventualContext);
        return proxy;
    }

    private static <T> void sanityCheckState() {
        if(state.get() != null) throw new IllegalStateException("You must call a matcher on each eventual before creating a new eventual");
    }

    static final class State {

        final RecordingEventualContext get() {
            return context.get();
        }

        final RecordingEventualContext getAndClear() {
            RecordingEventualContext local = context.get();
            context.set(null);
            return local;
        }

        private void setContext(RecordingEventualContext toSet) {
            context.set(toSet);
        }

        private ThreadLocal<RecordingEventualContext> context = new ThreadLocal<RecordingEventualContext>();

    }


}
