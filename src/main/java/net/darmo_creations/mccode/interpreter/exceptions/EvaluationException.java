package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;

public class EvaluationException extends MCCodeRuntimeException {
  public EvaluationException(final Scope scope, final String translationKey, final Object... args) {
    super(scope, translationKey, args);
  }
}
