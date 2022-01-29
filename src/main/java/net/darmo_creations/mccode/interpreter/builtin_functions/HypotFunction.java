package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#hypot(double, double)} function.
 */
@Doc("Returns sqrt(x² + y²).")
public class HypotFunction extends BuiltinFunction {
  /**
   * Create a function that returns sqrt(x² + y²).
   */
  public HypotFunction() {
    super("hypot", ProgramManager.getTypeInstance(FloatType.class),
        ProgramManager.getTypeInstance(FloatType.class), ProgramManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.hypot(this.getParameterValue(scope, 0), this.getParameterValue(scope, 1));
  }
}
