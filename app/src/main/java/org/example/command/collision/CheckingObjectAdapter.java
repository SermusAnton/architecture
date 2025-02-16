package org.example.command.collision;

import org.example.objects.MovingObject;
import org.example.value.Vector;

public class CheckingObjectAdapter implements CheckingObject {

    private final MovingObject movingObject;

    private final Vector offset;

    private final Vector side;

    public CheckingObjectAdapter(MovingObject movingObject, Vector offset, Vector side) {
        this.movingObject = movingObject;
        this.offset = offset;
        this.side = side;
    }

    @Override
    public Locality getOldLocality() {
        return Locality.getLocality(movingObject.getLocation(), offset, side);
    }

    @Override
    public Locality getNewLocality() {
        var newPosition =  Vector.plus(movingObject.getLocation(), movingObject.getVelocity());
        return Locality.getLocality(newPosition, offset, side);
    }
}
