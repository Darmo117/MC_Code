package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.BooleanType;
import net.darmo_creations.mccode.interpreter.type_wrappers.ListType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.MCList;

import java.util.Comparator;

/**
 * A function that sorts the given iterable object.
 */
@Doc("Sorts the given iterable object. Returns a new list.")
public class SortedFunction extends BuiltinFunction {
  /**
   * Create a function that sorts the given iterable object.
   */
  public SortedFunction() {
    super("sorted", ProgramManager.getTypeInstance(AnyType.class),
        ProgramManager.getTypeInstance(AnyType.class), ProgramManager.getTypeInstance(BooleanType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    Object p = this.getParameterValue(scope, 0);
    boolean reversed = this.getParameterValue(scope, 1);
    if (p instanceof MCList) {
      MCList list = new MCList((MCList) p);
      list.sort(ListType.comparator(scope, reversed));
      return list;
    } else if (p instanceof String) {
      return ((String) p).codePoints()
          .mapToObj(cp -> (char) cp)
          .sorted(Comparator.comparing(cp -> reversed ? -cp : cp))
          .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
          .toString();
    }
    throw new CastException(scope, ProgramManager.getTypeInstance(ListType.class), ProgramManager.getTypeForValue(p));
  }
}
