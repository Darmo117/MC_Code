package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

public class ContinueStatement extends Statement {
  public static final int ID = 61;

  public ContinueStatement() {
  }

  public ContinueStatement(final NBTTagCompound ignoredTag) {
  }

  @Override
  public StatementAction execute(final Scope scope) {
    return StatementAction.CONTINUE_LOOP;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return "continue;";
  }
}
