package org.example.command;

import org.example.exception.ChangeFuelReserveException;
import org.example.objects.FuelBurningObject;
import org.example.value.Fuel;

public class BurnFuel implements Command {

    private final FuelBurningObject fuelBurningObject;

    public BurnFuel(FuelBurningObject fuelBurningObject) {
        this.fuelBurningObject = fuelBurningObject;
    }

    @Override
    public void execute() {
        try {
            fuelBurningObject.setFuelReserve(
                Fuel.minus(fuelBurningObject.getReserve(), fuelBurningObject.getConsumption())
            );
        } catch (Exception e) {
            throw new ChangeFuelReserveException(e.getMessage());
        }
    }
}
