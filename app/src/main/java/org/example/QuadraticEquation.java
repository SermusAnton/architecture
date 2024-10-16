package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuadraticEquation {

    public static final double EPSILON = 10E-14;  // IEEE 754

    public List<Double> solve(double a, double b, double c, double e) {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c) || Double.isNaN(e)) {
            throw new IllegalArgumentException("Argument is Not-a-Number!");
        }

        if (Double.isInfinite(a) || Double.isInfinite(b) || Double.isInfinite(c) || Double.isInfinite(e)) {
            throw new IllegalArgumentException("Argument is Infinite!");
        }

        if (Math.abs(a - e) <= e) {
            throw new IllegalArgumentException("Is not quadratic equation!");
        }

        var d = discriminant(a, b, c);

        if (d - e < e * -1) {
            return Collections.emptyList();
        }

        var result = new ArrayList<Double>(2);

        if (Math.abs(d - e) <= e) {
            result.add(0, -1 * (b / 2) / a);
            result.add(1, -1 * (b / 2) / a);
            return result;
        }

        result.add(0, -1 * (b / 2) / a - (Math.sqrt(d) / 2) / a);
        result.add(1, -1 * (b / 2) / a + (Math.sqrt(d) / 2) / a);
        return result;
    }

    private double discriminant(double a, double b, double c) {
        return b * b - (4 * a * c);
    }
}
