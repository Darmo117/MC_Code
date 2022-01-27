package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.ListType;

/**
 * Exception throw by __get_item__ method of {@link ListType}.
 */
public class IndexOutOfBoundsException extends MCCodeRuntimeException {
  /**
   * Create an exception.
   *
   * @param scope The scope this exception was thrown from.
   * @param index Index that raised the error.
   */
  public IndexOutOfBoundsException(final Scope scope, final int index) {
    super(scope, "mccode.interpreter.error.index_out_of_bounds", index);
  }
}
