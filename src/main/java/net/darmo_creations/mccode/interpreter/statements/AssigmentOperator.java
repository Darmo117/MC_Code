package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;

import java.util.Objects;
import java.util.Optional;

public enum AssigmentOperator {
  ASSIGN(":=", null), PLUS("+=", Operator.PLUS), SUB("-=", Operator.SUB), MUL("*=", Operator.MUL),
  DIV("/=", Operator.DIV), INT_DIV("//=", Operator.INT_DIV), MOD("%=", Operator.MOD), POW("^=", Operator.POW);

  private final String symbol;
  private final Operator baseOperator;

  AssigmentOperator(final String symbol, final Operator baseOperator) {
    this.symbol = Objects.requireNonNull(symbol);
    this.baseOperator = baseOperator;
  }

  public String getSymbol() {
    return this.symbol;
  }

  public Optional<Operator> getBaseOperator() {
    return Optional.ofNullable(this.baseOperator);
  }

  public static AssigmentOperator fromString(final String s) {
    for (AssigmentOperator operator : values()) {
      if (operator.getSymbol().equals(s)) {
        return operator;
      }
    }
    return null;
  }
}
