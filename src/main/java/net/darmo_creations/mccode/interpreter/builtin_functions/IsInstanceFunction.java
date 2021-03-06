package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.BooleanType;
import net.darmo_creations.mccode.interpreter.type_wrappers.StringType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * Function that checks whether the given object is of the provided type.
 */
@Doc("Checks whether the given object is of the provided type.")
public class IsInstanceFunction extends BuiltinFunction {
  /**
   * Create a function that checks whether the given object is of the provided type.
   */
  public IsInstanceFunction() {
    super("is_instance", ProgramManager.getTypeInstance(BooleanType.class),
        new Parameter("type_name", ProgramManager.getTypeInstance(StringType.class)),
        new Parameter("o", ProgramManager.getTypeInstance(AnyType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    Class<?> targetType = ProgramManager.getTypeForName(this.getParameterValue(scope, 0)).getWrappedType();
    Class<?> valueType = ProgramManager.getTypeForValue(this.getParameterValue(scope, 1)).getWrappedType();
    return targetType.isAssignableFrom(valueType);
  }
}
