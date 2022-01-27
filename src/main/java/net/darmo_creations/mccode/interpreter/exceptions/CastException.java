package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

/**
 * Exception thrown when an attempt was made to cast an object to an incompatible type.
 */
public class CastException extends MCCodeRuntimeException {
  /**
   * Create a cast exception.
   *
   * @param scope      The scope this exception was thrown from.
   * @param targetType Cast target type.
   * @param sourceType Type of cast object.
   */
  public CastException(final Scope scope, final Type<?> targetType, final Type<?> sourceType) {
    super(scope, "mccode.interpreter.error.invalid_cast", sourceType, targetType);
  }
}
