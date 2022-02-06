package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;

/**
 * A node that represents an integer literal.
 */
public class IntLiteralNode extends LiteralNode<Long> {
  public static final int ID = 2;

  /**
   * Create an integer literal node.
   *
   * @param value Integer value.
   */
  public IntLiteralNode(final long value) {
    super(value);
  }

  /**
   * Create an int literal node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public IntLiteralNode(final NBTTagCompound tag) {
    super(tag::getLong);
  }

  @Override
  protected BiConsumer<String, Long> getValueSerializer(final NBTTagCompound tag) {
    return tag::setLong;
  }

  @Override
  public int getID() {
    return ID;
  }
}
