package org.example;

import org.example.command.BurnFuel;
import org.example.exception.ChangeFuelReserveException;
import org.example.objects.FuelBurningObject;
import org.example.value.Fuel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BurnFuelTest {

    @Mock
    FuelBurningObject fuelBurningObject;

    @InjectMocks
    BurnFuel burnFuel;

    // п.2 Команда
    @Test
    void burnFuel_success() {
        var reserve = new Fuel(12);
        var consumption = new Fuel(7);

        when(fuelBurningObject.getReserve()).thenReturn(reserve);
        when(fuelBurningObject.getConsumption()).thenReturn(consumption);

        burnFuel.execute();

        var expectedReserve = new Fuel(5);
        verify(fuelBurningObject, times(1)).setFuelReserve(expectedReserve);
    }

    // п.2 Команда
    @Test
    void burnFuel_cantSetFuelReserve_exception() {
        var reserve = new Fuel(12);
        var consumption = new Fuel(7);

        when(fuelBurningObject.getReserve()).thenReturn(reserve);
        when(fuelBurningObject.getConsumption()).thenReturn(consumption);
        doThrow(new ChangeFuelReserveException("message")).when(fuelBurningObject).setFuelReserve(any());

        assertThrows(ChangeFuelReserveException.class, () -> burnFuel.execute());
    }
}
