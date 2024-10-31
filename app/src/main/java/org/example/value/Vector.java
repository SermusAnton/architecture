package org.example.value;

import java.util.Arrays;
import java.util.Objects;

public class Vector {

    private final int[] coordinates;

    public Vector(int... coordinates) {
        this.coordinates = coordinates;
    }

    public static Vector plus(Vector first, Vector second) {
        var newCoordinates = new int[first.coordinates.length];
        for (int i = 0; i < first.coordinates.length; i++) {
            newCoordinates[i] = first.coordinates[i] + second.coordinates[i];
        }
        return new Vector(newCoordinates);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vector vector = (Vector) o;
        return Objects.deepEquals(coordinates, vector.coordinates);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinates);
    }

    @Override
    public String toString() {
        return "Vector{" +
            "coordinates=" + Arrays.toString(coordinates) +
            '}';
    }
}
