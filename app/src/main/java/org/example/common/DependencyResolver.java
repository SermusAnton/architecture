package org.example.common;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DependencyResolver implements Resolver {

    private final Map<String, Function<List<Object>, Object>> store;

    public DependencyResolver(Map<String, Function<List<Object>, Object>> store) {
        this.store = store;
    }

    @Override
    public Object resolve(String key, List<Object> args) {
        var strategy = store.get(key);
        return strategy.apply(args);
    }
}