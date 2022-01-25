package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link Node} representing an list literal.
 */
public class ListLiteralNode extends Node {
  public static final int ID = 5;

  private static final String VALUES_KEY = "Values";

  private final List<Node> values;

  /**
   * Create an list literal {@link Node}.
   *
   * @param values List’s values.
   */
  public ListLiteralNode(final List<Node> values) {
    this.values = new ArrayList<>(values);
  }

  /**
   * Create an list literal {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public ListLiteralNode(final NBTTagCompound tag) {
    NBTTagList list = tag.getTagList(VALUES_KEY, new NBTTagCompound().getId());
    this.values = new ArrayList<>();
    for (NBTBase t : list) {
      this.values.add(NodeNBTHelper.getNodeForTag((NBTTagCompound) t));
    }
  }

  @Override
  public Object evaluate(Scope scope) throws EvaluationException, ArithmeticException {
    return new MCList(this.values.stream().map(node -> node.evaluate(scope)).collect(Collectors.toList()));
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    NBTTagList list = new NBTTagList();
    this.values.forEach(v -> list.appendTag(v.writeToNBT()));
    tag.setTag(VALUES_KEY, list);
    return tag;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return this.values.toString();
  }
}
