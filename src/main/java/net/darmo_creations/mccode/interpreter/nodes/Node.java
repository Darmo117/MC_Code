package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.NBTSerializable;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A node is a component of an expression tree.
 * It returns a value when evaluated in a given scope.
 * <p>
 * Nodes can be serialized to NBT tags.
 */
public abstract class Node implements NBTSerializable {
  public static final String ID_KEY = "NodeID";

  /**
   * Evaluate this node.
   *
   * @param scope The scope this node is evaluated from.
   * @return The value of this node.
   * @throws EvaluationException If an error occured during evaluation.
   * @throws ArithmeticException If a math error occured during evaluation.
   */
  public abstract Object evaluate(Scope scope) throws EvaluationException, ArithmeticException;

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

  @Override
  public abstract boolean equals(Object o);

  @Override
  public abstract int hashCode();
}
