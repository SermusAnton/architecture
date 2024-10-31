package org.example;

import org.example.value.Vector;

public interface MovingObject {
    Vector getLocation();

    Vector getVelocity();

    void setLocation(Vector newLocation);
}
