package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#asin(double)} function.
 */
@Doc("Returns the arc sine of the given value.")
public class AsinFunction extends BuiltinFunction {
  /**
   * Create a function that returns the arc sine of its parameter.
   */
  public AsinFunction() {
    super("asin", ProgramManager.getTypeInstance(FloatType.class),
        new Parameter("x", ProgramManager.getTypeInstance(FloatType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.asin(this.getParameterValue(scope, 0));
  }
}
