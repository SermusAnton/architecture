package org.example;

import org.example.command.Command;
import org.example.command.Interpret;
import org.example.command.MacroCommand;
import org.example.command.Move;
import org.example.command.Rotate;
import org.example.common.ioc.DependencyResolver;
import org.example.common.ioc.IoC;
import org.example.dto.CommandType;
import org.example.value.Angle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InterpretTest {

    private final Map<String, Function<List<Object>, Object>> store = new ConcurrentHashMap<>();

    @Mock
    Move move;
    @Mock
    Rotate rotate;

    @Test
    void interpret_success() {
        Map<CommandType, List<Object>> packet = Map.of(CommandType.MOVE, List.of(),
            CommandType.ROTATE, List.of(new Angle(1)));
        Queue<Command> game = new ArrayDeque<>();
        Object gameObject = new Object();
        Interpret interpret = new Interpret(packet, game, gameObject);

        registerIoCDefaultResolver();
        registerCommands();

        interpret.execute();

        assertEquals(1, game.size());
        var command = game.peek();
        assert command != null;
        assertEquals(MacroCommand.class, command.getClass());
    }

    private void registerIoCDefaultResolver() {
        Function<List<Object>, Object> register = args ->
            store.put((String) args.get(0), (Function<List<Object>, Object>) args.get(1));
        store.put("IoC.Register", register);

        var dependencyVault = new DependencyResolver(store);
        IoC.setStrategy(
            dependencyVault::resolve
        );
    }

    private void registerCommands() {
        Function<List<Object>, Object> moveCommand = arg -> move;
        IoC.<Command>resolve(
            "IoC.Register",
            CommandType.MOVE.toString(),
            moveCommand
        );

        Function<List<Object>, Object> rotateCommand = arg -> rotate;
        IoC.<Command>resolve(
            "IoC.Register",
            CommandType.ROTATE.toString(),
            rotateCommand
        );
    }
}
