package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#log(double)} function.
 */
@Doc("Returns the natural logarithm (base e) of the given value.")
public class LogFunction extends BuiltinFunction {
  /**
   * Create a function that returns the natural logarithm (base e) of its parameter.
   *
   * @param programManager The manager this function is declared in.
   */
  public LogFunction(final ProgramManager programManager) {
    super("log", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.log(this.getParameter(scope, 0));
  }
}
