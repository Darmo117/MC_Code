package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;

/**
 * A {@link Node} representing an integer literal.
 */
public class IntLiteralNode extends LiteralNode<Integer> {
  public static final int ID = 2;

  /**
   * Create an integer literal {@link Node}.
   *
   * @param value Integer value.
   */
  public IntLiteralNode(final int value) {
    super(value);
  }

  /**
   * Create an int literal {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public IntLiteralNode(final NBTTagCompound tag) {
    super(tag::getInteger);
  }

  @Override
  protected BiConsumer<String, Integer> getValueSerializer(final NBTTagCompound tag) {
    return tag::setInteger;
  }

  @Override
  public int getID() {
    return ID;
  }
}