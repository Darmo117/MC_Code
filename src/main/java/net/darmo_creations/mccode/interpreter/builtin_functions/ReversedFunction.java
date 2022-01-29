package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.ListType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.MCList;

/**
 * A function that reverses the given iterable object.
 */
@Doc("Reverses the order of the given iterable object. Returns a new list.")
public class ReversedFunction extends BuiltinFunction {
  /**
   * Create a function that reverses the given iterable object.
   */
  public ReversedFunction() {
    super("reversed", ProgramManager.getTypeInstance(ListType.class), ProgramManager.getTypeInstance(AnyType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    MCList list = ProgramManager.getTypeInstance(ListType.class).explicitCast(scope, this.getParameterValue(scope, 0));
    list.sort(ListType.comparator(scope, true));
    return list;
  }
}
