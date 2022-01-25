package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

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
  protected <T> T getParameter(final Scope scope, final int index) {
    String paramName = getAutoParameterNameForIndex(index);
    //noinspection unchecked
    return (T) this.parameters.get(paramName).getRight()
        .implicitCast(scope, scope.getVariable(paramName, false));
  }

  @Override
  public String toString() {
    return super.toString() + " {<builtin>}";
  }
}
