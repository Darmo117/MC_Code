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

  private Utils() {
  }
}
