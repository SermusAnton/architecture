package org.example;

import org.example.command.CheckFuel;
import org.example.exception.RunningOutOfFuelException;
import org.example.objects.FuelCheckingObject;
import org.example.value.Fuel;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckFuelTest {

    @Mock
    FuelCheckingObject fuelCheckingObject;

    @InjectMocks
    CheckFuel checkFuel;

    // п.1 Команда
    @ParameterizedTest
    @MethodSource("provideFuelAndResult")
    void checkFuel_success(Fuel reserve, Fuel consumption, boolean isThrowRunningOutOfFuel) {
        when(fuelCheckingObject.getReserve()).thenReturn(reserve);
        when(fuelCheckingObject.getConsumption()).thenReturn(consumption);

        if (isThrowRunningOutOfFuel) {
            assertThrows(RunningOutOfFuelException.class, () -> checkFuel.execute());
        } else {
            assertDoesNotThrow(() -> checkFuel.execute());
        }
    }

    private static Stream<Arguments> provideFuelAndResult() {
        return Stream.of(
            Arguments.of(new Fuel(12), new Fuel(7), false),
            Arguments.of(new Fuel(0), new Fuel(7), true),
            Arguments.of(new Fuel(12), new Fuel(0), false),
            Arguments.of(new Fuel(12), new Fuel(15), true)
        );
    }
}
