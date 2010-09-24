package org.doxla.eventualj;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class RecordingEventualContext<T> {
    private final T realSystemUnderTest;
    private T proxySystemUnderTest;
    private Method methodUnderTest;
    private Object[] args;

    RecordingEventualContext(T realSystemUnderTest) {
        this.realSystemUnderTest = realSystemUnderTest;
    }

    public Object tryMethod() {
        try {
            return methodUnderTest.invoke(realSystemUnderTest, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void record(Object proxy, Method method, Object[] args) {
        if (!method.getName().equals("toString")) {
            proxySystemUnderTest = (T) proxy;
            methodUnderTest = method;
            this.args = args;
        }
    }
}
