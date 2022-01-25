package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

public class CastException extends MCCodeException {
  private final Type<?> targetType;
  private final Type<?> sourceType;

  public CastException(final Type<?> targetType, final Type<?> sourceType) {
    this.targetType = targetType;
    this.sourceType = sourceType;
  }

  public Type<?> getTargetType() {
    return this.targetType;
  }

  public Type<?> getSourceType() {
    return this.sourceType;
  }
}
