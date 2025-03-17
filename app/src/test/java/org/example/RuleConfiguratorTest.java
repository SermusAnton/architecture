package org.example;

import org.example.command.Command;
import org.example.command.Rotate;
import org.example.command.ShotCommand;
import org.example.command.dynamic.LoadCommand;
import org.example.command.rule.RuleConfigurator;
import org.example.common.AdapterSourceCode;
import org.example.common.inmemorycompile.InMemoryCompile;
import org.example.common.ioc.DependencyResolver;
import org.example.common.ioc.IoC;
import org.example.objects.RotatingObject;
import org.example.objects.ShootingObject;
import org.example.value.Fuel;
import org.example.value.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RuleConfiguratorTest {
    private final ConcurrentLinkedDeque<Command> queue = new ConcurrentLinkedDeque<>();
    private final ConcurrentHashMap<String, Object> gameObject = new ConcurrentHashMap<>();
    private final Map<String, Function<List<Object>, Object>> store = new ConcurrentHashMap<>();
    private final Map<String, String> nameToCommand = new ConcurrentHashMap<>();
    private final Map<String, List<Function<List<Object>, Command>>> objectCommandAbility = new ConcurrentHashMap<>();

    private final String nameJar = "movelib-0.1-plain.jar";
    private final URL pathToJar = RuleConfiguratorTest.class.getClassLoader().getResource(nameJar);

    @Test
    void configure_success() {
        initDefaultScope();
        initNameToCommand();
        initCommandRule();
        initObjectAbility();
        var objectAbility = Map.of("Корабль", List.of("Движение", "Поворот", "Выстрел"));
        var macroCommands = Map.of("Движение", List.of("Проверка топлива", "Move", "Сжигание топлива"));
        var configurator = new RuleConfigurator(objectAbility, macroCommands);
        configurator.execute();

        //FuelCheckingObject
        gameObject.put("reserve", new Fuel(100));
        gameObject.put("consumption", new Fuel(10));
        //MovingObject
        gameObject.put("velocity", new Vector(1, 1));
        gameObject.put("location", new Vector(3, 3));

        var macroCommand = store.get("Move");
        var command = (Command) macroCommand.apply(List.of(gameObject));
        command.execute();

        assertEquals(new Fuel(90), gameObject.get("fuelreserve"));
        assertEquals(new Vector(4, 4), gameObject.get("location"));
    }

    void initDefaultScope() {
        registerIoCDefaultResolver();
        registerGameObject();
        registerGameQueue();
        registerAdapter();
        loadNewCommand();
        registerFuelCheckingObjectMethods();
        registerFuelBurningObjectMethods();
        registerMovingObjectMethods();
        registerRotatingObjectMethods();
        registerShootingObjectMethods();

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

    private void registerFuelCheckingObjectMethods() {
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.FuelCheckingObject:reserve.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("reserve")
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.FuelCheckingObject:consumption.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("consumption")
        );
    }

    private void registerMovingObjectMethods() {
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.MovingObject:location.set",
            (Function<List<Object>, Command>) (List<Object> args) -> () -> ((Map<String, Object>) args.get(0)).put("location", args.get(1))
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.MovingObject:velocity.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("velocity")
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.MovingObject:location.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("location")
        );
    }

    private void registerFuelBurningObjectMethods() {
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.FuelBurningObject:fuelreserve.set",
            (Function<List<Object>, Command>) (List<Object> args) -> () -> ((Map<String, Object>) args.get(0)).put("fuelreserve", args.get(1))
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.FuelBurningObject:reserve.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("reserve")
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.FuelBurningObject:consumption.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("consumption")
        );
    }

    private void registerRotatingObjectMethods() {
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.RotatingObject:angle.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("angle")
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.RotatingObject:anglevelocity.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("anglevelocity")
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.RotatingObject:angle.set",
            (Function<List<Object>, Command>) (List<Object> args) -> () -> ((Map<String, Object>) args.get(0)).put("angle", args.get(1))
        );
        Function<List<Object>, Command> rotate = (List<Object> args) -> {
            var rotatingObject = IoC.<RotatingObject>resolve("Adapter", RotatingObject.class, args.get(0));
            return new Rotate(rotatingObject);
        };
        IoC.<Command>resolve(
            "IoC.Register",
            "Rotate",
            rotate
        );
    }

    private void registerShootingObjectMethods() {
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.ShootingObject:location.set",
            (Function<List<Object>, Command>) (List<Object> args) -> () -> ((Map<String, Object>) args.get(0)).put("location", args.get(1))
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.ShootingObject:velocity.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("velocity")
        );
        IoC.<Command>resolve(
            "IoC.Register",
            "Spaceship.Operations.ShootingObject:location.get",
            (Function<List<Object>, Object>) (List<Object> args) -> ((Map<String, Object>) args.get(0)).get("location")
        );
        Function<List<Object>, Command> shot = (List<Object> args) -> {
            var shotingObject = IoC.<ShootingObject>resolve("Adapter", ShootingObject.class, args.get(0));
            return new ShotCommand(shotingObject);
        };
        IoC.<Command>resolve(
            "IoC.Register",
            "ShotCommand",
            shot
        );
    }

    private void loadNewCommand() {
        assert pathToJar != null;
        var loadCommand = new LoadCommand(pathToJar.getPath());
        loadCommand.execute();
    }

    private void initNameToCommand() {
        nameToCommand.put("Проверка топлива", "CheckFuel");
        nameToCommand.put("Движение", "Move");
        nameToCommand.put("Сжигание топлива", "BurnFuel");
        nameToCommand.put("Поворот", "Rotate");
        nameToCommand.put("Выстрел", "ShortCommand");
    }

    private void initCommandRule() {
        IoC.<Command>resolve(
            "IoC.Register",
            "GetCommandByName",
            (Function<List<Object>, Object>) (List<Object> args) -> {
                var name = (String) args.get(0);
                var alias = nameToCommand.get(name);
                if (Objects.isNull(alias)) {
                    return store.get(name);
                }
                return store.get(alias);
            }
        );

        IoC.<Command>resolve(
            "IoC.Register",
            "SetCommandByName",
            (Function<List<Object>, Object>) (List<Object> args) -> {
                var name = (String) args.get(0);
                var alias = nameToCommand.get(name);
                if (Objects.isNull(alias)) {
                    return store.put(name, (Function<List<Object>, Object>) args.get(1));
                }
                return store.put(alias, (Function<List<Object>, Object>) args.get(1));
            }
        );
    }

    private void initObjectAbility() {
        IoC.<Command>resolve(
            "IoC.Register",
            "SetAvailableCommandByType",
            (Function<List<Object>, Object>) (List<Object> args) -> {
                var name = (String) args.get(0);
                    return objectCommandAbility.put(name, (List<Function<List<Object>, Command>>) args.get(1));
            }
        );
    }
}
