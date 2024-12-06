package org.example;

import org.example.command.Command;
import org.example.command.Move;
import org.example.command.Rotate;
import org.example.common.ioc.DependencyResolver;
import org.example.common.ioc.IoC;
import org.example.common.ioc.ScopeResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IoCTest {

    @Mock
    Move move;

    @Mock
    Rotate rotate;

    //п.2 Расширяемая фабрика и IoC
    @Test
    void setVault_success() {
        Map<String, Function<List<Object>, Object>> store = new ConcurrentHashMap<>();

        Function<List<Object>, Object> register = args ->
            store.put((String) args.get(0), (Function<List<Object>, Object>) args.get(1));
        store.put("IoC.Register", register);

        var dependencyVault = new DependencyResolver(store);
        IoC.setStrategy(
            dependencyVault::resolve
        );

        IoC.<Command>resolve(
            "IoC.Register",
            "Commands.Move",
            (Function<List<Object>, Command>) (List<Object> arg) -> move
        );

        var cmd = IoC.<Command>resolve("Commands.Move");

        cmd.execute();

        verify(move, times(1)).execute();
    }

    //п.2 Расширяемая фабрика и IoC
    @Test
    void registerAndResolve_success() {
        Map<String, Function<List<Object>, Object>> store = new ConcurrentHashMap<>();

        Function<List<Object>, Object> register = args ->
            store.put((String) args.get(0), (Function<List<Object>, Object>) args.get(1));
        store.put("IoC.Register", register);

        var resolver = new DependencyResolver(store);
        IoC.setStrategy(
            resolver::resolve
        );

        IoC.<Command>resolve(
            "IoC.Register",
            "Commands.Move",
            (Function<List<Object>, Command>) (List<Object> arg) -> move
        );

        IoC.<Command>resolve(
            "IoC.Register",
            "Commands.Rotate",
            (Function<List<Object>, Command>) (List<Object> arg) -> rotate
        );

        var cmd = IoC.<Command>resolve("Commands.Move");

        cmd.execute();

        verify(move, times(1)).execute();
        verify(rotate, never()).execute();
    }

    //п.3 Расширяемая фабрика и IoC
    @Test
    void currentScopeNotSet_useDefault() {
        var resolver = new ScopeResolver();
        IoC.setStrategy(
            resolver::resolve
        );

        IoC.<Command>resolve(
            "IoC.Register",
            "Commands.Move",
            (Function<List<Object>, Command>) (List<Object> arg) -> move
        );

        IoC.<Command>resolve(
            "IoC.Register",
            "Commands.Rotate",
            (Function<List<Object>, Command>) (List<Object> arg) -> rotate
        );

        var cmd = IoC.<Command>resolve("Commands.Move");

        cmd.execute();

        verify(move, times(1)).execute();
        verify(rotate, never()).execute();
    }

    //п.3 Расширяемая фабрика и IoC
    @Test
    void scopeNew_success() {
        var resolver = new ScopeResolver();
        IoC.setStrategy(
            resolver::resolve
        );

        IoC.<Command>resolve(
            "Scope.New",
            "scopeId"
        ).execute();

        IoC.<Command>resolve(
            "Scope.Current",
            "scopeId"
        ).execute();

        IoC.<Command>resolve(
            "IoC.Register",
            "Commands.Move",
            (Function<List<Object>, Command>) (List<Object> arg) -> move
        );

        IoC.<Command>resolve(
            "IoC.Register",
            "Commands.Rotate",
            (Function<List<Object>, Command>) (List<Object> arg) -> rotate
        );

        var cmd = IoC.<Command>resolve("Commands.Move");

        cmd.execute();

        verify(move, times(1)).execute();
        verify(rotate, never()).execute();
    }

    //п.3 Расширяемая фабрика и IoC
    @Test
    void twoScopes_success() {
        var scopeId1 = "scopeId1";
        var scopeId2 = "scopeId2";

        var move1 = mock(Move.class);
        var move2 = mock(Move.class);

        var resolver = new ScopeResolver();
        IoC.setStrategy(
            resolver::resolve
        );

        IoC.<Command>resolve("Scope.New", scopeId1).execute();
        IoC.<Command>resolve("Scope.Current", scopeId1).execute();

        IoC.<Command>resolve("IoC.Register", "Commands.Move", (Function<List<Object>, Command>) (List<Object> arg) -> move1);

        IoC.<Command>resolve("Scope.New", scopeId2).execute();
        IoC.<Command>resolve("Scope.Current", scopeId2).execute();

        IoC.<Command>resolve("IoC.Register", "Commands.Move", (Function<List<Object>, Command>) (List<Object> arg) -> move2);

        var cmd = IoC.<Command>resolve("Commands.Move");
        cmd.execute();

        verify(move2, times(1)).execute();
        verify(move1, never()).execute();

        IoC.<Command>resolve("Scope.Current", scopeId1).execute();

        cmd = IoC.resolve("Commands.Move");
        cmd.execute();

        verify(move1, times(1)).execute();
    }

    //п.4 Расширяемая фабрика и IoC
    @Test
    void twoScopes_differentThread_success() throws InterruptedException {
        var scopeId1 = "scopeId1";
        var scopeId2 = "scopeId2";

        var move1 = mock(Move.class);
        var move2 = mock(Move.class);

        var resolver = new ScopeResolver();
        IoC.setStrategy(
            resolver::resolve
        );

        IoC.<Command>resolve("Scope.New", scopeId1).execute();
        IoC.<Command>resolve("Scope.New", scopeId2).execute();

        Thread first = new Thread(() -> {
            IoC.<Command>resolve("Scope.Current", scopeId1).execute();
            IoC.<Command>resolve("IoC.Register", "Commands.Move", (Function<List<Object>, Command>) (List<Object> arg) -> move1);
            var cmd = IoC.<Command>resolve("Commands.Move");
            cmd.execute();
        });

        Thread second = new Thread(() -> {
            IoC.<Command>resolve("Scope.Current", scopeId2).execute();
            IoC.<Command>resolve("IoC.Register", "Commands.Move", (Function<List<Object>, Command>) (List<Object> arg) -> move2);
            var cmd = IoC.<Command>resolve("Commands.Move");
            cmd.execute();
        });

        first.start();
        second.start();

        first.join();
        second.join();

        verify(move1, times(1)).execute();
        verify(move2, times(1)).execute();
    }
}
