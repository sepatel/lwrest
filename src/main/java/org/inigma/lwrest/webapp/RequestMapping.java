package org.inigma.lwrest.webapp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RequestMapping {
    private static final Pattern PATH_PATTERN = Pattern.compile("/\\{(\\w+?)\\}");
    private static final String PATH_VARIABLE = "/(\\\\w+?)";
    private final Method method;
    private final Pattern pattern;
    private final List<String> pathVariables;

    RequestMapping(Method method, String pattern) {
        this.method = method;
        this.pathVariables = new ArrayList<String>();
        StringBuffer sb = new StringBuffer("^");
        Matcher matcher = PATH_PATTERN.matcher(pattern);
        while (matcher.find()) {
            this.pathVariables.add(matcher.group(1));
            matcher.appendReplacement(sb, PATH_VARIABLE);
        }
        matcher.appendTail(sb);
        sb.append("$");
        this.pattern = Pattern.compile(sb.toString());
    }

    public Method getMethod() {
        return method;
    }

    public String getPattern() {
        return pattern.pattern();
    }

    public PathParameters matches(String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            PathParameters pp = new PathParameters();
            for (int i = 0; i < pathVariables.size(); i++) {
                pp.set(pathVariables.get(i), matcher.group(i + 1));
            }
            return pp;
        }
        return null;
    }
}
