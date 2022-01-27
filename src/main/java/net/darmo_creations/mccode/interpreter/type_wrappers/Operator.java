package net.darmo_creations.mccode.interpreter.type_wrappers;

public enum Operator {
  MINUS("-"), NOT("not"),
  PLUS("+"), SUB("-"), MUL("*"), DIV("/"), INT_DIV("//"), MOD("%"), POW("^"),
  EQUAL("="), NOT_EQUAL("!="), GT(">"), GE(">="), LT("<"), LE("<="), IN("in", true), NOT_IN("not in", true),
  AND("and"), OR("or"),
  GET_ITEM("get_item"), ITERATE("iter"), LENGTH("len");

  private final String symbol;
  private final boolean flipped;

  Operator(final String symbol) {
    this.symbol = symbol;
    this.flipped = false;
  }

  Operator(final String symbol, final boolean flipped) {
    this.symbol = symbol;
    this.flipped = flipped;
  }

  public String getSymbol() {
    return this.symbol;
  }

  public boolean isFlipped() {
    return this.flipped;
  }

  public static Operator fromString(final String s) {
    for (Operator operator : values()) {
      if (operator.getSymbol().equals(s)) {
        return operator;
      }
    }
    return null;
  }
}
