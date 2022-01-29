package net.darmo_creations.mccode.commands;

import net.darmo_creations.mccode.MCCode;
import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.ProgramStatusException;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.parser.ExpressionParser;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

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

    Optional<Option> option = Option.fromString(args[0]);
    if (option.isPresent()) {
      args = Arrays.copyOfRange(args, 1, args.length);
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
        case RUN:
          this.runProgram(sender, args);
          break;
        case PAUSE:
          this.pauseProgram(sender, args);
          break;
        case LIST:
          if (args.length != 0) {
            throw new WrongUsageException(this.getUsage(sender));
          }
          this.listPrograms(sender);
          break;
        case GET:
          this.getVariableValue(sender, args);
          break;
        case SET:
          this.setVariableValue(sender, args);
          break;
      }
    } else {
      throw new WrongUsageException(this.getUsage(sender));
    }
  }

  private void loadProgram(ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 1) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    try {
      pm.loadProgram(programName);
    } catch (SyntaxErrorException e) {
      throw new CommandException("mccode.interpreter.error.syntax_error", programName);
    } catch (ProgramStatusException e) {
      throw new CommandException(e.getTranslationKey(), e.getProgramName());
    }
    notifyCommandListener(sender, this, "commands.program.feedback.program_loaded", programName);
  }

  private void unloadProgram(ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 1) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    try {
      pm.unloadProgram(programName);
    } catch (ProgramStatusException e) {
      throw new CommandException(e.getTranslationKey(), e.getProgramName());
    }
    notifyCommandListener(sender, this, "commands.program.feedback.program_unloaded", programName);
  }

  private void resetProgram(ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 1) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    try {
      pm.resetProgram(programName);
    } catch (ProgramStatusException e) {
      throw new CommandException(e.getTranslationKey(), e.getProgramName());
    }
    notifyCommandListener(sender, this, "commands.program.feedback.program_reset", programName);
  }

  private void runProgram(ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 1) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    try {
      pm.runProgram(programName);
    } catch (ProgramStatusException e) {
      throw new CommandException(e.getTranslationKey(), e.getProgramName());
    }
    notifyCommandListener(sender, this, "commands.program.feedback.program_launched", programName);
  }

  private void pauseProgram(ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 1) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    try {
      pm.pauseProgram(programName);
    } catch (ProgramStatusException e) {
      throw new CommandException(e.getTranslationKey(), e.getProgramName());
    }
    notifyCommandListener(sender, this, "commands.program.feedback.program_paused", programName);
  }

  private void listPrograms(ICommandSender sender) {
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String list = String.join(", ", pm.getLoadedPrograms());
    notifyCommandListener(sender, this, "commands.program.feedback.loaded_programs", list);
  }

  private void getVariableValue(ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 2) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    Optional<Program> program = pm.getProgram(programName);
    if (program.isPresent()) {
      String variableName = args[1];
      Object value;
      try {
        value = program.get().getScope().getVariable(variableName, true);
      } catch (EvaluationException e) {
        throw new CommandException(e.getTranslationKey(), e.getArgs());
      }
      notifyCommandListener(sender, this, "commands.program.feedback.get_variable_value", variableName, value);
    } else {
      throw new CommandException("mccode.interpreter.error.program_not_found", programName);
    }
  }

  private void setVariableValue(ICommandSender sender, String[] args) throws CommandException {
    if (args.length < 3) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    Optional<Program> program = pm.getProgram(programName);
    if (program.isPresent()) {
      String expression = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
      Node node;
      try {
        node = ExpressionParser.parse(expression);
      } catch (SyntaxErrorException e) {
        throw new CommandException("mccode.interpreter.error.expression_syntax_error");
      }

      Object value;
      try {
        value = node.evaluate(program.get().getScope());
        program.get().getScope().setVariable(args[1], value, true);
      } catch (EvaluationException e) {
        throw new CommandException(e.getTranslationKey(), e.getArgs());
      }
      notifyCommandListener(sender, this, "commands.program.feedback.set_variable_value", value);
    } else {
      throw new CommandException("mccode.interpreter.error.program_not_found", programName);
    }
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
    return super.getTabCompletions(server, sender, args, targetPos); // TODO
  }

  private enum Option {
    LOAD, UNLOAD, RESET, RUN, PAUSE, LIST, GET, SET;

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
