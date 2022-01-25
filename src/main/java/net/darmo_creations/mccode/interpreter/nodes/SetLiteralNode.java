package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link Node} representing a set literal.
 */
public class SetLiteralNode extends Node {
  public static final int ID = 7;

  private static final String VALUES_KEY = "Values";

  private final Set<Node> values;

  /**
   * Create a set literal {@link Node}.
   *
   * @param values Set’s values.
   */
  public SetLiteralNode(final List<Node> values) {
    this.values = new HashSet<>(values);
  }

  /**
   * Create a set literal {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public SetLiteralNode(final NBTTagCompound tag) {
    NBTTagList list = tag.getTagList(VALUES_KEY, new NBTTagCompound().getId());
    this.values = new HashSet<>();
    for (NBTBase t : list) {
      this.values.add(NodeNBTHelper.getNodeForTag((NBTTagCompound) t));
    }
  }

  @Override
  public Object evaluate(Scope scope) throws EvaluationException, ArithmeticException {
    return new MCSet(this.values.stream().map(node -> node.evaluate(scope)).collect(Collectors.toSet()));
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
