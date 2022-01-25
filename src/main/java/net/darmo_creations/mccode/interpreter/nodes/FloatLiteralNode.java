package net.darmo_creations.mccode.interpreter.nodes;

import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;

/**
 * A {@link Node} representing a float literal.
 */
public class FloatLiteralNode extends LiteralNode<Double> {
  public static final int ID = 3;

  /**
   * Create a float literal {@link Node}.
   *
   * @param value Float value.
   */
  public FloatLiteralNode(final double value) {
    super(value);
  }

  /**
   * Create a float literal {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public FloatLiteralNode(final NBTTagCompound tag) {
    super(tag::getDouble);
  }

  @Override
  protected BiConsumer<String, Double> getValueSerializer(final NBTTagCompound tag) {
    return tag::setDouble;
  }

  @Override
  public int getID() {
    return ID;
  }
}
