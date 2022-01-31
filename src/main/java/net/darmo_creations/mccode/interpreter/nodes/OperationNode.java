package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

/**
 * A node that represents the call to a function or operator.
 */
public abstract class OperationNode extends Node {
  public static final String ARGUMENTS_KEY = "Arguments";

  protected final List<Node> arguments;

  /**
   * Create an operation call node.
   *
   * @param arguments Operation’s arguments.
   */
  public OperationNode(final List<Node> arguments) {
    this.arguments = new ArrayList<>(arguments);
  }

  /**
   * Create an operation node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public OperationNode(final NBTTagCompound tag) {
    this(NodeNBTHelper.deserializeNodesList(tag, ARGUMENTS_KEY));
  }

  /**
   * Return the list of arguments.
   */
  public List<Node> getArguments() {
    return new ArrayList<>(this.arguments);
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(ARGUMENTS_KEY, NodeNBTHelper.serializeNodesList(this.arguments));
    return tag;
  }
}
