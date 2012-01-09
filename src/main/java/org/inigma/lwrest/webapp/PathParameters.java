package org.inigma.lwrest.webapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PathParameters {
    private Map<String, String> params = new HashMap<String, String>();

    public boolean contains(String key) {
        return params.containsKey(key);
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        if (!params.containsKey(key)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(params.get(key));
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        if (!params.containsKey(key)) {
            return defaultValue;
        }
        return Integer.parseInt(params.get(key));
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        if (!params.containsKey(key)) {
            return defaultValue;
        }
        return params.get(key);
    }

    public Set<String> keys() {
        return params.keySet();
    }
}
