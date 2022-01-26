package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;
import java.util.function.Function;

/**
 * Utility class for deserializing {@link Node}s from NBT tags.
 */
public final class NodeNBTHelper {
  private static final Map<Integer, Function<NBTTagCompound, Node>> NODE_PROVIDERS = new HashMap<>();

  static {
    NODE_PROVIDERS.put(NullLiteralNode.ID, NullLiteralNode::new);
    NODE_PROVIDERS.put(BooleanLiteralNode.ID, BooleanLiteralNode::new);
    NODE_PROVIDERS.put(IntLiteralNode.ID, IntLiteralNode::new);
    NODE_PROVIDERS.put(FloatLiteralNode.ID, FloatLiteralNode::new);
    NODE_PROVIDERS.put(StringLiteralNode.ID, StringLiteralNode::new);
    NODE_PROVIDERS.put(ListLiteralNode.ID, ListLiteralNode::new);
    NODE_PROVIDERS.put(MapLiteralNode.ID, MapLiteralNode::new);
    NODE_PROVIDERS.put(SetLiteralNode.ID, SetLiteralNode::new);

    NODE_PROVIDERS.put(VariableNode.ID, VariableNode::new);
    NODE_PROVIDERS.put(PropertyCallNode.ID, PropertyCallNode::new);
    NODE_PROVIDERS.put(MethodCallNode.ID, MethodCallNode::new);
    NODE_PROVIDERS.put(FunctionCallNode.ID, FunctionCallNode::new);

    NODE_PROVIDERS.put(CastOperatorNode.ID, CastOperatorNode::new);
    NODE_PROVIDERS.put(UnaryOperatorNode.ID, UnaryOperatorNode::new);
    NODE_PROVIDERS.put(BinaryOperatorNode.ID, BinaryOperatorNode::new);
  }

  /**
   * Return the node corresponding to the given tag.
   *
   * @param tag The tag to deserialize.
   * @return The node.
   * @throws IllegalArgumentException If no {@link Node} correspond to the {@link Node#ID_KEY} property.
   */
  public static Node getNodeForTag(final NBTTagCompound tag) {
    int tagID = tag.getInteger(Node.ID_KEY);
    if (!NODE_PROVIDERS.containsKey(tagID)) {
      throw new IllegalArgumentException("Undefined node ID: " + tagID);
    }
    return NODE_PROVIDERS.get(tagID).apply(tag);
  }

  public static List<Node> deserializeNodesList(final NBTTagCompound tag, final String key) {
    List<Node> statements = new ArrayList<>();
    for (NBTBase t : tag.getTagList(key, new NBTTagCompound().getId())) {
      statements.add(getNodeForTag((NBTTagCompound) t));
    }
    return statements;
  }

  public static NBTTagList serializeNodesList(final Collection<Node> statements) {
    NBTTagList statementsList = new NBTTagList();
    statements.forEach(s -> statementsList.appendTag(s.writeToNBT()));
    return statementsList;
  }

  private NodeNBTHelper() {
  }
}
