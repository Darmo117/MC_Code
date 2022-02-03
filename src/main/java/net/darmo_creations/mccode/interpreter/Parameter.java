package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

import java.util.Objects;

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

  @Override
  public String toString() {
    return String.format("Parameter{name=%s,type=%s}", this.name, this.type);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Parameter parameter = (Parameter) o;
    return this.name.equals(parameter.name) && this.type.equals(parameter.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.type);
  }
}
