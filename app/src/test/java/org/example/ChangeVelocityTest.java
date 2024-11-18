package org.example;

import org.example.command.ChangeVelocity;
import org.example.exception.ChangeVelocityException;
import org.example.exception.ReadVelocityDropException;
import org.example.exception.ReadVelocityException;
import org.example.objects.VelocityChangingObject;
import org.example.value.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangeVelocityTest {

    @Mock
    VelocityChangingObject velocityChangingObject;

    @InjectMocks
    ChangeVelocity changeVelocity;

    // п.5 Команда
    @Test
    void changeVelocity_success() {
        var velocity = new Vector(-7,3);
        var dropVelocity = new Vector(-7,3);
        var expectedVelocity = new Vector(0, 0);
        when(velocityChangingObject.getVelocity()).thenReturn(velocity);
        when(velocityChangingObject.getVelocityDrop()).thenReturn(dropVelocity);

        changeVelocity.execute();

        verify(velocityChangingObject, times(1)).setVelocity(expectedVelocity);
    }

    // п.5 Команда
    @Test
    void changeVelocity_velocityIsNull_exception() {
        when(velocityChangingObject.getVelocity()).thenReturn(null);

        assertThrows(ReadVelocityException.class, () -> changeVelocity.execute());

        verify(velocityChangingObject, never()).setVelocity(any());
    }

    // п.5 Команда
    @Test
    void rectilinearMovement_velocityIsNull_exception() {
        var velocity = new Vector(-7,3);
        when(velocityChangingObject.getVelocity()).thenReturn(velocity);
        when(velocityChangingObject.getVelocityDrop()).thenReturn(null);

        assertThrows(ReadVelocityDropException.class, () -> changeVelocity.execute());

        verify(velocityChangingObject, never()).setVelocity(any());
    }

    // п.5 Команда
    @Test
    void rectilinearMovement_setLocation_exception() {
        var velocity = new Vector(-7,3);
        var dropVelocity = new Vector(-7,3);
        when(velocityChangingObject.getVelocity()).thenReturn(velocity);
        when(velocityChangingObject.getVelocityDrop()).thenReturn(dropVelocity);
        doThrow(new ChangeVelocityException("message")).when(velocityChangingObject).setVelocity(any());

        assertThrows(ChangeVelocityException.class, () -> changeVelocity.execute());
    }
}
