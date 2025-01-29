package org.example;

import org.example.command.Command;
import org.example.command.collision.CheckCollisionInArea;
import org.example.command.collision.CheckingObject;
import org.example.command.collision.CheckingObjectAdapter;
import org.example.command.collision.CollisionException;
import org.example.command.collision.HittingArea;
import org.example.command.collision.Locality;
import org.example.common.ioc.DependencyResolver;
import org.example.common.ioc.IoC;
import org.example.objects.MovingObject;
import org.example.value.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckCollisionInAreaTest {

    @Mock
    private MovingObject movingObject;

    @BeforeEach
    void init() {
        Map<String, Function<List<Object>, Object>> store = new ConcurrentHashMap<>();

        Function<List<Object>, Object> register = args ->
            store.put((String) args.get(0), (Function<List<Object>, Object>) args.get(1));
        store.put("IoC.Register", register);

        var dependencyVault = new DependencyResolver(store);
        IoC.setStrategy(
            dependencyVault::resolve
        );
    }

    @DisplayName("Простое перемещение из одной окрестности в другую без коллизий")
    @Test
    void execute_success() {
        var offset = new Vector(1, 1, 1); // половина side
        var side = new Vector(2, 2, 2);

        when(movingObject.getLocation()).thenReturn(new Vector(2, 2, 2)); // был в т. 2,2,2
        when(movingObject.getVelocity()).thenReturn(new Vector(2, 2, 2)); // переместился 4,4,4

        var checkingObject = new CheckingObjectAdapter(movingObject, offset, side);

        var locality1 = new Locality(new Vector(1, 1, 1), side);
        var locality2 = new Locality(new Vector(3, 3, 3), side);
        var oldHittingArea = mock(HittingArea.class);
        var newHittingArea = mock(HittingArea.class);
        var hittingAreas = new ConcurrentHashMap();
        hittingAreas.put(locality1, oldHittingArea);
        hittingAreas.put(locality2, newHittingArea);

        var checkCollisionInArea = new CheckCollisionInArea(checkingObject, hittingAreas, null);

        assertDoesNotThrow(checkCollisionInArea::execute);

        verify(oldHittingArea, times(1)).checkAndRemove(checkingObject);
        verify(oldHittingArea, never()).add(any());
        verify(newHittingArea, never()).checkAndRemove(any());
        verify(newHittingArea, times(1)).add(checkingObject);
    }

    @DisplayName("Простое перемещение из одной окрестности в другую в исходной области произошла коллизия")
    @Test
    void execute_oldLocationCollision_collision() {
        var side = new Vector(2, 2, 2);
        var oldLocality = new Locality(new Vector(1, 1, 1), side);
        var newLocality = new Locality(new Vector(3, 3, 3), side);
        var otherLocality = new Locality(new Vector(10, 10, 10), side);

        var checking = mock(CheckingObject.class);
        when(checking.getOldLocality()).thenReturn(oldLocality);
        when(checking.getNewLocality()).thenReturn(newLocality);
        when(checking.toString()).thenReturn("checking");
        var collision = mock(CheckingObject.class);
        when(collision.toString()).thenReturn("collision");

        IoC.<Command>resolve(
            "IoC.Register",
            "FunctionCollision",
            (Function<List<Object>, Command>) (List<Object> arg) -> (Command) () -> {
                throw new CollisionException(new Exception(String.format(
                    "Two objects collided: %s and %s", arg.get(0), arg.get(1))));
            });

        var oldHittingArea = new HittingArea();
        oldHittingArea.add(collision);
        var newHittingArea = new HittingArea();
        var hittingAreas = Map.of(oldLocality, oldHittingArea, newLocality, newHittingArea);

        var checkCollisionInArea = new CheckCollisionInArea(checking, hittingAreas, null);

        assertThrows(CollisionException.class, checkCollisionInArea::execute);
    }

    @DisplayName("Простое перемещение из одной окрестности в другую, проверка для 2х пар окрестностей")
    @Test
    void execute_twoAreas_success() {
        var side = new Vector(2, 2, 2);

        var otherLocality = new Locality(new Vector(-1, -1, -1), side);
        var oldLocality = new Locality(new Vector(1, 1, 1), side);
        var newLocality = new Locality(new Vector(3, 3, 3), side);
        var checking1 = mock(CheckingObject.class);
        when(checking1.toString()).thenReturn("checking1");

        var checking2 = mock(CheckingObject.class);
        when(checking2.getOldLocality()).thenReturn(oldLocality);
        when(checking2.getNewLocality()).thenReturn(newLocality);
        when(checking2.toString()).thenReturn("checking2");

        IoC.<Command>resolve(
            "IoC.Register",
            "FunctionCollision",
            (Function<List<Object>, Command>) (List<Object> arg) -> (Command) () -> {
                throw new CollisionException(new Exception(String.format(
                    "Two objects collided: %s and %s", arg.get(0), arg.get(1))));
            });

        var hittingAreas = new ConcurrentHashMap<Locality, HittingArea>();
        var hittingArea = new HittingArea();
        hittingArea.add(checking1);
        hittingAreas.put(otherLocality, hittingArea);

        var hittingAreas2 = new ConcurrentHashMap<Locality, HittingArea>();
        var hittingArea2 = new HittingArea();
        hittingArea2.add(checking1);
        hittingAreas2.put(oldLocality, hittingArea2);

        var collision = new CheckCollisionInArea(checking2, hittingAreas2, null);

        assertThrows(CollisionException.class, () ->
            new CheckCollisionInArea(checking2, hittingAreas, collision).execute());
    }

}
