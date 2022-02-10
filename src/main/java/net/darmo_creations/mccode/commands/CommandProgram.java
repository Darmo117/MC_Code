package net.darmo_creations.mccode.commands;

import net.darmo_creations.mccode.MCCode;
import net.darmo_creations.mccode.interpreter.MemberFunction;
import net.darmo_creations.mccode.interpreter.ObjectProperty;
import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.ProgramStatusException;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.parser.ExpressionParser;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

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
        case GET_VAR:
          this.getVariableValue(sender, args);
          break;
        case SET_VAR:
          this.setVariableValue(sender, args);
          break;
        case DELETE_VAR:
          this.deleteVariable(sender, args);
          break;
        case DOC:
          this.showDoc(sender, args);
          break;
      }
    } else {
      throw new WrongUsageException(this.getUsage(sender));
    }
  }

  private void loadProgram(ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    String alias = null;
    Object[] parsedArgs = new Object[0];
    if (args.length > 1) {
      int argsOffset = 1;
      if (args.length >= 3 && "as".equals(args[1])) {
        alias = args[2];
        argsOffset = 3;
      }
      if (argsOffset < args.length) {
        parsedArgs = this.parseArgs(Arrays.copyOfRange(args, argsOffset, args.length));
      }
    }
    try {
      pm.loadProgram(programName, alias, false, parsedArgs);
    } catch (SyntaxErrorException e) {
      throw new CommandException("mccode.interpreter.error.syntax_error", programName);
    } catch (ProgramStatusException e) {
      throw new CommandException(e.getTranslationKey(), e.getProgramName());
    }
    notifyCommandListener(sender, this, "commands.program.feedback.program_loaded", programName);
  }

  private Object[] parseArgs(String[] args) {
    Object[] parsedArgs = new Object[args.length];

    for (int i = 0, argsLength = args.length; i < argsLength; i++) {
      String arg = args[i];
      Object o;
      try {
        o = Long.parseLong(arg);
      } catch (NumberFormatException e) {
        try {
          o = Double.parseDouble(arg);
        } catch (NumberFormatException e1) {
          o = Boolean.parseBoolean(arg);
        }
      }
      parsedArgs[i] = o;
    }

    return parsedArgs;
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
      String variableName = args[1];
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
        program.get().getScope().setVariable(variableName, value, true);
      } catch (EvaluationException e) {
        throw new CommandException(e.getTranslationKey(), e.getArgs());
      }
      notifyCommandListener(sender, this, "commands.program.feedback.set_variable_value", variableName, node);
    } else {
      throw new CommandException("mccode.interpreter.error.program_not_found", programName);
    }
  }

  private void deleteVariable(ICommandSender sender, final String[] args) throws CommandException {
    if (args.length != 2) {
      throw new WrongUsageException(this.getUsage(sender));
    }

    ProgramManager pm = MCCode.INSTANCE.PROGRAM_MANAGERS.get(sender.getEntityWorld());
    String programName = args[0];
    Optional<Program> program = pm.getProgram(programName);
    if (program.isPresent()) {
      String variableName = args[1];
      program.get().getScope().deleteVariable(variableName, true);
    } else {
      throw new CommandException("mccode.interpreter.error.program_not_found", programName);
    }
  }

  private void showDoc(ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 2) {
      throw new WrongUsageException(this.getUsage(sender));
    }
    DocType docType = DocType.fromString(args[0]).orElseThrow(() -> new WrongUsageException(this.getUsage(sender)));
    String name = args[1];

    if (docType == null) {
      throw new WrongUsageException(this.getUsage(sender));
    }

    String doc;
    Object[] translationArgs;

    switch (docType) {
      case TYPE: {
        Type<?> type = ProgramManager.getTypeForName(name);
        if (type == null) {
          throw new CommandException("mccode.interpreter.error.no_type_for_name", name);
        }
        doc = type.getDoc().orElseThrow(() -> new CommandException("commands.program.error.no_doc_for_type", name));
        translationArgs = new Object[]{name};
        break;
      }

      case PROPERTY: {
        if (!name.contains(".")) {
          throw new WrongUsageException(this.getUsage(sender));
        }
        String[] parts = name.split("\\.", 2);
        String typeName = parts[0];
        String propertyName = parts[1];
        ObjectProperty property = ProgramManager.getTypeForName(typeName).getProperty(propertyName);
        if (property != null) {
          doc = property.getDoc().orElseThrow(() -> new CommandException("commands.program.error.no_doc_for_property", typeName, propertyName));
          translationArgs = new Object[]{typeName, propertyName};
        } else {
          throw new CommandException("mccode.interpreter.error.no_property_for_type", typeName, propertyName);
        }
        break;
      }

      case METHOD: {
        if (!name.contains(".")) {
          throw new WrongUsageException(this.getUsage(sender));
        }
        String[] parts = name.split("\\.", 2);
        String typeName = parts[0];
        String methodName = parts[1];
        MemberFunction method = ProgramManager.getTypeForName(typeName).getMethod(methodName);
        if (method != null) {
          doc = method.getDoc().orElseThrow(() -> new CommandException("commands.program.error.no_doc_for_method", typeName, methodName));
          translationArgs = new Object[]{typeName, methodName};
        } else {
          throw new CommandException("mccode.interpreter.error.no_method_for_type", typeName, methodName);
        }
        break;
      }

      case FUNCTION:
        BuiltinFunction function = ProgramManager.getBuiltinFunction(name);
        if (function != null) {
          doc = function.getDoc().orElseThrow(() -> new CommandException("commands.program.error.no_doc_for_function", name));
          translationArgs = new Object[]{name};
        } else {
          throw new CommandException("commands.program.error.no_function", name);
        }
        break;

      default:
        throw new WrongUsageException(this.getUsage(sender));
    }

    notifyCommandListener(sender, this, "commands.program.feedback.doc_" + docType.name().toLowerCase(), translationArgs);
    sender.sendMessage(new TextComponentString(doc));
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
    return super.getTabCompletions(server, sender, args, targetPos); // TODO
  }

  private enum Option {
    LOAD, UNLOAD, RESET, RUN, PAUSE, LIST, GET_VAR, SET_VAR, DELETE_VAR, DOC;

    public static Optional<Option> fromString(final String s) {
      for (Option value : values()) {
        if (value.name().toLowerCase().equals(s)) {
          return Optional.of(value);
        }
      }
      return Optional.empty();
    }
  }

  private enum DocType {
    TYPE, PROPERTY, METHOD, FUNCTION;

    public static Optional<DocType> fromString(final String s) {
      for (DocType value : values()) {
        if (value.name().toLowerCase().equals(s)) {
          return Optional.of(value);
        }
      }
      return Optional.empty();
    }
  }
}
