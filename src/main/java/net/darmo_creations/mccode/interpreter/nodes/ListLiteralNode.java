package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A node that represents an list literal.
 */
public class ListLiteralNode extends Node {
  public static final int ID = 5;

  private static final String VALUES_KEY = "Values";

  private final List<Node> values;

  /**
   * Create an list literal node.
   *
   * @param values List’s values.
   */
  public ListLiteralNode(final Collection<Node> values) {
    this.values = new ArrayList<>(values);
  }

  /**
   * Create an list literal node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public ListLiteralNode(final NBTTagCompound tag) {
    this(NodeNBTHelper.deserializeNodesList(tag, VALUES_KEY));
  }

  @Override
  public Object evaluate(Scope scope) throws EvaluationException, ArithmeticException {
    return new MCList(this.values.stream().map(node -> {
      Object v = node.evaluate(scope);
      return ProgramManager.getTypeForValue(v).copy(scope, v);
    }).collect(Collectors.toList()));
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(VALUES_KEY, NodeNBTHelper.serializeNodesList(this.values));
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
