package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#log10(double)} function.
 */
@Doc("Returns the base 10 logarithm of the given value.")
public class Log10Function extends BuiltinFunction {
  /**
   * Create a function that returns the base 10 logarithm of its parameter.
   */
  public Log10Function() {
    super("log10", ProgramManager.getTypeInstance(FloatType.class),
        new Parameter("x", ProgramManager.getTypeInstance(FloatType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.log10(this.getParameterValue(scope, 0));
  }
}
