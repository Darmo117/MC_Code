package net.darmo_creations.mccode.interpreter.exceptions;

public class ProgramException extends RuntimeException {
  private final int line;

  public ProgramException(final int line, Throwable cause) {
    super(cause);
    this.line = line;
  }

  public int getLine() {
    return this.line;
  }
}
