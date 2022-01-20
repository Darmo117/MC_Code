package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.Interpreter;
import net.darmo_creations.naissancee.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.naissancee.mccode.interpreter.exceptions.MCCodeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Type<T> {
  // Set by Interpreter.declareType() method
  @SuppressWarnings("unused")
  private String name;
  // Set by Interpreter.declareType() method
  @SuppressWarnings("unused")
  private Class<?> trueType;
  // Set by Interpreter.declareType() method
  @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
  private Map<String, Method> properties;

  public String getName() {
    return this.name;
  }

  public Class<?> getTrueType() {
    return this.trueType;
  }

  public List<String> getPropertieNames() {
    return new ArrayList<>(this.properties.keySet());
  }

  public Object getProperty(final String propertyName, final Object o) {
    this.ensureType(o, String.format("attempt to get property from type \"%s\" for object of type \"%s\"", this, o));
    try {
      return this.properties.get(propertyName).invoke(o);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new MCCodeException(
          String.format("cannot access property \"%s\" of object of type \"%s\"", propertyName, this), e);
    }
  }

  public T getDefault() {
    throw new MCCodeException(this.getName() + " type has no default value");
  }

  public T cast(final Object o) {
    if (o != null && o.getClass() == this.getTrueType()) {
      //noinspection unchecked
      return (T) o;
    }
    throw new CastException(this, Interpreter.getTypeForValue(o));
  }

  /**
   * Return the value at the given index.
   *
   * @param index Index of the item.
   * @param o     The object to get the item from.
   * @return The item value for the index.
   * @apiNote API method to circumvent generic type restrictions.
   */
  public Object getItem(final Object index, final Object o) {
    this.ensureType(o, "");
    //noinspection unchecked
    return this._getItem(index, (T) o);
  }

  /**
   * Return the value at the given index.
   *
   * @param index Index of the item.
   * @param o     The object to get the item from.
   * @return The item value for the index.
   */
  protected Object _getItem(final Object index, final T o) {
    throw new UnsupportedOperationException(String.format("type \"%s\" does not support getItem", this));
  }

  /**
   * Convert the given object to a boolean value.
   *
   * @param o The object to convert.
   * @return The boolean value for the object.
   * @apiNote API method to circumvent generic type restrictions.
   */
  final boolean toBoolean(final Object o) {
    //noinspection unchecked
    return this._toBoolean((T) o);
  }

  /**
   * Convert the given object to a boolean value.
   *
   * @param o The object to convert.
   * @return The boolean value for the object.
   */
  protected boolean _toBoolean(final T o) {
    return true;
  }

  @Override
  public String toString() {
    return this.name;
  }

  private void ensureType(final Object o, final String errorMessage) {
    if (Interpreter.getTypeForValue(o) != this) {
      throw new MCCodeException(errorMessage);
    }
  }
}
