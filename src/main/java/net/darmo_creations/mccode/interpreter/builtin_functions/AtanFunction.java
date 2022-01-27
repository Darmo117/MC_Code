package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#atan(double)} function.
 */
@Doc("Returns the arc tangent of the given value.")
public class AtanFunction extends BuiltinFunction {
  /**
   * Create a function that returns the arc tangent of its parameter.
   *
   * @param programManager The manager this function is declared in.
   */
  public AtanFunction(final ProgramManager programManager) {
    super("atan", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.atan(this.getParameter(scope, 0));
  }
}
