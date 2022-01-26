package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

public class CastException extends MCCodeRuntimeException {
  public CastException(final Scope scope, final Type<?> targetType, final Type<?> sourceType) {
    super(scope, "mccode.interpreter.error.invalid_cast", sourceType, targetType);
  }
}
