package org.example;

import org.example.command.Command;
import org.example.common.ioc.IoC;
import org.example.dto.PacketCommandDto;
import org.example.service.CommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

    @Mock
    PacketCommandDto packetCommandDto;
    @Mock
    Queue<Command> queue;
    @Mock
    Object gameObject;

    private final Map<String, Object> store = new ConcurrentHashMap<>();

    @Test
    void create_success() {
        registerIoCDefaultResolver();
        var gameId = 1L;
        when(packetCommandDto.getGameId()).thenReturn(gameId);
        store.put(String.format("Game%s.Deque", gameId), queue);
        var gameObjectId = 10L;
        when(packetCommandDto.getGameObjectId()).thenReturn(gameObjectId);
        store.put("Game Object", gameObject);

        var commandService = new CommandService();
        commandService.create(packetCommandDto);
        verify(queue, times(1)).add(any());
    }

    private void registerIoCDefaultResolver() {
        Function<List<Object>, Object> register = args ->
            store.put((String) args.get(0), args.get(1));
        store.put("IoC.Register", register);

        BiFunction<String, List<Object>, Object> strategy = (k, v) -> store.put(k, v);
        IoC.setStrategy(strategy);
    }
}
