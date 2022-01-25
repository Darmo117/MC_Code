package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * A {@link Node} representing a variable.
 */
public class VariableNode extends Node {
  public static final int ID = 100;

  private static final String NAME_KEY = "Name";

  private final String name;

  /**
   * Create a variable {@link Node}.
   *
   * @param name Variable’s name.
   */
  public VariableNode(final String name) {
    this.name = Objects.requireNonNull(name);
  }

  /**
   * Create a number {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public VariableNode(final NBTTagCompound tag) {
    this(tag.getString(NAME_KEY));
  }

  /**
   * Return the value of the variable referenced by this node.
   *
   * @return The variable’s value.
   * @throws EvaluationException If no variable with this name exists in the given scope.
   */
  @Override
  public Object evaluate(final Scope scope) throws EvaluationException {
    return scope.getVariable(this.name, false);
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setString(NAME_KEY, this.name);
    return tag;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
