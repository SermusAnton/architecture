package org.example.common;

import java.util.List;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public class IoC {

    private IoC() {
    }

    private static BiFunction<String, List<Object>, Object> strategy;

    public static <T> T resolve(String key, Object... args) {
        return (T) strategy.apply(key, List.of(args));
    }

    public static void setStrategy(BiFunction<String, List<Object>, Object> newValue) {
        strategy = newValue;
    }
}
