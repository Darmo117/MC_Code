package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.type_wrappers.IntType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Wrapper function for Java’s {@link Math#ceil(double)} function.
 */
@Doc("Returns the smallest float value that is greater than or equal to the argument and is equal to a mathematical integer.")
public class CeilFunction extends BuiltinFunction {
  /**
   * Create a function that returns the smallest float value
   * that is greater than or equal to the argument and is equal to a mathematical integer.
   */
  public CeilFunction() {
    super("ceil", ProgramManager.getTypeInstance(IntType.class), ProgramManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return (int) Math.ceil(this.getParameter(scope, 0));
  }
}
