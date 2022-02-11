package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.type_wrappers.IntType;
import net.darmo_creations.mccode.interpreter.type_wrappers.NullType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

/**
 * A function that sets the random number generator’s seed for the program it is called from.
 */
@Doc("Sets the seed of the random number generator of this program/module. " +
    "§lImportant§r: Seed will be lost if the world is unloaded while the program is running and may cause unexpected behaviors. " +
    "As such, §oit is strongly discouraged to rely on this function outside of debugging§r.")
public class SetRandomSeedFunction extends BuiltinFunction {
  /**
   * Create a function that sets the random number generator’s seed for the program it is called from.
   */
  public SetRandomSeedFunction() {
    super("set_random_seed", ProgramManager.getTypeInstance(NullType.class),
        new Parameter("seed", ProgramManager.getTypeInstance(IntType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    long seed = this.getParameterValue(scope, 0);
    scope.getProgram().setRNGSeed(seed);
    return null;
  }
}
