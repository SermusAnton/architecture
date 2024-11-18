package org.example.objects;

import org.example.value.Fuel;

public interface FuelBurningObject extends FuelCheckingObject {
    void setFuelReserve(Fuel fuel);
}
