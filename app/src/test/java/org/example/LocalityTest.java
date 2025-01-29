package org.example;

import org.example.command.collision.Locality;
import org.example.value.Vector;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LocalityTest {

    @ParameterizedTest
    @MethodSource("provideDataAndResult")
    void getLeftDownVertex_success(Vector position, Vector offset, Vector side, Locality expected) {
        var result = Locality.getLocality(position, offset, side);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideDataAndResult() {
        return Stream.of(
            Arguments.of(new Vector(4, 4, 4),
                new Vector(2, 2, 2),
                new Vector(3, 3, 3),
                new Locality(new Vector(2, 2, 2), new Vector(3, 3, 3))),
            Arguments.of(new Vector(6, 6, 6),
                new Vector(2, 2, 2),
                new Vector(3, 3, 3),
                new Locality(new Vector(5, 5, 5), new Vector(3, 3, 3))),
            Arguments.of(new Vector(5, 5, 5),
                new Vector(2, 2, 2),
                new Vector(3, 3, 3),
                new Locality(new Vector(5, 5, 5), new Vector(3, 3, 3))),
            Arguments.of(new Vector(4, 4, 6),
                new Vector(2, 2, 2),
                new Vector(3, 3, 3),
                new Locality(new Vector(2, 2, 5), new Vector(3, 3, 3)))
        );
    }
}
