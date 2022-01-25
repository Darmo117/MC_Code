package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;

/**
 * A {@link Node} representing a boolean literal.
 */
public class BooleanLiteralNode extends LiteralNode<Boolean> {
  public static final int ID = 1;

  /**
   * Create a boolean literal {@link Node}.
   *
   * @param value Boolean value.
   */
  public BooleanLiteralNode(final boolean value) {
    super(value);
  }

  /**
   * Create a boolean literal {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public BooleanLiteralNode(final NBTTagCompound tag) {
    super(tag::getBoolean);
  }

  @Override
  protected BiConsumer<String, Boolean> getValueSerializer(final NBTTagCompound tag) {
    return tag::setBoolean;
  }

  @Override
  public int getID() {
    return ID;
  }
}
