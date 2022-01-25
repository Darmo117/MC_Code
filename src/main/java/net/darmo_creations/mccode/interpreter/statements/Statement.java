package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.NBTSerializable;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A statement is an instruction that can be executed in a given scope.
 */
public abstract class Statement implements NBTSerializable {
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

  /**
   * Serialize this statement into an NBT tag.
   *
   * @return The serialized data.
   */
  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ID_KEY, this.getID());
    return tag;
  }

  /**
   * Return the type ID of this statement.
   */
  public abstract int getID();

  @Override
  public abstract String toString();
}
