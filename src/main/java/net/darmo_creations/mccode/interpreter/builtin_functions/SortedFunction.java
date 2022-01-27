package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.ListType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.MCList;

/**
 * A function that sorts the given iterable object.
 */
@Doc("Sorts the given iterable object. Returns a new list.")
public class SortedFunction extends BuiltinFunction {
  /**
   * Create a function that sorts the given iterable object.
   */
  public SortedFunction() {
    super("sorted", ProgramManager.getTypeInstance(ListType.class), ProgramManager.getTypeInstance(AnyType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    MCList list = ProgramManager.getTypeInstance(ListType.class).explicitCast(scope, this.getParameter(scope, 0));
    list.sort(ListType.comparator(scope, false));
    return list;
  }
}
