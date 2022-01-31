package net.darmo_creations.mccode.interpreter.exceptions;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.darmo_creations.mccode.interpreter.type_wrappers.TernaryOperator;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.darmo_creations.mccode.interpreter.type_wrappers.UnaryOperator;

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
  public UnsupportedOperatorException(final Scope scope, final UnaryOperator operator, final Type<?> type) {
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
  public UnsupportedOperatorException(final Scope scope, final BinaryOperator operator, final Type<?> type1, final Type<?> type2) {
    super(scope, "mccode.interpreter.error.unsupported_binary_operator", operator, type1, type2);
  }

  /**
   * Create an exception a ternary operator.
   *
   * @param scope    The scope this exception was thrown from.
   * @param operator The operator that raised the error.
   * @param type1    Type of the first operand.
   * @param type2    Type of the second operand.
   * @param type3    Type of the third operand.
   */
  public UnsupportedOperatorException(final Scope scope, final TernaryOperator operator, final Type<?> type1, final Type<?> type2, final Type<?> type3) {
    super(scope, "mccode.interpreter.error.unsupported_ternary_operator", operator, type1, type2, type3);
  }
}
