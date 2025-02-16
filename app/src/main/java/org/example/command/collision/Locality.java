package org.example.command.collision;

import org.example.value.Vector;

import java.util.Objects;

public class Locality {

    private final Vector vertex;

    private final Vector side;

    public Locality(Vector vertex, Vector side) {
        this.vertex = vertex;
        this.side = side;
    }

    public static Locality getLocality(Vector position, Vector offset, Vector side) {
        var vertex = getLeftDownVertex(position, offset, side);
        return new Locality(vertex, side);
    }

    private static Vector getLeftDownVertex(Vector position, Vector offset, Vector side) {
        var withoutOffset = Vector.minus(position, offset);
        var number = Vector.divide(withoutOffset, side);
        return Vector.plus(Vector.multiply(number, side), offset);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Locality locality = (Locality) o;
        return Objects.equals(vertex, locality.vertex) && Objects.equals(side, locality.side);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex, side);
    }
}
