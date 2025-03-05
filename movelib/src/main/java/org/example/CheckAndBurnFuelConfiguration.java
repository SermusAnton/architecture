package org.example;

import org.example.command.BurnFuel;
import org.example.command.CheckFuel;
import org.example.command.Command;
import org.example.command.MacroCommand;
import org.example.command.Move;
import org.example.command.dynamic.Register;
import org.example.common.ioc.IoC;
import org.example.objects.FuelBurningObject;
import org.example.objects.FuelCheckingObject;
import org.example.objects.MovingObject;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CheckAndBurnFuelConfiguration implements Register {

    private static final String ADAPTER = "Adapter";

    @SuppressWarnings("unused")
    @Override
    public void init() {
        Function<List<Object>, Command> macroCommand = (List<Object> args) -> {
            var fuelCheckingObject = IoC.<FuelCheckingObject>resolve(ADAPTER, FuelCheckingObject.class, args.get(0));
            var checkFuel = new CheckFuel(fuelCheckingObject);
            var movingObject = IoC.<MovingObject>resolve(ADAPTER, MovingObject.class, args.get(0));
            var move = new Move(movingObject);
            var fuelBurningObject = IoC.<FuelBurningObject>resolve(ADAPTER, FuelBurningObject.class, args.get(0));
            var burnFuel = new BurnFuel(fuelBurningObject);
            return new MacroCommand(List.of(checkFuel, move, burnFuel));
        };

        IoC.<Command>resolve(
            "IoC.Register",
            "Move",
            macroCommand
        );
    }
}
