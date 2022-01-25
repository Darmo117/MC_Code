package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

public class ImportStatement extends Statement {
  public static final int ID = 0;

  public ImportStatement(final NBTTagCompound tag) {
    // TODO
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    // TODO
    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return null;
  }
}
