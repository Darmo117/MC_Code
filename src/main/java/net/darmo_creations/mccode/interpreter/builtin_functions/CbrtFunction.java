package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#cbrt(double)} function.
 */
@Doc("Returns the cube root of the given value.")
public class CbrtFunction extends BuiltinFunction {
  /**
   * Create a function that returns the cube root of its parameter.
   */
  public CbrtFunction() {
    super("cbrt", ProgramManager.getTypeInstance(FloatType.class), ProgramManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.cbrt(this.getParameterValue(scope, 0));
  }
}
