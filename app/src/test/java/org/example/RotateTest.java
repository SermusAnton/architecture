package org.example;

import org.example.object.RotatingObject;
import org.example.command.Rotate;
import org.example.exception.ChangeAngleException;
import org.example.exception.ReadAngelVelocity;
import org.example.exception.ReadAngleException;
import org.example.value.Angle;
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
class RotateTest {

    @Mock
    RotatingObject rotatingObject;

    @InjectMocks
    Rotate rotate;

    @Test
    void rotate_success() {
        var angle = new Angle(12);
        var angleVelocity = new Angle(350);
        var exitingAngle = new Angle(108);
        when(rotatingObject.getAngle()).thenReturn(angle);
        when(rotatingObject.getAngleVelocity()).thenReturn(angleVelocity);

        rotate.execute();

        verify(rotatingObject, times(1)).setAngle(exitingAngle);
    }

    @Test
    void rotate_angleIsNull_exception() {
        when(rotatingObject.getAngle()).thenReturn(null);

        assertThrows(ReadAngleException.class, () -> rotate.execute());

        verify(rotatingObject, times(0)).setAngle(any());
    }

    @Test
    void rotate_velocityIsNull_exception() {
        var angle = new Angle(12);
        when(rotatingObject.getAngle()).thenReturn(angle);
        when(rotatingObject.getAngleVelocity()).thenReturn(null);

        assertThrows(ReadAngelVelocity.class, () -> rotate.execute());

        verify(rotatingObject, times(0)).setAngle(any());
    }

    @Test
    void rotate_setAngle_exception() {
        var angle = new Angle(12);
        var angleVelocity = new Angle(350);
        when(rotatingObject.getAngle()).thenReturn(angle);
        when(rotatingObject.getAngleVelocity()).thenReturn(angleVelocity);
        doThrow(new ChangeAngleException("message")).when(rotatingObject).setAngle(any());

        assertThrows(ChangeAngleException.class, () -> rotate.execute());
    }
}
