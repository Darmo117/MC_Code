package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * A node that represents a call to an object’s property.
 */
public class PropertyCallNode extends Node {
  public static final int ID = 101;

  private static final String OBJECT_KEY = "Object";
  private static final String PROPERTY_NAME_KEY = "PropertyName";

  private final Node object;
  private final String propertyName;

  /**
   * Create a node that represents a call to an object’s property.
   *
   * @param object       Expression that evaluates to the object to get the property of.
   * @param propertyName Name of the property.
   */
  public PropertyCallNode(final Node object, final String propertyName) {
    this.object = Objects.requireNonNull(object);
    this.propertyName = Objects.requireNonNull(propertyName);
  }

  /**
   * Create a node that represents a call to an object’s property from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public PropertyCallNode(final NBTTagCompound tag) {
    this(
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(OBJECT_KEY)),
        tag.getString(PROPERTY_NAME_KEY)
    );
  }

  @Override
  public Object evaluate(final Scope scope) throws EvaluationException, ArithmeticException {
    Object obj = this.object.evaluate(scope);
    Type<?> objectType = scope.getProgramManager().getTypeForValue(obj);
    return objectType.getProperty(scope, obj, this.propertyName);
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(OBJECT_KEY, this.object.writeToNBT());
    tag.setString(PROPERTY_NAME_KEY, this.propertyName);
    return tag;
  }

  @Override
  public String toString() {
    return String.format("%s.%s", this.object, this.propertyName);
  }
}
