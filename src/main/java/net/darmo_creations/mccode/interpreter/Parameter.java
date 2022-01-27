package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

/**
 * A class that represents a function parameter.
 */
public class Parameter {
  private final String name;
  private final Type<?> type;

  /**
   * Create a function parameter.
   *
   * @param name Parameter’s name.
   * @param type Parameter’s type.
   */
  public Parameter(final String name, final Type<?> type) {
    this.name = name;
    this.type = type;
  }

  /**
   * Return parameter’s name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return parameter’s type.
   */
  public Type<?> getType() {
    return this.type;
  }
}
