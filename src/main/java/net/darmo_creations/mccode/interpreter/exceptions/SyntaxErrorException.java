package net.darmo_creations.mccode.interpreter.exceptions;

/**
 * Error thrown when a program features a syntax error.
 */
public class SyntaxErrorException extends MCCodeException {
  private final int line;
  private final int column;
  private final Object[] args;

  public SyntaxErrorException(final int line, final int column, final String translationKey, final Object... args) {
    super(translationKey);
    this.line = line;
    this.column = column;
    this.args = args;
  }

  /**
   * Return the line where this error occured on.
   */
  public int getLine() {
    return this.line;
  }

  /**
   * Return the column where this error occured on.
   */
  public int getColumn() {
    return this.column;
  }

  public String getTranslationKey() {
    return super.getMessage();
  }

  public Object[] getArgs() {
    return this.args;
  }
}
