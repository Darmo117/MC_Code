package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

public class Parameter {
  private final String name;
  private final Type<?> type;

  public Parameter(final String name, final Type<?> type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return this.name;
  }

  public Type<?> getType() {
    return this.type;
  }
}
