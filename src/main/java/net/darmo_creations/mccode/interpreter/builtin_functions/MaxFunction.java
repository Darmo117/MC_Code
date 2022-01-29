package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.ListType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.MCList;

/**
 * A function that returns the maximum value of an iterable object.
 */
@Doc("Returns the maximum value of an iterable object.")
public class MaxFunction extends BuiltinFunction {
  /**
   * Create a function that returns the maximum value of an iterable object.
   */
  public MaxFunction() {
    super("max", ProgramManager.getTypeInstance(AnyType.class), ProgramManager.getTypeInstance(ListType.class));
  }

  @Override
  public Object apply(Scope scope) {
    MCList list = this.getParameterValue(scope, 0);
    if (list.isEmpty()) {
      throw new EvaluationException(scope, "mccode.interpreter.error.empty_list");
    }
    return list.stream().max(ListType.comparator(scope, false)).get();
  }
}
