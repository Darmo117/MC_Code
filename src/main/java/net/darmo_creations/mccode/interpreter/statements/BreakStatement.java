package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Statement that is used to exit from a loop.
 */
public class BreakStatement extends Statement {
  public static final int ID = 60;

  /**
   * Create a break statement.
   */
  public BreakStatement() {
  }

  /**
   * Create a break statement from an NBT tag.
   */
  public BreakStatement(final NBTTagCompound ignoredTag) {
  }

  @Override
  public StatementAction execute(final Scope scope) {
    return StatementAction.EXIT_LOOP;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return "break;";
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof BreakStatement;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }
}
