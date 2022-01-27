package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#sqrt(double)} function.
 */
@Doc("Returns the square root of the given value.")
public class SqrtFunction extends BuiltinFunction {
  /**
   * Create a function that returns the square root of its parameter.
   *
   * @param programManager The manager this function is declared in.
   */
  public SqrtFunction(final ProgramManager programManager) {
    super("sqrt", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.sqrt(this.getParameter(scope, 0));
  }
}
