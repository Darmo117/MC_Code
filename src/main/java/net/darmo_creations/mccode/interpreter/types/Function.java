package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for functions.
 */
public abstract class Function {
  private final Type<?> returnType;
  private final String name;
  protected final Map<String, Pair<Integer, ? extends Type<?>>> parameters;

  /**
   * Create a function with the given name.
   *
   * @param name       Function’s name.
   * @param parameters Functions parameters.
   * @param returnType Function’s return type.
   */
  public Function(final String name, final Map<String, Pair<Integer, ? extends Type<?>>> parameters, final Type<?> returnType) {
    this.name = name == null ? "<anonymous>" : name;
    this.parameters = parameters; // TODO check indices
    this.returnType = Objects.requireNonNull(returnType);
  }

  /**
   * Return this function’s name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return the types of the parameters of this function.
   */
  public Map<String, Pair<Integer, ? extends Type<?>>> getParametersTypes() {
    return new HashMap<>(this.parameters);
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
   * @return Parameter’s name and type.
   * @throws MCCodeException If no parameter with this index exist.
   */
  public Pair<String, ? extends Type<?>> getParameter(final int index) {
    return this.parameters.entrySet().stream()
        .filter(e -> e.getValue().getLeft() == index)
        .findFirst()
        .map(e -> new ImmutablePair<>(e.getKey(), e.getValue().getRight()))
        .orElseThrow(() -> new MCCodeException(String.format("no parameter at index %s for function %s", index, this.name)));
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
   * Generate a parameter types map from an array of types.
   * <p>
   * This is a helper method for subclasses of this class.
   *
   * @param types The array to generate types map from.
   * @return The types map.
   */
  protected static Map<String, Pair<Integer, ? extends Type<?>>> generateParameters(final Type<?>... types) {
    Map<String, Pair<Integer, ? extends Type<?>>> map = new HashMap<>();
    for (int i = 0; i < types.length; i++) {
      map.put(getAutoParameterNameForIndex(i), new ImmutablePair<>(i, types[i]));
    }
    return map;
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
