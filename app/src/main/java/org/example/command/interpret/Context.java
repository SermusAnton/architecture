package org.example.command.interpret;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Context {
    private final Map<String, Object> data;

    public Context(Map<String, Object> data) {
        this.data = data;
    }

    public Object getDataBy(String key) {
        return Optional.ofNullable(data.get(key))
            .orElseThrow(() ->
                new IllegalArgumentException(String.format("Key %s not found", key)));
    }


    public Set<Map.Entry<String, Object>> entry() {
        return data.entrySet();
    }
}
