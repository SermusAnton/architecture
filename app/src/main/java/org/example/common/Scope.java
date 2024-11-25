package org.example.common;

import org.example.command.Command;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class Scope {

    private static final ThreadLocal<String> currentScope = new ThreadLocal<>();

    private static final Map<String, Function<List<Object>, Object>> defaultStore = new ConcurrentHashMap<>();

    static {
        defaultStore.put("Scope.New",
            args -> create((String) args.get(0)));
        defaultStore.put("Scope.Current",
            args -> setCurrentScopeId((String) args.get(0)));
        defaultStore.put("IoC.Register",
            args -> defaultStore.put((String) args.get(0), (Function<List<Object>, Object>) args.get(1)));
    }

    private static final Map<String, Resolver> resolvers = new ConcurrentHashMap<>();

    public static Command create(String id) {
        Map<String, Function<List<Object>, Object>> store = new ConcurrentHashMap<>(defaultStore);
        store.put("IoC.Register",
            args -> store.put((String) args.get(0), (Function<List<Object>, Object>) args.get(1)));
        return () -> resolvers.put(id, new DependencyResolver(store));
    }

    public static Command setCurrentScopeId(String id) {
        return () -> currentScope.set(id);
    }

    public static Resolver getCurrent() {
        var id = getCurrentScopeId();
        return resolvers.getOrDefault(id, new DependencyResolver(defaultStore));
    }

    private static String getCurrentScopeId() {
        if (Objects.nonNull(currentScope.get())) {
            return currentScope.get();
        }
        return "root";
    }

    public void unload() {
        currentScope.remove();
    }
}