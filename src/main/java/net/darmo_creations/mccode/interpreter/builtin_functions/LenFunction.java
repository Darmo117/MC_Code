package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.IntType;
import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * A function that returns the length of the given object.
 * The argument object’s wrapper type must implement the __len__ method or else an exception will be thrown.
 */
@Doc("Returns the number of elements of the given collection.")
public class LenFunction extends BuiltinFunction {
  /**
   * Create a function that returns the length of a collection.
   */
  public LenFunction() {
    super("len", ProgramManager.getTypeInstance(IntType.class), ProgramManager.getTypeInstance(AnyType.class));
  }

  @Override
  public Object apply(Scope scope) {
    Object parameter = this.getParameterValue(scope, 0);
    return ProgramManager.getTypeForValue(parameter).applyOperator(scope, Operator.LENGTH, parameter, null, null, false);
  }
}
