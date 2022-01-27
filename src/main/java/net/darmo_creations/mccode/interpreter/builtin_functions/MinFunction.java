package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.ListType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.MCList;

/**
 * A function that returns the minimum value of an iterable object.
 */
@Doc("Returns the minimum value of an iterable object.")
public class MinFunction extends BuiltinFunction {
  /**
   * Create a function that returns the minimum value of an iterable object.
   *
   * @param programManager The manager this function is declared in.
   */
  public MinFunction(final ProgramManager programManager) {
    super("min", programManager.getTypeInstance(AnyType.class), programManager.getTypeInstance(ListType.class));
  }

  @Override
  public Object apply(Scope scope) {
    MCList list = this.getParameter(scope, 0);
    if (list.isEmpty()) {
      throw new MCCodeRuntimeException(scope, "mccode.interpreter.error.empty_list");
    }
    return list.stream().min(ListType.comparator(scope, false)).get();
  }
}
