package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.MapType;

/**
 * Exception throw by __del_item__ and __get_item__ methods of {@link MapType}.
 */
public class NoSuchKeyException extends MCCodeRuntimeException {
  /**
   * Create an exception.
   *
   * @param scope The scope this exception was thrown from.
   * @param key   Key that raised the error.
   */
  public NoSuchKeyException(final Scope scope, final String key) {
    super(scope, "mccode.interpreter.error.no_such_key", key);
  }
}
