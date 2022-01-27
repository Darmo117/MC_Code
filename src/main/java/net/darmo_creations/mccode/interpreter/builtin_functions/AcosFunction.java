package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#acos(double)} function.
 */
@Doc("Returns the arc cosine of the given value.")
public class AcosFunction extends BuiltinFunction {
  /**
   * Create a function that returns the arc cosine of its parameter.
   *
   * @param programManager The manager this function is declared in.
   */
  public AcosFunction(final ProgramManager programManager) {
    super("acos", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.acos(this.getParameter(scope, 0));
  }
}