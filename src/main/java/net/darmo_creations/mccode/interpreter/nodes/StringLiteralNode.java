package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.minecraft.nbt.NBTTagCompound;

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
    super(value);
    if (value.matches("[\t\b\r\f]")) {
      throw new MCCodeException("illegal character(s) in string literal");
    }
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

  @Override
  public String toString() {
    return Utils.escapeString(this.value);
  }
}
