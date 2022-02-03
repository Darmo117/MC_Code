package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Base node that represents a literal value (boolean, int, float, string, null, etc.)
 *
 * @param <T> Type of the literal’s value.
 */
public abstract class LiteralNode<T> extends Node {
  public static final String VALUE_KEY = "Value";

  protected final T value;

  /**
   * Create a literal node.
   *
   * @param value Literal’s value.
   */
  public LiteralNode(final T value) {
    this.value = value;
  }

  /**
   * Create a literal node from deserializing function.
   *
   * @param deserializer The type-specific function to deserialize the tag.
   */
  public LiteralNode(final Function<String, T> deserializer) {
    this(deserializer.apply(VALUE_KEY));
  }

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
   * Return a method for the given NBT tag that accepts a string key and this literal’s value.
   *
   * @param tag The tag to serialize the value into.
   * @return The serializing function.
   */
  protected abstract BiConsumer<String, T> getValueSerializer(final NBTTagCompound tag);

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    LiteralNode<?> that = (LiteralNode<?>) o;
    return Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value);
  }
}
