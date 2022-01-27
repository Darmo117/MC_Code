package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;

public class IndexOutOfBoundsException extends MCCodeRuntimeException {
  public IndexOutOfBoundsException(final Scope scope, final int index) {
    super(scope, "mccode.interpreter.error.index_out_of_bounds", index);
  }
}
