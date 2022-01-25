package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Interpreter;
import net.darmo_creations.mccode.interpreter.MemberFunction;
import net.darmo_creations.mccode.interpreter.ObjectProperty;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.*;
import net.darmo_creations.mccode.interpreter.types.BoundMemberFunction;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

/**
 * A type is a class that wraps a data type to make it usable from programs.
 * It may possess properties and methods annoted with {@link Property} and {@link Method}.
 * <p>
 * Once declared in an {@link Interpreter} instance it becomes available to programs of this interpreter.
 *
 * @param <T> Type of data wrapped by this class.
 */
public abstract class Type<T> {
  public static final String NAME_KEY = "Name";

  // Set by Interpreter.processTypeAnnotations() method
  @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
  private Map<String, ObjectProperty> properties;
  // Set by Interpreter.processTypeAnnotations() method
  @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
  private Map<String, MemberFunction> methods;
  // Set by Interpreter.processTypeAnnotations() method
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
    this.ensureType(scope.getInterpreter(), self,
        String.format("attempt to get property from type \"%s\" for object of type \"%s\"", this, self));
    if (this.properties.containsKey(propertyName)) {
      return this.properties.get(propertyName).get(self);
    } else if (this.methods.containsKey(propertyName)) { // TODO do not return methods
      return new BoundMemberFunction(self, this.methods.get(propertyName));
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
   * @throws MCCodeRuntimeException If the instance object does not have a property with the given name, or the property is a member function.
   */
  public void setProperty(final Scope scope, final Object self, final String propertyName, final Object value) {
    this.ensureType(scope.getInterpreter(), self,
        String.format("attempt to get property from type \"%s\" for object of type \"%s\"", this, self));
    if (this.properties.containsKey(propertyName)) {
      this.properties.get(propertyName).set(scope, self, value);
    } else if (this.methods.containsKey(propertyName)) { // TODO delete as methods should not be returned as property values
      throw new EvaluationException(scope, "mccode.interpreter.error.set_method_property", this, propertyName);
    }
    throw new EvaluationException(scope, "mccode.interpreter.error.no_property_for_type", this, propertyName);
  }

  public MemberFunction getMethod(final String name) {
    return this.methods.get(name);
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
    throw new CastException(this, scope.getInterpreter().getTypeForValue(o));
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
   * @apiNote API method to circumvent generic type restrictions.
   */
  final boolean toBoolean(final Object self) {
    //noinspection unchecked
    return this.__bool__((T) self);
  }

  public Object applyOperator(final Scope scope, final Operator operator, Object self, Object o, final boolean inPlace) {
    if (scope.getInterpreter().getTypeForValue(self) != this) {
      throw new MCCodeException("mismatch types");
    }
    //noinspection unchecked
    T $this = (T) self;

    switch (operator) {
      case MINUS:
        return this.__minus__(scope, $this);
      case NOT:
        return this.__not__(scope, $this);
      case PLUS:
        return this.__add__(scope, $this, o, inPlace);
      case SUB:
        return this.__sub__(scope, $this, o, inPlace);
      case MUL:
        return this.__mul__(scope, $this, o, inPlace);
      case DIV:
        return this.__div__(scope, $this, o, inPlace);
      case INT_DIV:
        return this.__intdiv__(scope, $this, o, inPlace);
      case MOD:
        return this.__mod__(scope, $this, o, inPlace);
      case POW:
        return this.__pow__(scope, $this, o, inPlace);
      case EQUAL:
        return this.__eq__(scope, $this, o);
      case NOT_EQUAL:
        return this.__neq__(scope, $this, o);
      case GT:
        return this.__gt__(scope, $this, o);
      case GE:
        return this.__ge__(scope, $this, o);
      case LT:
        return this.__lt__(scope, $this, o);
      case LE:
        return this.__le__(scope, $this, o);
      case IN:
        return this.__in__(scope, $this, o);
      case NOT_IN:
        return !this.toBoolean(this.__in__(scope, $this, o));
      case AND:
        return this.__and__(scope, $this, o);
      case OR:
        return this.__or__(scope, $this, o);
      case GET_ITEM:
        return this.__get_item__(scope, $this, o);
      case ITERATE:
        return this.__iter__(scope, $this);
    }
    throw new MCCodeException("invalid operator " + operator);
  }

  public void setItem(final Scope scope, Object self, final Object key, final Object value) {
    if (scope.getInterpreter().getTypeForValue(self) != this) {
      throw new MCCodeException("mismatch types");
    }
    //noinspection unchecked
    this.__set_item__(scope, (T) self, key, value);
  }

  public void deleteItem(final Scope scope, Object self, final Object key) {
    if (scope.getInterpreter().getTypeForValue(self) != this) {
      throw new MCCodeException("mismatch types");
    }
    //noinspection unchecked
    this.__del_item__(scope, (T) self, key);
  }

  protected Object __get_item__(Scope scope, T self, Object key) {
    throw new UnsupportedOperatorException(scope, Operator.PLUS, this, scope.getInterpreter().getTypeForValue(key));
  }

  protected void __set_item__(Scope scope, T self, Object key, Object value) {
    throw new UnsupportedOperatorException(scope, Operator.PLUS, this, scope.getInterpreter().getTypeForValue(key));
  }

  protected void __del_item__(Scope scope, T self, Object key) {
    throw new UnsupportedOperatorException(scope, Operator.PLUS, this, scope.getInterpreter().getTypeForValue(key));
  }

  protected Object __minus__(Scope scope, T self) {
    throw new UnsupportedOperatorException(scope, Operator.MINUS, this);
  }

  protected Object __not__(Scope scope, T self) {
    return !this.toBoolean(self);
  }

  protected Object __add__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.PLUS, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __sub__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.SUB, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __mul__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.MUL, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __div__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.DIV, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __intdiv__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.INT_DIV, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __mod__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.MOD, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __pow__(Scope scope, T self, Object o, final boolean inPlace) {
    throw new UnsupportedOperatorException(scope, Operator.POW, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __eq__(Scope scope, T self, Object o) {
    return self == o;
  }

  protected Object __neq__(Scope scope, T self, Object o) {
    return !this.toBoolean(this.__eq__(scope, self, o));
  }

  protected Object __gt__(Scope scope, T self, Object o) {
    throw new UnsupportedOperatorException(scope, Operator.GT, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __ge__(Scope scope, T self, Object o) {
    return this.toBoolean(this.__gt__(scope, self, o)) || this.toBoolean(this.__eq__(scope, self, o));
  }

  protected Object __lt__(Scope scope, T self, Object o) {
    return !this.toBoolean(this.__ge__(scope, self, o));
  }

  protected Object __le__(Scope scope, T self, Object o) {
    return !this.toBoolean(this.__gt__(scope, self, o));
  }

  protected Object __in__(Scope scope, T self, Object o) {
    throw new UnsupportedOperatorException(scope, Operator.IN, this, scope.getInterpreter().getTypeForValue(o));
  }

  protected Object __and__(Scope scope, T self, Object o) {
    return this.toBoolean(self) && scope.getInterpreter().getTypeForValue(o).toBoolean(o);
  }

  protected Object __or__(Scope scope, T self, Object o) {
    return this.toBoolean(self) || scope.getInterpreter().getTypeForValue(o).toBoolean(o);
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

  protected Iterator<?> __iter__(Scope scope, T self) {
    throw new UnsupportedOperatorException(scope, Operator.ITERATE, this);
  }

  public NBTTagCompound writeToNBT(final Scope scope, final Object self) {
    Interpreter interpreter = scope.getInterpreter();
    this.ensureType(interpreter, self,
        String.format("attempt to serialize object of type \"%s\" from type \"%s\"", interpreter.getTypeForValue(self), this));
    //noinspection unchecked
    return this._writeToNBT(scope, (T) self);
  }

  protected NBTTagCompound _writeToNBT(final Scope scope, final T self) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(NAME_KEY, this.getName());
    return tag;
  }

  public abstract T readFromNBT(final Scope scope, final NBTTagCompound tag);

  @Override
  public String toString() {
    return this.getName();
  }

  private void ensureType(final Interpreter interpreter, final Object o, final String errorMessage) {
    if (interpreter.getTypeForValue(o) != this) {
      throw new TypeException(errorMessage);
    }
  }
}
