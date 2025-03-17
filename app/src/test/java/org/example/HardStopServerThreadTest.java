package org.example;

import org.example.command.HardStopServerThread;
import org.example.common.ioc.IoC;
import org.example.common.polling.ServerThread;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HardStopServerThreadTest {

    @Mock
    ServerThread serverThread;

    private final Map<String, Object> store = new ConcurrentHashMap<>();

    @Test
    void hardStopServerThread_execute_success() {
        registerIoCDefaultResolver();
        var id = 1L;
        store.put(String.format("Game%s.ServerThread", id), serverThread);
        var hardStopServerThread = new HardStopServerThread(1L);
        hardStopServerThread.execute();

        verify(serverThread, times(1)).hardStop();
    }

    private void registerIoCDefaultResolver() {
        Function<List<Object>, Object> register = args ->
            store.put((String) args.get(0), args.get(1));
        store.put("IoC.Register", register);

        BiFunction<String, List<Object>, Object> strategy = (k, v) -> store.put(k, v);
        IoC.setStrategy(strategy);
    }
}
