package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#tan(double)} function.
 */
@Doc("Returns the tangent of the given angle in radians.")
public class TanFunction extends BuiltinFunction {
  /**
   * Create a function that returns the tangent of its parameter.
   */
  public TanFunction() {
    super("tan", ProgramManager.getTypeInstance(FloatType.class),
        new Parameter("a", ProgramManager.getTypeInstance(FloatType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.tan(this.getParameterValue(scope, 0));
  }
}
