package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#cos(double)} function.
 */
@Doc("Returns the cosine of the given angle in radians.")
public class CosFunction extends BuiltinFunction {
  /**
   * Create a function that returns the cosine of its parameter.
   *
   * @param programManager The manager this function is declared in.
   */
  public CosFunction(final ProgramManager programManager) {
    super("cos", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.cos(this.getParameter(scope, 0));
  }
}
