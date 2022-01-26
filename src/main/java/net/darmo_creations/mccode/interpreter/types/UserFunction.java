package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.StackTraceElement;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.statements.ReturnStatement;
import net.darmo_creations.mccode.interpreter.statements.Statement;
import net.darmo_creations.mccode.interpreter.statements.StatementAction;
import net.darmo_creations.mccode.interpreter.statements.StatementNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class UserFunction extends Function {
  public static final int MAX_CALL_DEPTH = 100;

  private static final String PARAMETERS_KEY = "Parameters";
  private static final String STATEMENTS_KEY = "Statements";
  private static final String IP_KEY = "IP";

  private final List<Statement> statements;
  private int ip;

  public UserFunction(final ProgramManager programManager, final List<String> parameterNames, final List<Statement> statements) {
    super(null, extractParameters(programManager, parameterNames), programManager.getTypeInstance(AnyType.class));
    this.statements = Objects.requireNonNull(statements);
    this.ip = 0;
  }

  public UserFunction(final ProgramManager programManager, final NBTTagCompound tag) {
    this(programManager, extractParameterNames(tag), StatementNBTHelper.deserializeStatementsList(tag, STATEMENTS_KEY));
    this.ip = tag.getInteger(IP_KEY);
  }

  public static List<String> extractParameterNames(final NBTTagCompound tag) {
    NBTTagList statementsTag = tag.getTagList(PARAMETERS_KEY, new NBTTagString().getId());
    List<String> parametersNames = new ArrayList<>();
    for (NBTBase t : statementsTag) {
      parametersNames.add(((NBTTagString) t).getString());
    }
    return parametersNames;
  }

  @Override
  public Object apply(Scope scope) {
    List<StackTraceElement> callStack = Arrays.asList(scope.getStackTrace());
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
    this.parameters.entrySet().stream()
        .sorted(Comparator.comparing(e -> e.getValue().getLeft()))
        .forEach(e -> parametersList.appendTag(new NBTTagString(e.getKey())));
    tag.setTag(PARAMETERS_KEY, parametersList);
    tag.setTag(STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(this.statements));
    tag.setInteger(IP_KEY, this.ip);
    return tag;
  }

  @Override
  public String toString() {
    return super.toString() + String.format("{%s}", Utils.indentStatements(this.statements));
  }

  private static Map<String, Pair<Integer, ? extends Type<?>>> extractParameters(final ProgramManager programManager, final List<String> parameterNames) {
    Map<String, Pair<Integer, ? extends Type<?>>> map = new HashMap<>();
    for (int i = 0; i < parameterNames.size(); i++) {
      map.put(parameterNames.get(i), new ImmutablePair<>(i, programManager.getTypeInstance(AnyType.class)));
    }
    return map;
  }
}
