package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.UserException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.NullType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * A function that raises an error with an attached object.
 */
@Doc("Raises an error with the given message.")
public class ErrorFunction extends BuiltinFunction {
  /**
   * Create a function that raises an error with an attached object.
   */
  public ErrorFunction() {
    super("error", ProgramManager.getTypeInstance(NullType.class),
        new Parameter("message", ProgramManager.getTypeInstance(AnyType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    Object message = this.getParameterValue(scope, 0);
    throw new UserException(
        scope,
        ProgramManager.getTypeForValue(message).toString(message),
        ProgramManager.getTypeForValue(message).copy(scope, message)
    );
  }
}
