package org.example.common;

import java.util.List;

public interface Resolver {
    Object resolve(String key, List<Object> args);
}
