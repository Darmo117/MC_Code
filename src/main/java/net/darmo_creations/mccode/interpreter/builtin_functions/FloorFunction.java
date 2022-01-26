package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#floor(double)} function.
 */
@Doc("Returns the largest float value that is less than or equal to the argument and is equal to a mathematical integer.")
public class FloorFunction extends BuiltinFunction {
  /**
   * Create a function that returns largest float value
   * that is less than or equal to the argument and is equal to a mathematical integer.
   *
   * @param programManager The interpreter this function is declared in.
   */
  public FloorFunction(final ProgramManager programManager) {
    super("floor", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.floor(this.getParameter(scope, 0));
  }
}