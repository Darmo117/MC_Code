package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#toDegrees(double)} function.
 */
@Doc("Converts the given angle in radians to degrees.")
public class ToDegreesFunction extends BuiltinFunction {
  /**
   * Create a function that converts the given angle in radians to degrees.
   */
  public ToDegreesFunction() {
    super("to_degrees", ProgramManager.getTypeInstance(FloatType.class),
        new Parameter("radians", ProgramManager.getTypeInstance(FloatType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.toDegrees(this.getParameterValue(scope, 0));
  }
}
