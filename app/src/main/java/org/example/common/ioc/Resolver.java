package org.example.common.ioc;

import java.util.List;

public interface Resolver {
    Object resolve(String key, List<Object> args);
}
