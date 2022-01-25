package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Base node that represents a literal value (boolean, int, float, string, null, etc.)
 *
 * @param <T> Type of the literal’s value.
 */
public abstract class LiteralNode<T> extends Node {
  private static final String VALUE_KEY = "Value";

  private final T value;

  /**
   * Create a float {@link Node}.
   *
   * @param value Float value.
   */
  public LiteralNode(final T value) {
    this.value = value;
  }

  /**
   * Create a literal {@link Node} from deserializing function.
   *
   * @param deserializer The type-specific function to deserialize the tag.
   */
  public LiteralNode(final Function<String, T> deserializer) {
    this(deserializer.apply(VALUE_KEY));
  }

  /**
   * Return the value of this node.
   *
   * @return The associated float.
   */
  @Override
  public Object evaluate(final Scope scope) {
    return this.value;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    BiConsumer<String, T> serializer = this.getValueSerializer(tag);
    if (serializer != null) {
      serializer.accept(VALUE_KEY, this.value);
    }
    return tag;
  }

  /**
   * Return a method from the given NBT tag that accepts a string key and this literal’s value.
   *
   * @param tag The tag to serialize the value into..
   * @return The value serializing function.
   */
  protected abstract BiConsumer<String, T> getValueSerializer(final NBTTagCompound tag);

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }
}
