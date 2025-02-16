package org.example.command.collision;

import org.example.command.Command;
import org.example.common.ioc.IoC;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class HittingArea {

    private final List<CheckingObject> checkingObjects = new CopyOnWriteArrayList<>();

    public void checkAndRemove(CheckingObject checkingObject) throws CollisionException {
        checkingObjects
            .stream()
            .filter(obj -> !Objects.equals(checkingObject, obj))
            .forEachOrdered(obj -> {
                var cmd = IoC.<Command>resolve("FunctionCollision", checkingObject, obj);
                cmd.execute();
                checkingObjects.remove(checkingObject);
            });
    }

    public void add(CheckingObject checkingObject) {
        checkingObjects.add(checkingObject);
    }
}