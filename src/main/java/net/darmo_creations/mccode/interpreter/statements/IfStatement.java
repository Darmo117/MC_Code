package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;

public class IfStatement extends Statement {
  public static final int ID = 40;

  private int ip;

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return null; // TODO
  }
}
