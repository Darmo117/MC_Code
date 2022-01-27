package net.darmo_creations.mccode.interpreter.exceptions;

/**
 * Error thrown when a program features a syntax error.
 */
public class SyntaxErrorException extends MCCodeException {
  public SyntaxErrorException(final String s) {
    super(s);
  }
}
