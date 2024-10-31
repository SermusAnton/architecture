package org.example.command;

import org.example.object.MovingObject;
import org.example.exception.ChangeLocationException;
import org.example.exception.ReadLocationException;
import org.example.exception.ReadVelocityException;
import org.example.value.Vector;

import java.util.Objects;

public class Move implements Command {

    private final MovingObject movingObject;

    public Move(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    @Override
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
