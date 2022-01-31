package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * A node that represents a variable.
 * <p>
 * Evaluates to the value of the referenced variable.
 * May throw an exception if the variable is not declared in the given scope.
 */
public class VariableNode extends Node {
  public static final int ID = 100;

  public static final String NAME_KEY = "Name";

  private final String name;

  /**
   * Create a variable node.
   *
   * @param name Variable’s name.
   */
  public VariableNode(final String name) {
    this.name = Objects.requireNonNull(name);
  }

  /**
   * Create a number node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public VariableNode(final NBTTagCompound tag) {
    this(tag.getString(NAME_KEY));
  }

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
