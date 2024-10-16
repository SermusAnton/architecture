package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.example.QuadraticEquation.EPSILON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuadraticEquationTest {

    private final QuadraticEquation quadraticEquation = new QuadraticEquation();
    private static final double ZERO = 1.5E-15;
    private static final double NaN = Double.longBitsToDouble(0x7ff8000000000000L);

    private static final double NEGATIVE_INFINITY = Double.longBitsToDouble(0xfff0000000000000L);

    private static final double POSITIVE_INFINITY =  Double.longBitsToDouble(0x7ff0000000000000L);

    // n.3
    @Test
    void solve_noRoots_empty() {
        var result = quadraticEquation.solve(1d, 0, 1d, EPSILON);
        assertTrue(result.isEmpty());
    }

    // n.5
    @Test
    void solve_twoRoots_success() {
        var result = quadraticEquation.solve(1d, 0, -1d, EPSILON);
        assertEquals(-1d, result.get(0));
        assertEquals(1d, result.get(1));
    }

    // n.7
    @Test
    void solve_oneRoot_success() {
        var result = quadraticEquation.solve(1d, 2d, 1d, EPSILON);
        assertEquals(-1d, result.get(0));
        assertEquals(-1d, result.get(1));
    }

    // n.9
    @Test
    void solve_aZero_exception() {
        assertThrows(IllegalArgumentException.class,
            () -> quadraticEquation.solve(ZERO, 2d, 1d, EPSILON));
    }

    // n.11
    @Test
    void solve_discriminantLessEPSION_success() {
        double a = 1d;
        double c = 1d;
        double b = Math.sqrt(4 * (a * c) + ZERO);
        var result = quadraticEquation.solve(a, b, c, EPSILON);
        assertTrue(compareDouble(-1d, result.get(0)));
        assertTrue(compareDouble(-1d, result.get(1)));
    }

    // n.13
    @ParameterizedTest
    @MethodSource("provideNaN")
    void solve_NaN_exception(double a, double b, double c, double e, String expectedErrorMessage) {
        var result = assertThrows(IllegalArgumentException.class,
            () -> quadraticEquation.solve(a, b, c, e));
        assertEquals(expectedErrorMessage, result.getMessage());
    }

    // n.13
    @ParameterizedTest
    @MethodSource("provideNegativeInfinity")
    void solve_negativeInfinity_exception(double a, double b, double c, double e, String expectedErrorMessage) {
        var result = assertThrows(IllegalArgumentException.class,
            () -> quadraticEquation.solve(a, b, c, e));
        assertEquals(expectedErrorMessage, result.getMessage());
    }

    // n.13
    @ParameterizedTest
    @MethodSource("providePositiveInfinity")
    void solve_positiveInfinity_exception(double a, double b, double c, double e, String expectedErrorMessage) {
        var result = assertThrows(IllegalArgumentException.class,
            () -> quadraticEquation.solve(a, b, c, e));
        assertEquals(expectedErrorMessage, result.getMessage());
    }

    private boolean compareDouble(double expected, double actual) {
        return expected - actual <= EPSILON;
    }

    private static Stream<Arguments> provideNaN() {
        return Stream.of(
            Arguments.of(NaN, 2d, 1d, EPSILON, "Argument is Not-a-Number!"),
            Arguments.of(1d, NaN, 1d, EPSILON, "Argument is Not-a-Number!"),
            Arguments.of(1d, 2d, NaN, EPSILON, "Argument is Not-a-Number!"),
            Arguments.of(1d, 2d, 1d, NaN, "Argument is Not-a-Number!")
        );
    }

    private static Stream<Arguments> provideNegativeInfinity() {
        return Stream.of(
            Arguments.of(NEGATIVE_INFINITY, 2d, 1d, EPSILON, "Argument is Infinite!"),
            Arguments.of(1d, NEGATIVE_INFINITY, 1d, EPSILON, "Argument is Infinite!"),
            Arguments.of(1d, 2d, NEGATIVE_INFINITY, EPSILON, "Argument is Infinite!"),
            Arguments.of(1d, 2d, 1d, NEGATIVE_INFINITY, "Argument is Infinite!")
        );
    }

    private static Stream<Arguments> providePositiveInfinity() {
        return Stream.of(
            Arguments.of(POSITIVE_INFINITY, 2d, 1d, EPSILON, "Argument is Infinite!"),
            Arguments.of(1d, POSITIVE_INFINITY, 1d, EPSILON, "Argument is Infinite!"),
            Arguments.of(1d, 2d, POSITIVE_INFINITY, EPSILON, "Argument is Infinite!"),
            Arguments.of(1d, 2d, 1d, POSITIVE_INFINITY, "Argument is Infinite!")
        );
    }
}
