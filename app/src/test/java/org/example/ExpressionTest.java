package org.example;

import org.example.command.Command;
import org.example.command.MacroCommand;
import org.example.command.StartMove;
import org.example.command.interpret.CommandExpression;
import org.example.command.interpret.Context;
import org.example.command.interpret.MacroCommandExpression;
import org.example.common.AdapterSourceCode;
import org.example.common.inmemorycompile.InMemoryCompile;
import org.example.common.ioc.DependencyResolver;
import org.example.common.ioc.IoC;
import org.example.common.ioc.ScopeResolver;
import org.example.objects.StartingObject;
import org.example.value.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExpressionTest {

    private final ConcurrentLinkedDeque<Command> queue = new ConcurrentLinkedDeque<>();
    private final ConcurrentHashMap<String, Object> gameObject = new ConcurrentHashMap<>();

    @Mock
    private Command move;

    private final String scopeId1 = "scopeId1";
    private final String scopeId2 = "scopeId2";

    void initDefaultScope() {
        registerIoCDefaultResolver();
        registerGameObject();
        registerGameQueue();
        registerAdapter();
        registerStartingObjectMethods();
        registerMoveCommand();
        registerStartMoveCommand();
    }

    /*
    {
      "id": "548",
      "action": "StartMove",
      "initialVelocity": 2
    }
    */
    @Test
    void expression_success() {
        initDefaultScope();
        var data = new HashMap<String, Object>();
        data.put("id", 548L);
        var args = new ArrayList<>();
        args.add(2);
        data.put("args", args);
        var context = new Context(data);

        var expression = new CommandExpression("StartMove");
        var command = expression.interpret(context);

        assertEquals(StartMove.class, command.getClass());
        command.execute();
        assertEquals(new Vector(2), gameObject.get("velocity"));
    }

    /*
    * {
          "id": "548",
          "action": "MacroCommand",
          "commands": [
            {
              "id": "548",
              "action": "StartMove",
              "initialVelocity": 2
            },
            {
              "id": "548",
              "action": "Move"
            }
          ]
        }
    * */
    @Test
    void expressionMacro_success() {
        initDefaultScope();
        var data = new HashMap<String, Object>();
        data.put("id", 548L);
        var args = new ArrayList<>();
        args.add(2);
        data.put("args", args);
        var context = new Context(data);

        var expression = new MacroCommandExpression(List.of(
            new CommandExpression("StartMove"),
            new CommandExpression("Move")
        ));
        var command = expression.interpret(context);

        assertEquals(MacroCommand.class, command.getClass());
        command.execute();
        assertEquals(new Vector(2), gameObject.get("velocity"));

        verify(move, times(1)).execute();
    }

    private void initDifferentScope() {
        registerIoCScopeResolver();
        registerScope(scopeId1);
        registerScope(scopeId2);
        switchScope(scopeId1);
        registerGameObject();
        registerGameQueue();
        registerAdapter();
        registerStartingObjectMethods();
        registerMoveCommand();
        registerStartMoveCommand();
        switchScope(scopeId2);
        registerGameObjectGamer2();
        registerGameQueue();
        registerAdapter();
        registerStartingObjectMethods();
        registerMoveCommand();
        registerStartMoveCommand();
    }

    /*
    {
      "id": "548",
      "action": "StartMove",
      "initialVelocity": 2
    }
    */
    @Test
    void expression_twoGamers_otherScope_success() {
        initDifferentScope();
        // gamer 1
        switchScope(scopeId1);
        var data = new HashMap<String, Object>();
        data.put("id", 548L);
        var args = new ArrayList<>();
        args.add(2);
        data.put("args", args);
        var context = new Context(data);

        var expression = new CommandExpression("StartMove");
        var command = expression.interpret(context);

        assertEquals(StartMove.class, command.getClass());
        command.execute();
        assertEquals(new Vector(2), gameObject.get("velocity"));

        // gamer 2
        switchScope(scopeId2);
        data = new HashMap<String, Object>();
        data.put("id", 548L);
        args = new ArrayList<>();
        args.add(4);
        data.put("args", args);
        context = new Context(data);
        expression = new CommandExpression("StartMove");
        command = expression.interpret(context);

        assertEquals(StartMove.class, command.getClass());
        command.execute();

        // Проверяем, что не изменилась начальная скорость объекта gamer1
        assertEquals(new Vector(2), gameObject.get("velocity"));
    }

    private void registerIoCDefaultResolver() {
        Map<String, Function<List<Object>, Object>> store = new ConcurrentHashMap<>();
        Function<List<Object>, Object> register = args ->
            store.put((String) args.get(0), (Function<List<Object>, Object>) args.get(1));
        store.put("IoC.Register", register);

        var dependencyVault = new DependencyResolver(store);
        IoC.setStrategy(
            dependencyVault::resolve
        );
    }

    private void registerGameObject() {
        var gameObjects = new ConcurrentHashMap<String, Object>();
        gameObjects.put("548", gameObject);
        Function<List<Object>, Object> saveGameObject = arg -> gameObjects.get(arg.get(0).toString());
        IoC.<Command>resolve(
            "IoC.Register",
            "GameObject",
            saveGameObject
        );
    }

    private void registerGameObjectGamer2() {
        var gameObjects = new ConcurrentHashMap<String, Object>();
        gameObjects.put("548", new ConcurrentHashMap<>());
        Function<List<Object>, Object> saveGameObject = arg -> gameObjects.get(arg.get(0).toString());
        IoC.<Command>resolve(
            "IoC.Register",
            "GameObject",
            saveGameObject
        );
    }

    private void registerGameQueue() {
        Function<List<Object>, Object> getGame = arg -> queue;
        IoC.<Command>resolve(
            "IoC.Register",
            "GetGame",
            getGame
        );
    }

    private void registerAdapter() {
        IoC.<Command>resolve(
            "IoC.Register",
            "Adapter",
            (Function<List<Object>, Object>) (List<Object> arg) -> {
                var typeClass = (Class<?>) arg.get(0);
                var adapterSourceCode = new AdapterSourceCode(typeClass);
                adapterSourceCode.generate();
                var inMemoryCompile = InMemoryCompile.getInstance();
                var clazz = inMemoryCompile.load(adapterSourceCode.getQualifiedClassName(), adapterSourceCode.getCode());
                try {
                    return clazz.getDeclaredConstructors()[0].newInstance(arg.get(1));
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }

    private void registerStartingObjectMethods() {
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.StartingObject:velocity.set",
            (Function<List<Object>, Command>) (List<Object> args) -> () -> ((Map<String, Object>) args.get(0)).put("velocity", args.get(1))
        );
    }

    private void registerMoveCommand() {
        IoC.<Command>resolve(
            "IoC.Register",
            "Move",
            (Function<List<Object>, Command>) (List<Object> arg) -> move
        );
    }

    private void registerStartMoveCommand() {
        Function<List<Object>, Command> startMove = args -> {
            var startObject = IoC.<StartingObject>resolve("Adapter", StartingObject.class, args.get(0));
            var initialVelocity = new Vector(Integer.parseInt(((List) args.get(1)).get(0).toString()));
            return new StartMove(startObject, initialVelocity);
        };
        IoC.<Command>resolve(
            "IoC.Register",
            "StartMove",
            startMove
        );
    }

    private void registerIoCScopeResolver() {
        var resolver = new ScopeResolver();
        IoC.setStrategy(
            resolver::resolve
        );
    }

    private void registerScope(String scopeId) {
        IoC.<Command>resolve("Scope.New", scopeId).execute();
        switchScope(scopeId);
    }

    private void switchScope(String scopeId) {
        IoC.<Command>resolve("Scope.Current", scopeId).execute();
    }
}
