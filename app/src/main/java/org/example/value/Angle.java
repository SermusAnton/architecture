package org.example.value;

import java.util.Objects;

public class Angle {

    private final byte value;

    public Angle(int value){
        this.value = (byte) (value %Byte.MAX_VALUE);
    }

    public static Angle plus(Angle oldValue, Angle angleVelocity) {
        return new Angle(oldValue.value + angleVelocity.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Angle angle1 = (Angle) o;
        return value == angle1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Angle{" +
            "angle=" + value +
            '}';
    }
}
