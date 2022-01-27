package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Statement that is used to skip to the next iteration of a loop.
 */
public class ContinueStatement extends Statement {
  public static final int ID = 61;

  /**
   * Create a continue statement.
   */
  public ContinueStatement() {
  }

  /**
   * Create a continue statement from an NBT tag.
   */
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
