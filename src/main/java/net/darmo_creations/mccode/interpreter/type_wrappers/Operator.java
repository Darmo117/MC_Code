package net.darmo_creations.mccode.interpreter.type_wrappers;

/**
 * Enumarates all available operators.
 */
public enum Operator {
  // Unary
  MINUS("-"),
  NOT("not"),

  // Binary math
  PLUS("+"),
  SUB("-"),
  MUL("*"),
  DIV("/"),
  INT_DIV("//"),
  MOD("%"),
  POW("^"),

  // Binary comparison
  EQUAL("="),
  NOT_EQUAL("!="),
  GT(">"),
  GE(">="),
  LT("<"),
  LE("<="),

  // Binary logic
  AND("and"),
  OR("or"),

  // Collections
  IN("in", true),
  NOT_IN("not in", true),
  GET_ITEM("get_item"),
  SET_ITEM("set_item"),
  DEL_ITEM("del_item"),
  ITERATE("iter"),
  LENGTH("len"),
  ;

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

  /**
   * Return the symbol of this operator.
   */
  public String getSymbol() {
    return this.symbol;
  }

  /**
   * Return whether the instance and second argument of this operator are flipped.
   * <p>
   * Specifically concerns the {@link #IN} and {@link #NOT_IN} operators.
   */
  public boolean isFlipped() {
    return this.flipped;
  }

  /**
   * Return the operator with the given symbol.
   *
   * @param symbol The symbol.
   * @return The operator or null if none matched.
   */
  public static Operator fromString(final String symbol) {
    for (Operator operator : values()) {
      if (operator.getSymbol().equals(symbol)) {
        return operator;
      }
    }
    return null;
  }
}
