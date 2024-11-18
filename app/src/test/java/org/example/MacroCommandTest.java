package org.example;

import org.example.command.BurnFuel;
import org.example.command.ChangeVelocity;
import org.example.command.CheckFuel;
import org.example.command.MacroCommand;
import org.example.command.Move;
import org.example.command.Rotate;
import org.example.exception.ChangeLocationException;
import org.example.exception.CommandException;
import org.example.exception.RunningOutOfFuelException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MacroCommandTest {

    @Mock
    Move move;

    @Mock
    Rotate rotate;

    @Mock
    CheckFuel checkFuel;

    @Mock
    BurnFuel burnFuel;

    @Mock
    ChangeVelocity changeVelocity;

    // п.3 Команда
    @Test
    void macroCommand_success() {
        var macroCommand = new MacroCommand(List.of(move, rotate));

        macroCommand.execute();

        verify(move, times(1)).execute();
        verify(rotate, times(1)).execute();
    }

    // п.3 Команда
    @Test
    void macroCommand_exception() {
        var macroCommand = new MacroCommand(List.of(move, rotate));
        doThrow(new ChangeLocationException("message")).when(move).execute();

        assertThrows(CommandException.class, () ->  macroCommand.execute());

        verify(move, times(1)).execute();
        verify(rotate, times(0)).execute();
    }

    // п.4 Команда
    @Test
    void macroCommand_moveAndBurnFuel_success() {
        var macroCommand = new MacroCommand(List.of(checkFuel, move, burnFuel));

        macroCommand.execute();

        verify(checkFuel, times(1)).execute();
        verify(move, times(1)).execute();
        verify(burnFuel, times(1)).execute();
    }

    // п.4 Команда
    @Test
    void macroCommand_moveAndBurnFuel_exception() {
        var macroCommand = new MacroCommand(List.of(checkFuel, move, burnFuel));
        doThrow(new RunningOutOfFuelException()).when(checkFuel).execute();

        assertThrows(CommandException.class, () ->  macroCommand.execute());

        verify(checkFuel, times(1)).execute();
        verify(move, never()).execute();
        verify(burnFuel, never()).execute();
    }

    // п.6 Команда
    @Test
    void macroCommand_rotateAndChangeVelocity_success() {
        var macroCommand = new MacroCommand(List.of(rotate, changeVelocity));

        macroCommand.execute();

        verify(rotate, times(1)).execute();
        verify(changeVelocity, times(1)).execute();
    }
}
