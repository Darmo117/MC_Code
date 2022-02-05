package net.darmo_creations.mccode.interpreter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsTest {
  @ParameterizedTest
  @MethodSource("provideArgsForTrueModulo")
  void trueModulo(double expected, double a, double b) {
    assertEquals(expected, Utils.trueModulo(a, b));
  }

  public static Stream<Arguments> provideArgsForTrueModulo() {
    return Stream.of(
        Arguments.of(0, 0, 1),
        Arguments.of(0, 1, 1),
        Arguments.of(0, 0, 3),
        Arguments.of(1, 1, 3),
        Arguments.of(2, 2, 3),
        Arguments.of(0, 3, 3),
        Arguments.of(0, -3, 3),
        Arguments.of(2, -1, 3),
        Arguments.of(1, -2, 3),
        Arguments.of(1.5, 1.5, 3),
        Arguments.of(0.5, 3.5, 3),
        Arguments.of(1.5, -1.5, 3),
        Arguments.of(0.5, -2.5, 3),
        Arguments.of(0.5, 4, 3.5),
        Arguments.of(-1, 2, -3)
    );
  }

  @Test
  void trueModuloError() {
    //noinspection ResultOfMethodCallIgnored
    assertThrows(ArithmeticException.class, () -> Utils.trueModulo(1, 0));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForEscapeString")
  void escapeString(String expected, String s) {
    assertEquals(expected, Utils.escapeString(s));
  }

  public static Stream<Arguments> provideArgsForEscapeString() {
    return Stream.of(
        Arguments.of("\"\"", ""),
        Arguments.of("\"a\"", "a"),
        Arguments.of("\"\\n\"", "\n"),
        Arguments.of("\"\\\"\"", "\""),
        Arguments.of("\"\\\\\"", "\\")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForUnescapeString")
  void unescapeString(String expected, String s) {
    assertEquals(expected, Utils.unescapeString(s));
  }

  public static Stream<Arguments> provideArgsForUnescapeString() {
    return Stream.of(
        Arguments.of("", "\"\""),
        Arguments.of("a", "\"a\""),
        Arguments.of("\n", "\"\\n\""),
        Arguments.of("\"", "\"\\\"\""),
        Arguments.of("\\", "\"\\\\\"")
    );
  }
}
