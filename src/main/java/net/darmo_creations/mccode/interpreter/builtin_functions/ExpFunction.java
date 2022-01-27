package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#exp(double)} function.
 */
@Doc("Returns Euler's number e raised to the power of the given value.")
public class ExpFunction extends BuiltinFunction {
  /**
   * Create a function that returns Euler's number e raised to the power of its parameter.
   *
   * @param programManager The manager this function is declared in.
   */
  public ExpFunction(final ProgramManager programManager) {
    super("exp", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.exp(this.getParameter(scope, 0));
  }
}
