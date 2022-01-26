package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

@Doc("Returns sqrt(x^2 + y^2)")
public class HypotFunction extends BuiltinFunction {
  public HypotFunction(final ProgramManager programManager) {
    super("hypot", programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class), programManager.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.hypot(this.getParameter(scope, 0), this.getParameter(scope, 1));
  }
}
