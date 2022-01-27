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
   *
   * @param programManager The manager this function is declared in.
   */
  public CbrtFunction(final ProgramManager programManager) {
    super("cbrt", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.cbrt(this.getParameter(scope, 0));
  }
}
