package org.example.command;

import org.example.object.RotatingObject;
import org.example.exception.ChangeAngleException;
import org.example.exception.ReadAngelVelocity;
import org.example.exception.ReadAngleException;
import org.example.value.Angle;

import java.util.Objects;

public class Rotate implements Command {

    private final RotatingObject rotatingObject;

    public Rotate(RotatingObject rotatingObject) {
        this.rotatingObject = rotatingObject;
    }

    @Override
    public void execute() {
        if (Objects.isNull(rotatingObject.getAngle())) {
            throw new ReadAngleException();
        }
        if (Objects.isNull(rotatingObject.getAngleVelocity())) {
            throw new ReadAngelVelocity();
        }
        try {
            rotatingObject.setAngle(
                Angle.plus(rotatingObject.getAngle(), rotatingObject.getAngleVelocity())
            );
        } catch (RuntimeException e) {
            throw new ChangeAngleException(e.getMessage());
        }
    }
}
