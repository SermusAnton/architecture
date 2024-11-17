package org.example;

import org.example.objects.MovingObject;
import org.example.command.Move;
import org.example.exception.ChangeLocationException;
import org.example.exception.ReadLocationException;
import org.example.exception.ReadVelocityException;
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
class MoveTest {

    @Mock
    MovingObject movingObject;

    @InjectMocks
    Move move;

    @Test
    void rectilinearMovement_success() {
        var location = new Vector(12, 5);
        var velocity = new Vector(-7,3);
        var exitingLocation = new Vector(5, 8);
        when(movingObject.getLocation()).thenReturn(location);
        when(movingObject.getVelocity()).thenReturn(velocity);

        move.execute();

        verify(movingObject, times(1)).setLocation(exitingLocation);
    }

    @Test
    void rectilinearMovement_locationIsNull_exception() {
        when(movingObject.getLocation()).thenReturn(null);

        assertThrows(ReadLocationException.class, () -> move.execute());

        verify(movingObject, times(0)).setLocation(any());
    }

    @Test
    void rectilinearMovement_velocityIsNull_exception() {
        var location = new Vector(12, 5);
        when(movingObject.getLocation()).thenReturn(location);
        when(movingObject.getVelocity()).thenReturn(null);

        assertThrows(ReadVelocityException.class, () -> move.execute());

        verify(movingObject, times(0)).setLocation(any());
    }

    @Test
    void rectilinearMovement_setLocation_exception() {
        var location = new Vector(12, 5);
        var velocity = new Vector(-7,3);
        when(movingObject.getLocation()).thenReturn(location);
        when(movingObject.getVelocity()).thenReturn(velocity);
        doThrow(new ChangeLocationException("message")).when(movingObject).setLocation(any());

        assertThrows(ChangeLocationException.class, () -> move.execute());
    }
}
