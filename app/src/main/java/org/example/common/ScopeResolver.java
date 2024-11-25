package org.example.common;

import java.util.List;
import java.util.Objects;

public class ScopeResolver implements Resolver {

    @Override
    public Object resolve(String key, List<Object> args) {
        var resolver = Scope.getCurrent();
        if (Objects.isNull(resolver)) {
            throw new IllegalCallerException();
        }
        return resolver.resolve(key, args);
    }
}
