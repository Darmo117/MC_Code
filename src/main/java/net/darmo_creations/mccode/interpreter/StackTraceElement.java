package net.darmo_creations.mccode.interpreter;

public class StackTraceElement {
  private final String scopeName;

  public StackTraceElement(final String scopeName) {
    this.scopeName = scopeName;
  }

  public String getScopeName() {
    return this.scopeName;
  }
}
