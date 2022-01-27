package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;

/**
 * Base class for runtime exceptions of MCCode.
 */
public class MCCodeRuntimeException extends MCCodeException {
  private final Scope scope;
  private final Object[] args;

  /**
   * Create a runtime exception.
   *
   * @param scope          The scope this exception was thrown from.
   * @param translationKey Unlocalized string of the error message.
   * @param args           Values to use to format the error message.
   */
  public MCCodeRuntimeException(final Scope scope, final String translationKey, final Object... args) {
    super(translationKey);
    this.scope = scope;
    this.args = args;
  }

  /**
   * Return the scope of the this exception was thrown from.
   */
  public Scope getScope() {
    return this.scope;
  }

  /**
   * Return the unlocalized string of the error message.
   */
  public String getTranslationKey() {
    return this.getMessage();
  }

  /**
   * Return the values to use to format the error message.
   */
  public Object[] getArgs() {
    return this.args;
  }
}
