package org.example;


import org.example.command.ShotCommand;
import org.example.exception.ChangeLocationException;
import org.example.exception.ReadLocationException;
import org.example.exception.ReadVelocityException;
import org.example.objects.ShootingObject;
import org.example.value.Vector;
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
class ShotTest {
    @Mock
    ShootingObject shootingObject;

    @InjectMocks
    ShotCommand shotCommand;

    @Test
    void shot_success() {
        var location = new Vector(12, 5);
        var velocity = new Vector(-7,3);
        var exitingLocation = new Vector(5, 8);
        when(shootingObject.getLocation()).thenReturn(location);
        when(shootingObject.getVelocity()).thenReturn(velocity);

        shotCommand.execute();

        verify(shootingObject, times(1)).setLocation(exitingLocation);
    }

    @Test
    void shot_locationIsNull_exception() {
        when(shootingObject.getLocation()).thenReturn(null);

        assertThrows(ReadLocationException.class, () -> shotCommand.execute());

        verify(shootingObject, times(0)).setLocation(any());
    }

    @Test
    void shot_velocityIsNull_exception() {
        var location = new Vector(12, 5);
        when(shootingObject.getLocation()).thenReturn(location);
        when(shootingObject.getVelocity()).thenReturn(null);

        assertThrows(ReadVelocityException.class, () -> shotCommand.execute());

        verify(shootingObject, times(0)).setLocation(any());
    }

    @Test
    void shot_setLocation_exception() {
        var location = new Vector(12, 5);
        var velocity = new Vector(-7,3);
        when(shootingObject.getLocation()).thenReturn(location);
        when(shootingObject.getVelocity()).thenReturn(velocity);
        doThrow(new ChangeLocationException("message")).when(shootingObject).setLocation(any());

        assertThrows(ChangeLocationException.class, () -> shotCommand.execute());
    }
}
