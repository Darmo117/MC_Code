package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.MemberFunction;
import net.darmo_creations.mccode.interpreter.ObjectProperty;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.annotations.PropertySetter;
import net.darmo_creations.mccode.interpreter.exceptions.*;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

/**
 * A type is a class that wraps a data type to make it usable from programs.
 * It may possess properties and methods annoted with {@link Property}, {@link PropertySetter} or {@link Method}.
 * <p>
 * Once declared in an {@link ProgramManager} instance it becomes available to programs of the manager.
 *
 * @param <T> Type of data wrapped by this class.
 */
public abstract class Type<T> {
  public static final String NAME_KEY = "Name";

  // Set by ProgramManager.processTypeAnnotations() method
  @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
  private Map<String, ObjectProperty> properties;
  // Set by ProgramManager.processTypeAnnotations() method
  @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
  private Map<String, MemberFunction> methods;
  // Set by ProgramManager.processTypeAnnotations() method
  @SuppressWarnings("unused")
  private String doc;

  /**
   * Return the MCCode name of this type.
   */
  public abstract String getName();

  /**
   * Return the class of the wrapped type.
   */
  public abstract Class<T> getWrappedType();

  /**
   * Return the documentation string for this type.
   */
  public Optional<String> getDoc() {
    return Optional.ofNullable(this.doc);
  }

  /**
   * Return whether to generate an explicit cast operator.
   */
  public boolean generateCastOperator() {
    return true;
  }

  /**
   * Return the name of all properties.
   */
  public List<String> getPropertiesNames() {
    ArrayList<String> list = new ArrayList<>(this.properties.keySet());
    list.addAll(this.methods.keySet());
    list.sort(Comparator.comparing(String::toLowerCase));
    return list;
  }

  /**
   * Return the value of a property for the given instance object.
   *
   * @param scope        The scope this property is queried from.
   * @param self         Instance object to query. Must match this type’s wrapped type.
   * @param propertyName Name of the property.
   * @return Value of the property.
   * @throws TypeException          If the MCCode type of the instance differs from this type.
   * @throws MCCodeRuntimeException If the instance object does not have a property with the given name.
   */
  public Object getProperty(final Scope scope, final Object self, final String propertyName) {
    if (this.properties.containsKey(propertyName)) {
      return this.properties.get(propertyName).get(self);
    }
    throw new EvaluationException(scope, "mccode.interpreter.error.no_property_for_type", this, propertyName);
  }

  /**
   * Set the value of a property for the given instance object.
   *
   * @param scope        The scope this property is set from.
   * @param self         Instance object to use. Must match this type’s wrapped type.
   * @param propertyName Name of the property.
   * @param value        Value to assign to the property.
   * @throws TypeException          If the MCCode type of the instance differs from this type.
   * @throws MCCodeRuntimeException If the instance object does not have a property with the given name.
   */
  public void setProperty(final Scope scope, final Object self, final String propertyName, final Object value) {
    if (this.properties.containsKey(propertyName)) {
      this.properties.get(propertyName).set(scope, self, value);
    }
    throw new EvaluationException(scope, "mccode.interpreter.error.no_property_for_type", this, propertyName);
  }

  /**
   * Return the method with the given name.
   *
   * @param scope The scope this method is queried from.
   * @param name  Method’s name.
   * @return The method.
   */
  public MemberFunction getMethod(Scope scope, final String name) {
    if (this.methods.containsKey(name)) {
      return this.methods.get(name);
    }
    throw new EvaluationException(scope, "mccode.interpreter.error.no_method_for_type", this, name);
  }

  /**
   * Cast the given object into an object of this type’s wrapped type.
   * <p>
   * Called when evaluating operators or functions.
   *
   * @param scope The scope this operation is performed from.
   * @param o     The object to cast.
   * @return The cast object.
   * @throws CastException If the object cannot be cast into this type’s wrapped type.
   */
  public T implicitCast(final Scope scope, final Object o) throws CastException {
    if (o == null) {
      return null;
    }
    if (this.getWrappedType().isAssignableFrom(o.getClass())) {
      //noinspection unchecked
      return (T) o;
    }
    throw new CastException(scope, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Cast the given object into an object of this type’s wrapped type.
   * Default behavior is the same as implicit cast.
   * <p>
   * Called when this type’s cast operator is called.
   *
   * @param scope The scope this operation is performed from.
   * @param o     The object to cast.
   * @return The new instance.
   * @throws MCCodeRuntimeException If this type does not have a cast operator,
   *                                i.e. method {@link #generateCastOperator()} returns false.
   */
  public T explicitCast(final Scope scope, final Object o) throws MCCodeRuntimeException {
    if (this.generateCastOperator()) {
      return this.implicitCast(scope, o);
    }
    throw new EvaluationException(scope, "mccode.interpreter.error.no_cast_operator_for_type", this);
  }

  /**
   * Convert the given object to a boolean value.
   *
   * @param self An instance of this type to convert.
   * @return The boolean value for the object.
   */
  public final boolean toBoolean(final Object self) {
    //noinspection unchecked
    return this.__bool__((T) self);
  }

  /**
   * Return a deep copy of the given object.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @return A deep copy of the argument.
   */
  public T copy(final Scope scope, final Object self) {
    this.ensureType(self, String.format("attempt to clone object of type \"%s\" from type \"%s\"",
        ProgramManager.getTypeForValue(self), this));
    //noinspection unchecked
    return this.__copy__(scope, (T) self);
  }

  /**
   * Applie an operator on the given values.
   *
   * @param scope    Scope the operator is applied from.
   * @param operator Operator to apply.
   * @param self     The object to apply the operator on.
   * @param o1       An optional second object to apply the operator on.
   *                 Ignored if the passed operator supports only one operand.
   * @param o2       An optional third object to apply the operator on.
   *                 Ignored if the passed operator supports only one or two operands.
   * @param inPlace  Whether this operator should modify the object instead of creating a new instance.
   *                 May be ignored by some operators.
   * @return The result of the operation; null if the operator does not return anything.
   * @throws MCCodeException If the given operator is invalid.
   */
  public Object applyOperator(final Scope scope, final Operator operator, Object self, Object o1, Object o2, final boolean inPlace) {
    if (ProgramManager.getTypeForValue(self) != this) {
      throw new MCCodeException(String.format("operator %s expected instance object of type %s, got %s", operator.getSymbol(), this.getWrappedType(), self.getClass()));
    }
    //noinspection unchecked
    T $this = (T) self;

    switch (operator) {
      case MINUS:
        return this.__minus__(scope, $this);
      case NOT:
        return this.__not__(scope, $this);
      case PLUS:
        return this.__add__(scope, $this, o1, inPlace);
      case SUB:
        return this.__sub__(scope, $this, o1, inPlace);
      case MUL:
        return this.__mul__(scope, $this, o1, inPlace);
      case DIV:
        return this.__div__(scope, $this, o1, inPlace);
      case INT_DIV:
        return this.__intdiv__(scope, $this, o1, inPlace);
      case MOD:
        return this.__mod__(scope, $this, o1, inPlace);
      case POW:
        return this.__pow__(scope, $this, o1, inPlace);
      case EQUAL:
        return this.__eq__(scope, $this, o1);
      case NOT_EQUAL:
        return this.__neq__(scope, $this, o1);
      case GT:
        return this.__gt__(scope, $this, o1);
      case GE:
        return this.__ge__(scope, $this, o1);
      case LT:
        return this.__lt__(scope, $this, o1);
      case LE:
        return this.__le__(scope, $this, o1);
      case IN:
        return this.__in__(scope, $this, o1);
      case NOT_IN:
        return !this.toBoolean(this.__in__(scope, $this, o1));
      case AND:
        return this.__and__(scope, $this, o1);
      case OR:
        return this.__or__(scope, $this, o1);
      case GET_ITEM:
        return this.__get_item__(scope, $this, o1);
      case SET_ITEM:
        this.__set_item__(scope, $this, o1, o2);
        return null;
      case DEL_ITEM:
        this.__del_item__(scope, $this, o1);
        return null;
      case ITERATE:
        return this.__iter__(scope, $this);
      case LENGTH:
        return this.__len__(scope, $this);
    }
    throw new MCCodeException("invalid operator " + operator);
  }

  /**
   * Method that performs the GET_ITEM operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param key   Key to get the value of.
   * @return The value for the key.
   */
  protected Object __get_item__(Scope scope, T self, Object key) {
    throw new UnsupportedOperatorException(scope, Operator.GET_ITEM, this, ProgramManager.getTypeForValue(key));
  }

  /**
   * Method that performs the SET_ITEM operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param key   Key to set the value of.
   * @param value The value to set.
   */
  protected void __set_item__(Scope scope, T self, Object key, Object value) {
    throw new UnsupportedOperatorException(scope, Operator.SET_ITEM, this, ProgramManager.getTypeForValue(key));
  }

  /**
   * Method that performs the DELETE_ITEM operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param key   Key to delete.
   */
  protected void __del_item__(Scope scope, T self, Object key) {
    throw new UnsupportedOperatorException(scope, Operator.DEL_ITEM, this, ProgramManager.getTypeForValue(key));
  }

  /**
   * Method that performs the unary "minus" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @return The result of the operator.
   */
  protected Object __minus__(Scope scope, T self) {
    throw new UnsupportedOperatorException(scope, Operator.MINUS, this);
  }

  /**
   * Method that performs the "not" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @return The result of the operator.
   */
  protected Object __not__(@SuppressWarnings("unused") Scope scope, T self) {
    return !this.toBoolean(self);
  }

  /**
   * Method that performs the "addition" operation.
   *
   * @param scope   Scope the operation is performed from.
   * @param self    Instance of this type to apply the operator to.
   * @param o       The second object.
   * @param inPlace Whether this operation should modify the instance instead of creating a new one.
   * @return The result of the operator.
   */
  protected Object __add__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.PLUS, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Method that performs the "subtraction" operation.
   *
   * @param scope   Scope the operation is performed from.
   * @param self    Instance of this type to apply the operator to.
   * @param o       The second object.
   * @param inPlace Whether this operation should modify the instance instead of creating a new one.
   * @return The result of the operator.
   */
  protected Object __sub__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.SUB, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Method that performs the "multiplication" operation.
   *
   * @param scope   Scope the operation is performed from.
   * @param self    Instance of this type to apply the operator to.
   * @param o       The second object.
   * @param inPlace Whether this operation should modify the instance instead of creating a new one.
   * @return The result of the operator.
   */
  protected Object __mul__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.MUL, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Method that performs the "division" operation.
   *
   * @param scope   Scope the operation is performed from.
   * @param self    Instance of this type to apply the operator to.
   * @param o       The second object.
   * @param inPlace Whether this operation should modify the instance instead of creating a new one.
   * @return The result of the operator.
   */
  protected Object __div__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.DIV, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Method that performs the "integer division" operation.
   *
   * @param scope   Scope the operation is performed from.
   * @param self    Instance of this type to apply the operator to.
   * @param o       The second object.
   * @param inPlace Whether this operation should modify the instance instead of creating a new one.
   * @return The result of the operator.
   */
  protected Object __intdiv__(Scope scope, T self, Object o, final boolean inPlace) {
    return (int) Math.floor(((Number) this.__div__(scope, self, o, inPlace)).doubleValue());
  }

  /**
   * Method that performs the "modulus" operation.
   *
   * @param scope   Scope the operation is performed from.
   * @param self    Instance of this type to apply the operator to.
   * @param o       The second object.
   * @param inPlace Whether this operation should modify the instance instead of creating a new one.
   * @return The result of the operator.
   */
  protected Object __mod__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.MOD, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Method that performs the "power/exponent" operation.
   *
   * @param scope   Scope the operation is performed from.
   * @param self    Instance of this type to apply the operator to.
   * @param o       The second object.
   * @param inPlace Whether this operation should modify the instance instead of creating a new one.
   * @return The result of the operator.
   */
  protected Object __pow__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.POW, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Method that performs the "equality" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  protected Object __eq__(Scope scope, T self, Object o) {
    return self == o;
  }

  /**
   * Method that performs the "inequality" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  protected Object __neq__(Scope scope, T self, Object o) {
    return !(Boolean) this.__eq__(scope, self, o);
  }

  /**
   * Method that performs the "greater than" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  protected Object __gt__(Scope scope, T self, Object o) {
    throw new UnsupportedOperatorException(scope, Operator.GT, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Method that performs the "greater than or equal to" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  protected Object __ge__(Scope scope, T self, Object o) {
    return ((Boolean) this.__gt__(scope, self, o)) || ((Boolean) this.__eq__(scope, self, o));
  }

  /**
   * Method that performs the "less than" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  protected Object __lt__(Scope scope, T self, Object o) {
    return !(Boolean) this.__ge__(scope, self, o);
  }

  /**
   * Method that performs the "less than or equal to" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  protected Object __le__(Scope scope, T self, Object o) {
    return !(Boolean) this.__gt__(scope, self, o);
  }

  /**
   * Method that performs the "in" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  protected Object __in__(Scope scope, T self, Object o) {
    throw new UnsupportedOperatorException(scope, Operator.IN, this, ProgramManager.getTypeForValue(o));
  }

  /**
   * Method that performs the "len" operation.
   * It should return the length of the given object.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @return The length of the object.
   */
  protected int __len__(Scope scope, T self) {
    throw new UnsupportedOperatorException(scope, Operator.LENGTH, this);
  }

  /**
   * Method that performs the "logical and" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  @SuppressWarnings("unused")
  protected Object __and__(Scope scope, T self, Object o) {
    if (!this.toBoolean(self)) {
      return self;
    } else {
      return o;
    }
  }

  /**
   * Method that performs the "logical or" operation.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @param o     The second object.
   * @return The result of the operator.
   */
  @SuppressWarnings("unused")
  protected Object __or__(Scope scope, T self, Object o) {
    if (this.toBoolean(self)) {
      return self;
    } else {
      return o;
    }
  }

  /**
   * Convert the given instance object to a boolean value.
   *
   * @param self An instance of this type to convert.
   * @return The boolean value for the object.
   */
  protected boolean __bool__(final T self) {
    return true;
  }

  /**
   * Method that performs the "iterator" operation.
   * It should return an iterator over the values of the given object.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @return An iterator over the values of the object..
   */
  protected Iterator<?> __iter__(Scope scope, T self) {
    throw new UnsupportedOperatorException(scope, Operator.ITERATE, this);
  }

  /**
   * Method that performs the "deep copy" operation.
   * It should return a deep copy of the given object.
   *
   * @param scope Scope the operation is performed from.
   * @param self  Instance of this type to apply the operator to.
   * @return A deep copy of the argument.
   */
  protected T __copy__(final Scope scope, final T self) {
    return self;
  }

  /**
   * Serialize an instance of this type to an NBT tag.
   *
   * @param self Instance of this type to apply the serialize.
   * @return A tag.
   */
  public NBTTagCompound writeToNBT(final Object self) {
    this.ensureType(self, String.format("attempt to serialize object of type \"%s\" from type \"%s\"",
        ProgramManager.getTypeForValue(self), this));
    //noinspection unchecked
    return this._writeToNBT((T) self);
  }

  /**
   * Serialize an instance of this type to an NBT tag.
   *
   * @param self Instance of this type to apply the serialize.
   * @return A tag.
   */
  protected NBTTagCompound _writeToNBT(final T self) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(NAME_KEY, this.getName());
    return tag;
  }

  /**
   * Create a new instance of this type by deserializing the given NBT tag.
   *
   * @param scope Scope the operation is performed from.
   * @param tag   Tag to deserialize.
   * @return A new instance of this type.
   */
  public abstract T readFromNBT(final Scope scope, final NBTTagCompound tag);

  @Override
  public String toString() {
    return this.getName();
  }

  /**
   * Raise an exception if the type of the given object does not match this type.
   */
  private void ensureType(final Object o, final String errorMessage) {
    if (ProgramManager.getTypeForValue(o) != this) {
      throw new TypeException(errorMessage);
    }
  }
}
