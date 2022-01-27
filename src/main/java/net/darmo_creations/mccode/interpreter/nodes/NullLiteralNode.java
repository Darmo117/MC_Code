package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;

/**
 * A node that represents a null value.
 */
public class NullLiteralNode extends LiteralNode<Void> {
  public static final int ID = 0;

  /**
   * Create the null literal node.
   */
  public NullLiteralNode() {
    super((Void) null);
  }

  /**
   * Create the null literal node from an NBT tag.
   *
   * @param ignored The tag to deserialize.
   */
  public NullLiteralNode(final NBTTagCompound ignored) {
    super(key -> null);
  }

  @Override
  public NBTTagCompound writeToNBT() {
    return new NBTTagCompound(); // Return empty placeholder tag
  }

  @Override
  protected BiConsumer<String, Void> getValueSerializer(final NBTTagCompound tag) {
    return null; // No value to serialize
  }

  @Override
  public int getID() {
    return ID;
  }
}
