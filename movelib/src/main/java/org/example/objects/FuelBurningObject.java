package org.example.objects;

import org.example.value.Fuel;

public interface FuelBurningObject {
    void setFuelReserve(Fuel fuel);

    Fuel getReserve();

    Fuel getConsumption();
}
