package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class ObjectProperty {
  private final String name;
  private final Type<?> type;
  private final Method getter;
  private final Method setter;
  private final String doc;

  public ObjectProperty(final String name, Type<?> type, final Method getter, final Method setter, final String doc) {
    this.name = Objects.requireNonNull(name);
    this.type = Objects.requireNonNull(type);
    this.getter = Objects.requireNonNull(getter);
    this.setter = setter;
    this.doc = doc;
  }

  public String getName() {
    return this.name;
  }

  public Optional<String> getDoc() {
    return Optional.ofNullable(this.doc);
  }

  public Object get(final Object self) {
    try {
      return this.getter.invoke(self);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new MCCodeException(e);
    }
  }

  public void set(final Scope scope, Object self, final Object value) {
    if (this.setter != null) {
      try {
        this.setter.invoke(self, this.type.implicitCast(scope, value));
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new MCCodeException(e);
      }
    } else {
      throw new EvaluationException(scope, "mccode.interpreter.error.set_property", scope.getInterpreter().getTypeForValue(self), this.name);
    }
  }
}
