package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.IntType;
import net.darmo_creations.mccode.interpreter.type_wrappers.RangeType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.Range;

/**
 * A function that returns a {@link Range} object.
 */
@Doc("Returns a range object. Meant to be used in for-loop statements.")
public class RangeFunction extends BuiltinFunction {
  /**
   * Create a function that returns an integer range generator.
   */
  public RangeFunction() {
    super("range", ProgramManager.getTypeInstance(RangeType.class),
        new Parameter("start", ProgramManager.getTypeInstance(IntType.class)),
        new Parameter("end", ProgramManager.getTypeInstance(IntType.class)),
        new Parameter("step", ProgramManager.getTypeInstance(IntType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    return new Range(this.getParameterValue(scope, 0), this.getParameterValue(scope, 1), this.getParameterValue(scope, 2));
  }
}
