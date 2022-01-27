package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#abs(double)} function.
 */
@Doc("Returns the absolute value of the given value.")
public class AbsFunction extends BuiltinFunction {
  /**
   * Create a function that returns the absolute value of its parameter.
   *
   * @param programManager The manager this function is declared in.
   */
  public AbsFunction(final ProgramManager programManager) {
    super("abs", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.abs(this.<Double>getParameter(scope, 0));
  }
}
