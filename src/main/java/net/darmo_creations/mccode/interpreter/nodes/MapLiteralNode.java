package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A node that represents a map literal.
 */
public class MapLiteralNode extends Node {
  public static final int ID = 6;

  private static final String VALUES_KEY = "Values";

  private final Map<String, Node> values;

  /**
   * Create a map literal node.
   *
   * @param values Map’s entries.
   */
  public MapLiteralNode(final Map<String, Node> values) {
    this.values = new HashMap<>(values);
  }

  /**
   * Create a map literal node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public MapLiteralNode(final NBTTagCompound tag) {
    NBTTagCompound map = tag.getCompoundTag(VALUES_KEY);
    this.values = new HashMap<>();
    for (String k : map.getKeySet()) {
      this.values.put(k, NodeNBTHelper.getNodeForTag(map.getCompoundTag(k)));
    }
  }

  @Override
  public Object evaluate(Scope scope) throws EvaluationException, ArithmeticException {
    return new MCMap(this.values.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
      Object v = e.getValue().evaluate(scope);
      return scope.getProgramManager().getTypeForValue(v).copy(scope, v);
    })));
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    NBTTagCompound map = new NBTTagCompound();
    this.values.forEach((k, v) -> map.setTag(k, v.writeToNBT()));
    tag.setTag(VALUES_KEY, map);
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
