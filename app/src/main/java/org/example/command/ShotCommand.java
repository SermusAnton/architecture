package org.example.command;

import org.example.exception.ChangeLocationException;
import org.example.exception.ReadLocationException;
import org.example.exception.ReadVelocityException;
import org.example.objects.ShootingObject;
import org.example.value.Vector;

import java.util.Objects;

public class ShotCommand implements Command {

    private final ShootingObject shootingObject;

    public ShotCommand(ShootingObject shootingObject) {
        this.shootingObject = shootingObject;
    }

    @Override
    public void execute() {
        if (Objects.isNull(shootingObject.getLocation())) {
            throw new ReadLocationException();
        }
        if (Objects.isNull(shootingObject.getVelocity())) {
            throw new ReadVelocityException();
        }
        try {
            shootingObject.setLocation(
                Vector.plus(shootingObject.getLocation(), shootingObject.getVelocity())
            );
        } catch (RuntimeException e) {
            throw new ChangeLocationException(e.getMessage());
        }
    }
}
