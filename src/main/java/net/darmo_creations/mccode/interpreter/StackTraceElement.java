package net.darmo_creations.mccode.interpreter;

public class StackTraceElement {
  private final int line;
  private final String scopeName;

  public StackTraceElement(final int line, final String scopeName) {
    this.line = line;
    this.scopeName = scopeName;
  }

  public int getLine() {
    return this.line;
  }

  public String getScopeName() {
    return this.scopeName;
  }
}
