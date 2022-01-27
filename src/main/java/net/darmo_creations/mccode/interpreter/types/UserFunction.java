package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.StackTraceElement;
import net.darmo_creations.mccode.interpreter.*;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.statements.ReturnStatement;
import net.darmo_creations.mccode.interpreter.statements.Statement;
import net.darmo_creations.mccode.interpreter.statements.StatementAction;
import net.darmo_creations.mccode.interpreter.statements.StatementNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserFunction extends Function {
  public static final int MAX_CALL_DEPTH = 100;

  private static final String NAME_KEY = "Name";
  private static final String PARAMETERS_KEY = "Parameters";
  private static final String STATEMENTS_KEY = "Statements";
  private static final String IP_KEY = "IP";

  private final List<Statement> statements;
  private int ip;

  public UserFunction(final String name, final ProgramManager programManager, final List<String> parameterNames, final List<Statement> statements) {
    super(name, extractParameters(programManager, parameterNames), programManager.getTypeInstance(AnyType.class));
    this.statements = Objects.requireNonNull(statements);
    this.ip = 0;
  }

  public UserFunction(final ProgramManager programManager, final NBTTagCompound tag) {
    super(tag.getString(NAME_KEY), extractParameters(programManager, tag), programManager.getTypeInstance(AnyType.class));
    this.statements = StatementNBTHelper.deserializeStatementsList(tag, STATEMENTS_KEY);
    this.ip = tag.getInteger(IP_KEY);
  }

  @Override
  public Object apply(Scope scope) {
    List<StackTraceElement> callStack = scope.getStackTrace();
    if (callStack.size() == MAX_CALL_DEPTH) {
      throw new EvaluationException(scope, "mccode.interpreter.error.stack_overflow");
    }

    List<Statement> statementList = this.statements;
    while (this.ip < statementList.size()) {
      Statement statement = statementList.get(this.ip);
      StatementAction action = statement.execute(scope);
      if (action == StatementAction.EXIT_FUNCTION) {
        break;
      }
      if (action == StatementAction.WAIT) {
        throw new MCCodeRuntimeException(scope, "mccode.interpreter.error.wait_in_function");
      }
      this.ip++;
    }
    this.ip = 0;
    if (scope.isVariableDefined(ReturnStatement.RETURN_SPECIAL_VAR_NAME)) {
      return scope.getVariable(ReturnStatement.RETURN_SPECIAL_VAR_NAME, false);
    }
    return null;
  }

  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList parametersList = new NBTTagList();
    this.parameters.stream()
        .map(Parameter::getName)
        .sorted()
        .forEach(paramName -> parametersList.appendTag(new NBTTagString(paramName)));
    tag.setTag(PARAMETERS_KEY, parametersList);
    tag.setTag(STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(this.statements));
    tag.setInteger(IP_KEY, this.ip);
    return tag;
  }

  @Override
  public String toString() {
    String params = this.parameters.stream()
        .map(Parameter::getName)
        .sorted()
        .collect(Collectors.joining(", "));
    return String.format("function %s(%s) do%send", this.getName(), params, Utils.indentStatements(this.statements));
  }

  public static List<Parameter> extractParameters(final ProgramManager programManager, final NBTTagCompound tag) {
    NBTTagList parametersTag = tag.getTagList(PARAMETERS_KEY, new NBTTagString().getId());
    List<Parameter> parameters = new ArrayList<>();
    for (NBTBase t : parametersTag) {
      parameters.add(new Parameter(((NBTTagString) t).getString(), programManager.getTypeInstance(AnyType.class)));
    }
    return parameters;
  }

  private static List<Parameter> extractParameters(final ProgramManager programManager, final List<String> parameterNames) {
    return parameterNames.stream().map(n -> new Parameter(n, programManager.getTypeInstance(AnyType.class))).collect(Collectors.toList());
  }
}
