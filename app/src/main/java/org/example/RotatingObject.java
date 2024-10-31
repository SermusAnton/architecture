package org.example;

import org.example.value.Angle;

public interface RotatingObject {
    Angle getAngle();

    Angle getAngleVelocity();

    void setAngle(Angle newValue);
}
