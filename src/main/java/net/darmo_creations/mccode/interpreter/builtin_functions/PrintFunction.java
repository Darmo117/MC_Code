package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.NullType;
import net.darmo_creations.mccode.interpreter.type_wrappers.StringType;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * A function that prints text to the console and/or chat.
 */
public class PrintFunction extends BuiltinFunction {
  /**
   * Create a function that prints text to the console and/or chat.
   */
  public PrintFunction() {
    super("print", ProgramManager.getTypeInstance(NullType.class),
        new Parameter("message", ProgramManager.getTypeInstance(AnyType.class)),
        new Parameter("channel", ProgramManager.getTypeInstance(StringType.class)));
  }

  @Override
  public Object apply(final Scope scope) {
    Program program = scope.getProgram();
    MinecraftServer minecraftServer = program.getProgramManager().getWorld().getMinecraftServer();
    if (minecraftServer != null) {
      Object message = this.getParameterValue(scope, 0);
      String text = ProgramManager.getTypeForValue(message).toString(message);
      Channel channel = Channel.fromString(this.getParameterValue(scope, 1));
      if (channel == Channel.CHAT || channel == Channel.BOTH) {
        scope.getProgram().getProgramManager().getWorld().getPlayers(EntityPlayer.class, p -> true)
            .forEach(player -> player.sendMessage(new TextComponentString(text)));
      }
      if (channel == Channel.CONSOLE || channel == Channel.BOTH) {
        minecraftServer.sendMessage(new TextComponentString(String.format("[MCCode Program %s] %s", program.getName(), text)));
      }
    }
    return null;
  }

  public enum Channel {
    CHAT, CONSOLE, BOTH;

    public static Channel fromString(final String s) {
      for (Channel value : values()) {
        if (value.name().toUpperCase().equals(s)) {
          return value;
        }
      }
      return null;
    }
  }
}
