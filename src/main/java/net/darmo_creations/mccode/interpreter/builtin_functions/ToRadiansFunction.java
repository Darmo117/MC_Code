package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#toRadians(double)} function.
 */
@Doc("Converts the given angle in degrees to radians.")
public class ToRadiansFunction extends BuiltinFunction {
  /**
   * Create a function that converts the given angle in degrees to radians.
   *
   * @param programManager The manager this function is declared in.
   */
  public ToRadiansFunction(final ProgramManager programManager) {
    super("radians", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.toRadians(this.getParameter(scope, 0));
  }
}
