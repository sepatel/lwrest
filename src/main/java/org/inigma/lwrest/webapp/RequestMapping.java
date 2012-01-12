package org.inigma.lwrest.webapp;

import java.lang.reflect.Method;

class RequestMapping {
    private final Method method;
    private final String pattern;

    RequestMapping(Method method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public Method getMethod() {
        return method;
    }

    public String getPattern() {
        return pattern;
    }
}
