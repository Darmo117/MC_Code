package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;

public class MCCodeRuntimeException extends MCCodeException {
  private final Scope scope;
  private final Object[] args;

  public MCCodeRuntimeException(final Scope scope, final String translationKey, final Object... args) {
    super(translationKey);
    this.scope = scope;
    this.args = args;
  }

  public Scope getScope() {
    return this.scope;
  }

  public String getTranslationKey() {
    return this.getMessage();
  }

  public Object[] getArgs() {
    return this.args;
  }
}
