package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.statements.Statement;

import java.util.List;
import java.util.StringJoiner;

/**
 * Utility functions for interpreter-related classes.
 */
public final class Utils {
  private static final String IDENT = "  ";

  /**
   * Stringify the given list of statements and indents them.
   * Resulting string starts and ends with a line return.
   *
   * @param statements The statements to format.
   * @return The stringified and indented statements.
   */
  public static String indentStatements(final List<Statement> statements) {
    if (statements.isEmpty()) {
      return "";
    }
    StringJoiner code = new StringJoiner("\n" + IDENT, "\n" + IDENT, "\n");
    statements.forEach(s -> code.add(s.toString()));
    return code.toString();
  }

  /**
   * Performs a true modulo operation using the mathematical definition of "a mod b".
   *
   * @param a Value to get the modulo of.
   * @param b The divisor.
   * @return a mod b
   * @throws ArithmeticException If b = 0.
   */
  public static double trueModulo(final double a, final double b) {
    if (b == 0) {
      throw new ArithmeticException("/ by 0");
    }
    return ((a % b) + b) % b;
  }

  public static String escapeString(final String s) {
    return String.format("\"%s\"", s.replaceAll("([\"\\\\])", "\\\\$1").replace("\n", "\\n"));
  }

  private Utils() {
  }
}
