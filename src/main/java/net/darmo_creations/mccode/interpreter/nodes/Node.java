package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.NBTSerializable;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A node is the base component of an expression tree.
 * It returns a value when evaluated in a given scope.
 */
public abstract class Node implements NBTSerializable {
  public static final String ID_KEY = "NodeID";

  /**
   * Evaluate this node.
   *
   * @param scope The scope this node is evaluated from.
   * @return The value of this node.
   * @throws EvaluationException If an error occured during evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  public abstract Object evaluate(Scope scope) throws EvaluationException, ArithmeticException;

  /**
   * Serialize this node into an NBT tag.
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
   * Return the type ID of this Node.
   */
  public abstract int getID();

  @Override
  public abstract String toString();
}
