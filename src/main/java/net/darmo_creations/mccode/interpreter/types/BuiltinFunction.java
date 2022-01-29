package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Base class for builtin functions.
 */
public abstract class BuiltinFunction extends Function {
  /**
   * Create a builtin function.
   *
   * @param name           Function’s name.
   * @param returnType     Function’s return type.
   * @param parameterTypes Function’s parameter types. Parameter names are generated:
   *                       {@code _x&lt;i>_} where {@code i} is the parameter’s index.
   */
  public BuiltinFunction(final String name, final Type<?> returnType, final Type<?>... parameterTypes) {
    super(name, generateParameters(parameterTypes), returnType);
  }

  /**
   * Return the value of the given parameter.
   *
   * @param scope Scope the function is called from.
   * @param index Parameter’s index.
   * @param <T>   Parameter’s wrapped type.
   * @return Parameter’s value.
   */
  protected <T> T getParameterValue(final Scope scope, final int index) {
    //noinspection unchecked
    return (T) this.parameters.get(index).getType()
        .implicitCast(scope, scope.getVariable(getAutoParameterNameForIndex(index), false));
  }

  @Override
  public String toString() {
    String params = this.parameters.stream()
        .sorted(Comparator.comparing(Parameter::getName))
        .map(p -> p.getType().getName() + " " + p.getName())
        .collect(Collectors.joining(", "));
    return String.format("builtin function %s(%s) -> %s", this.getName(), params, this.getReturnType());
  }
}
