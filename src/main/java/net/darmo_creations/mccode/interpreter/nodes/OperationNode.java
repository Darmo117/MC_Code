package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Node} representing the call to a function or operator.
 */
public abstract class OperationNode extends Node {
  private static final String OPERANDS_KEY = "Operands";

  protected final List<Node> operands;

  /**
   * Create an operation call.
   *
   * @param operands Function’s arguments.
   */
  public OperationNode(final List<Node> operands) {
    this.operands = new ArrayList<>(operands);
  }

  /**
   * Create an operation {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public OperationNode(final NBTTagCompound tag) {
    this(deserializeOperands(tag.getTagList(OPERANDS_KEY, new NBTTagCompound().getId())));
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    NBTTagList list = new NBTTagList();
    this.operands.forEach(node -> list.appendTag(node.writeToNBT()));
    tag.setTag(OPERANDS_KEY, list);
    return tag;
  }

  private static List<Node> deserializeOperands(NBTTagList tagList) {
    List<Node> list = new ArrayList<>();
    tagList.forEach(tag -> list.add(NodeNBTHelper.getNodeForTag((NBTTagCompound) tag)));
    return list;
  }
}
