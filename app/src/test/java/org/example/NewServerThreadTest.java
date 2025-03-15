package org.example;

import org.example.command.NewServerThread;
import org.example.common.ioc.IoC;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class NewServerThreadTest {

    private final Map<String, Object> store = new ConcurrentHashMap<>();

    @Test
    void newServerThread_success() {
        registerIoCDefaultResolver();
        var newServerThread = new NewServerThread(1L);
        newServerThread.execute();

        assertEquals(1, store.size());
        var register = store.get("IoC.Register");
        assertEquals(2, ((List) register).size());
    }

    private void registerIoCDefaultResolver() {
        Function<List<Object>, Object> register = args ->
            store.put((String) args.get(0), args.get(1));
        store.put("IoC.Register", register);

        BiFunction<String, List<Object>, Object> strategy = (k, v) -> store.put(k, v);
        IoC.setStrategy(strategy);
    }
}
