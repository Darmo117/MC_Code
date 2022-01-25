package net.darmo_creations.mccode.interpreter.exceptions;

public class MCCodeException extends RuntimeException {
  public MCCodeException() {
  }

  public MCCodeException(String s) {
    super(s);
  }

  public MCCodeException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public MCCodeException(Throwable throwable) {
    super(throwable);
  }
}
