package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

public class UnsupportedOperatorException extends MCCodeRuntimeException {
  public UnsupportedOperatorException(final Scope scope, final Operator operator, final Type<?> type) {
    super(scope, "mccode.interpreter.error.unsupported_operator_unary", operator, type);
  }

  public UnsupportedOperatorException(final Scope scope, final Operator operator, final Type<?> type1, final Type<?> type2) {
    super(scope, "mccode.interpreter.error.unsupported_operator", operator, type1, type2);
  }
}
