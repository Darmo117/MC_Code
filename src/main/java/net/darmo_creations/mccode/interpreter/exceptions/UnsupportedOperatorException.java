package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

/**
 * Exception thrown when an operator was called on an incompatible object.
 */
public class UnsupportedOperatorException extends MCCodeRuntimeException {
  /**
   * Create an exception for a unary operator.
   *
   * @param scope    The scope this exception was thrown from.
   * @param operator The operator that raised the error.
   * @param type     Type of the object the operator was applied to.
   */
  public UnsupportedOperatorException(final Scope scope, final Operator operator, final Type<?> type) {
    super(scope, "mccode.interpreter.error.unsupported_operator_unary", operator, type);
  }

  /**
   * Create an exception a binary operator.
   *
   * @param scope    The scope this exception was thrown from.
   * @param operator The operator that raised the error.
   * @param type1    Type of the operator’s left operand.
   * @param type2    Type of the operator’s right operand.
   */
  public UnsupportedOperatorException(final Scope scope, final Operator operator, final Type<?> type1, final Type<?> type2) {
    super(scope, "mccode.interpreter.error.unsupported_operator", operator, type1, type2);
  }
}
