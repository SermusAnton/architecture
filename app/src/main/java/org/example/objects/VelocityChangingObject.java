package org.example.objects;

import org.example.value.Vector;

public interface VelocityChangingObject {
    Vector getVelocity();

    Vector getVelocityDrop();

    void setVelocity(Vector newValue);
}
