package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.NullType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * A function that prints text to the console.
 */
public class PrintFunction extends BuiltinFunction {
  /**
   * Create a function that prints text to the console.
   */
  public PrintFunction() {
    super("print", ProgramManager.getTypeInstance(NullType.class), ProgramManager.getTypeInstance(AnyType.class));
  }

  @Override
  public Object apply(final Scope scope) {
    Program program = scope.getProgram();
    MinecraftServer minecraftServer = program.getProgramManager().getWorld().getMinecraftServer();
    if (minecraftServer != null) {
      String text = String.format("[MCCode Program %s] %s", program.getName(), this.getParameterValue(scope, 0));
      minecraftServer.sendMessage(new TextComponentString(text));
    }
    return null;
  }
}
