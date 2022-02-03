package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
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
    super("reversed", ProgramManager.getTypeInstance(AnyType.class), ProgramManager.getTypeInstance(AnyType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    Object p = this.getParameterValue(scope, 0);
    if (p instanceof MCList) {
      MCList list = (MCList) p;
      MCList res = new MCList();
      list.forEach(v -> res.add(0, v));
      return res;
    } else if (p instanceof String) {
      StringBuilder sb = new StringBuilder();
      ((String) p).codePoints().forEach(cp -> sb.insert(0, (char) cp));
      return sb.toString();
    }
    throw new CastException(scope, ProgramManager.getTypeInstance(ListType.class), ProgramManager.getTypeForValue(p));
  }
}
