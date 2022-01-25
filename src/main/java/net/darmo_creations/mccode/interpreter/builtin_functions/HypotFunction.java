package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Interpreter;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

@Doc("Returns sqrt(x^2 + y^2)")
public class HypotFunction extends BuiltinFunction {
  public HypotFunction(final Interpreter interpreter) {
    super("hypot", interpreter.getTypeInstance(FloatType.class), interpreter.getTypeInstance(FloatType.class), interpreter.getTypeInstance(FloatType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    return Math.hypot(this.getParameter(scope, 0), this.getParameter(scope, 1));
  }
}
