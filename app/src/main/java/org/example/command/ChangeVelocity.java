package org.example.command;

import org.example.exception.ChangeVelocityException;
import org.example.exception.ReadVelocityDropException;
import org.example.exception.ReadVelocityException;
import org.example.objects.VelocityChangingObject;
import org.example.value.Vector;

import java.util.Objects;

public class ChangeVelocity implements Command {

    private final VelocityChangingObject velocityChangingObject;

    public ChangeVelocity(VelocityChangingObject velocityChangingObject) {
        this.velocityChangingObject = velocityChangingObject;
    }

    @Override
    public void execute() {
        if (Objects.isNull(velocityChangingObject.getVelocity())) {
            throw new ReadVelocityException();
        }
        if (Objects.isNull(velocityChangingObject.getVelocityDrop())) {
            throw new ReadVelocityDropException();
        }
        try {
            velocityChangingObject.setVelocity(
                Vector.minus(velocityChangingObject.getVelocity(), velocityChangingObject.getVelocityDrop())
            );
        } catch (RuntimeException e) {
            throw new ChangeVelocityException(e.getMessage());
        }
    }
}
