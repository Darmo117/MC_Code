package net.darmo_creations.mccode.commands;

import net.darmo_creations.mccode.MCCode;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandProgram extends CommandBase {
  @Override
  public String getName() {
    return "program";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "commands.program.usage";
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0) {
      throw new WrongUsageException(this.getUsage(sender));
    }

    Optional<Option> option = Option.fromString(args[1]);
    if (option.isPresent()) {
      args = Arrays.copyOfRange(args, 2, args.length);
      switch (option.get()) {
        case LOAD:
          this.loadProgram(sender, args);
          break;
        case UNLOAD:
          this.unloadProgram(sender, args);
          break;
        case RESET:
          this.resetProgram(sender, args);
          break;
      }
    } else {
      throw new WrongUsageException("commands.mccode.error.invalid_option", args[1]);
    }
  }

  private void loadProgram(ICommandSender sender, String[] args) {
    MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
  }

  private void unloadProgram(ICommandSender sender, String[] args) {
  }

  private void resetProgram(ICommandSender sender, String[] args) {
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return super.getTabCompletions(server, sender, args, targetPos);
  }

  enum Option {
    LOAD, UNLOAD, RESET;

    public static Optional<Option> fromString(final String s) {
      for (Option value : values()) {
        if (value.name().toLowerCase().equals(s)) {
          return Optional.of(value);
        }
      }
      return Optional.empty();
    }
  }
}
