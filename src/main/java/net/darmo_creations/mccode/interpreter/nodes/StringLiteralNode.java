package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * A node that represents a string literal.
 */
public class StringLiteralNode extends LiteralNode<String> {
  public static final int ID = 4;

  /**
   * Create a string literal node.
   *
   * @param value String value.
   */
  public StringLiteralNode(final String value) {
    super(Objects.requireNonNull(value));
  }

  /**
   * Create a string literal node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public StringLiteralNode(final NBTTagCompound tag) {
    super(tag::getString);
  }

  @Override
  protected BiConsumer<String, String> getValueSerializer(final NBTTagCompound tag) {
    return tag::setString;
  }

  @Override
  public int getID() {
    return ID;
  }
}
