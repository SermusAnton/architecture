package org.example.command;

import org.example.exception.RunningOutOfFuelException;
import org.example.objects.FuelCheckingObject;

import java.util.Objects;

public class CheckFuel implements Command {

    private final FuelCheckingObject fuelCheckingObject;

    public CheckFuel(FuelCheckingObject fuelCheckingObject) {
        this.fuelCheckingObject = fuelCheckingObject;
    }

    @Override
    public void execute() {
        if (Objects.isNull(fuelCheckingObject.getReserve())) {
            throw new IllegalArgumentException();
        }
        if (Objects.isNull(fuelCheckingObject.getConsumption())) {
            throw new IllegalArgumentException();
        }

        if (fuelCheckingObject.getReserve().isEmpty()) {
            throw new RunningOutOfFuelException();
        }

        if (fuelCheckingObject.getReserve().compareTo(fuelCheckingObject.getConsumption()) < 0) {
            throw new RunningOutOfFuelException();
        }
    }
}
