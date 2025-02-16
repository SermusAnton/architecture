package org.example.command;

import org.example.exception.ChangeVelocityException;
import org.example.objects.StartingObject;
import org.example.value.Vector;

public class StartMove implements Command{

    private final StartingObject startingObject;

    private final Vector initialVelocity;

    public StartMove(StartingObject startingObject, Vector initialVelocity) {
        this.startingObject = startingObject;
        this.initialVelocity = initialVelocity;
    }

    @Override
    public void execute() {
        try {
            startingObject.setVelocity(initialVelocity);
        } catch (RuntimeException e) {
            throw new ChangeVelocityException(e.getMessage());
        }
    }
}
