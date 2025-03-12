package org.example.objects;

import org.example.value.Vector;

public interface ShootingObject {
    Vector getLocation();

    Vector getVelocity();

    void setLocation(Vector newLocation);
}
