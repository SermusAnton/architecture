package org.example;

import org.example.exception.ChangeLocationException;
import org.example.exception.ReadLocationException;
import org.example.exception.ReadVelocityException;

import java.util.Objects;

public class Move {

    private final MovingObject movingObject;

    public Move(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    public void execute() {
        if (Objects.isNull(movingObject.getLocation())) {
            throw new ReadLocationException();
        }
        if (Objects.isNull(movingObject.getVelocity())) {
            throw new ReadVelocityException();
        }
        try {
            movingObject.setLocation(
                Vector.plus(movingObject.getLocation(), movingObject.getVelocity())
            );
        } catch (RuntimeException e) {
            throw new ChangeLocationException(e.getMessage());
        }
    }
}
