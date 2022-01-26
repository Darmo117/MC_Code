package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.darmo_creations.mccode.interpreter.types.Function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This class represents a method of a builtin type.
 * It wraps a Java {@link Method} object annoted by the
 * {@link net.darmo_creations.mccode.interpreter.annotations.Method} annotation
 * inside a class extending {@link Type}.
 */
public class MemberFunction extends Function {
  /**
   * Name of the special variable representing the object this method belongs to.
   * Automatically generated by the interpreter.
   */
  public static final String SELF_PARAM_NAME = "$this";

  private final Type<?> targetType;
  private final Method method;
  private final String doc;

  /**
   * Create a member function (method) for a given type.
   *
   * @param targetType      Type of method’s host.
   * @param name            Method’s name.
   * @param parametersTypes Method’s parameters: a map associating a parameter name to its index and type.
   * @param returnType      Method’s return type.
   * @param method          The actual Java method.
   * @param doc             Documentation string for this method.
   */
  public MemberFunction(final Type<?> targetType, final String name, final List<? extends Type<?>> parametersTypes,
                        final Type<?> returnType, final Method method, final String doc) {
    super(Objects.requireNonNull(name), generateParameters(parametersTypes.toArray(new Type[0])), returnType);
    this.targetType = Objects.requireNonNull(targetType);
    this.method = Objects.requireNonNull(method);
    this.doc = doc;
  }

  @Override
  public Object apply(Scope scope) {
    try {
      Map<String, Object> variables = scope.getVariables();
      Object self = variables.get(SELF_PARAM_NAME);
      if (!this.targetType.getWrappedType().isAssignableFrom(self.getClass())) {
        throw new MCCodeException(String.format("method %s expected instance of type %s, got %s", this.getName(), this.targetType.getWrappedType(), self.getClass()));
      }
      variables.remove(SELF_PARAM_NAME);
      // Extract parameter variables from scope to array
      Object[] args = variables.entrySet().stream()
          // Sort parameter values to match declared parameters order
          .sorted(Comparator.comparing(e -> this.parameters.get(e.getKey()).getLeft()))
          // Cast each value to the declared parameter type
          .map(e -> this.parameters.get(e.getKey()).getRight().implicitCast(scope, e.getValue()))
          .toArray();
      return this.method.invoke(self, args);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new MCCodeException(e);
    }
  }

  public Type<?> getTargetType() {
    return this.targetType;
  }

  public Optional<String> getDoc() {
    return Optional.ofNullable(this.doc);
  }

  @Override
  public String toString() {
    return super.toString() + " {<builtin method>}";
  }
}