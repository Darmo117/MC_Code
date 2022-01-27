package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#round(double)} function.
 */
@Doc("Returns the integer closest to the given value.")
public class RoundFunction extends BuiltinFunction {
  /**
   * Create a function that returns the integer closest to its parameter.
   *
   * @param programManager The manager this function is declared in.
   */
  public RoundFunction(final ProgramManager programManager) {
    super("round", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return (int) Math.round(this.<Double>getParameter(scope, 0));
  }
}
