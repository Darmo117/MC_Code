package net.darmo_creations.mccode.interpreter.exceptions;

public class TypeException extends MCCodeException {
  public TypeException(String s) {
    super(s);
  }

  public TypeException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
