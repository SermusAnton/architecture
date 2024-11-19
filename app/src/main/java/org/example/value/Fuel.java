package org.example.value;

import org.example.exception.RunningOutOfFuelException;

import java.util.Objects;

public class Fuel implements Comparable<Fuel> {

    private final int value;

    public Fuel(int value) {
        if (value < 0) {
            throw new RunningOutOfFuelException();
        }
        this.value = value;
    }

    public static Fuel minus(Fuel first, Fuel second) {
        return new Fuel(first.value - second.value);
    }

    public boolean isEmpty() {
        return value == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fuel that = (Fuel) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "FuelMeasure{" +
            "value=" + value +
            '}';
    }

    @Override
    public int compareTo(Fuel o) {
        return this.value - o.value;
    }
}
