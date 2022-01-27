package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;

import java.util.Objects;
import java.util.Optional;

/**
 * Enumerates all available assignment operators.
 */
public enum AssigmentOperator {
  ASSIGN(":=", null),
  PLUS("+=", Operator.PLUS),
  SUB("-=", Operator.SUB),
  MUL("*=", Operator.MUL),
  DIV("/=", Operator.DIV),
  INT_DIV("//=", Operator.INT_DIV),
  MOD("%=", Operator.MOD),
  POW("^=", Operator.POW),
  ;

  private final String symbol;
  private final Operator baseOperator;

  AssigmentOperator(final String symbol, final Operator baseOperator) {
    this.symbol = Objects.requireNonNull(symbol);
    this.baseOperator = baseOperator;
  }

  /**
   * Return the symbol of this operator.
   */
  public String getSymbol() {
    return this.symbol;
  }

  /**
   * Return the {@link Operator} to apply before assigning the value.
   */
  public Optional<Operator> getBaseOperator() {
    return Optional.ofNullable(this.baseOperator);
  }

  /**
   * Return the operator with the given symbol.
   *
   * @param symbol The symbol.
   * @return The operator or null if none matched.
   */
  public static AssigmentOperator fromString(final String symbol) {
    for (AssigmentOperator operator : values()) {
      if (operator.getSymbol().equals(symbol)) {
        return operator;
      }
    }
    return null;
  }
}
