package net.darmo_creations.mccode.interpreter.exceptions;

public class SyntaxErrorException extends MCCodeException {
  public SyntaxErrorException(String s) {
    super(s);
  }

  public SyntaxErrorException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
