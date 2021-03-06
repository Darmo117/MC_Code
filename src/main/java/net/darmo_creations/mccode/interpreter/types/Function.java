package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

import java.util.*;

/**
 * Base class for functions.
 */
public abstract class Function {
  private final Type<?> returnType;
  private final String name;
  protected final List<Parameter> parameters;

  /**
   * Create a function with the given name.
   *
   * @param name       Function’s name.
   * @param parameters Functions parameters.
   * @param returnType Function’s return type.
   */
  public Function(final String name, final List<Parameter> parameters, final Type<?> returnType) {
    this.name = name == null ? "<anonymous>" : name;
    // Check for duplicate names
    Set<String> names = new HashSet<>();
    for (Parameter parameter : parameters) {
      String parameterName = parameter.getName();
      if (names.contains(parameterName)) {
        throw new MCCodeException(String.format("parameter %s declared twice in function %s", parameterName, names));
      }
      names.add(parameterName);
    }
    this.parameters = parameters;
    this.returnType = Objects.requireNonNull(returnType);
  }

  /**
   * Return this function’s name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return the return type of this function.
   */
  public Type<?> getReturnType() {
    return this.returnType;
  }

  /**
   * Return the name and type of the parameter at the given index.
   *
   * @param index Parameter’s index.
   * @return The parameter object.
   */
  public Parameter getParameter(final int index) {
    return this.parameters.get(index);
  }

  /**
   * Return the list of parameters of this function.
   */
  public List<Parameter> getParameters() {
    return this.parameters;
  }

  /**
   * Call this function in the given scope.
   *
   * @param scope The scope the function is called from.It contains variables named
   *              after this function’s parameters containing the values of the arguments.
   * @return A value.
   */
  public abstract Object apply(Scope scope);

  /**
   * Generate a parameter types list from an array of types.
   * <p>
   * This is a helper method for subclasses of this class.
   *
   * @param types The array to generate types list from.
   * @return The types list.
   */
  protected static List<Parameter> generateParameters(final Type<?>... types) {
    List<Parameter> list = new ArrayList<>();
    for (int i = 0; i < types.length; i++) {
      list.add(new Parameter(getAutoParameterNameForIndex(i), types[i]));
    }
    return list;
  }

  /**
   * Return the automatic parameter name for the given parameter index.
   * <p>
   * This is a helper method for subclasses of this class.
   *
   * @param index Parameter’s index.
   * @return Parameter’s name.
   */
  protected static String getAutoParameterNameForIndex(final int index) {
    return String.format("_x%d_", index);
  }
}
