package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.NBTSerializable;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Base class for statements.
 * A statement is an instruction that can be executed in a given scope.
 * <p>
 * Statements can be serialized to NBT tags.
 */
public abstract class Statement implements NBTSerializable {
  /**
   * NBT tag key of statement ID property.
   */
  public static final String ID_KEY = "StatementID";

  /**
   * Execute this statement.
   *
   * @param scope Current scope.
   * @return The action to take after this statement has been executed.
   * @throws EvaluationException If an error occured during evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  public abstract StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException;

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ID_KEY, this.getID());
    return tag;
  }

  /**
   * Return the ID of this statement.
   * Used to serialize this statement; must be unique to each concrete subclass.
   */
  public abstract int getID();

  @Override
  public abstract String toString();
}
