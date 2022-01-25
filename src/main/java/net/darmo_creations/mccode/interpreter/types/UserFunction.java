package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.Interpreter;
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
  private static final String CLOSURE_KEY = "Closure";
  private static final String IP_KEY = "IP";

  private final List<Statement> statements;
  private final Scope closure;
  private int ip;

  public UserFunction(final List<String> parameterNames, final List<Statement> statements, final Scope closure) {
    super(null, extractParameters(closure.getInterpreter(), parameterNames), closure.getInterpreter().getTypeInstance(AnyType.class));
    this.statements = Objects.requireNonNull(statements);
    this.closure = closure;
    this.ip = 0;
  }

  public UserFunction(final NBTTagCompound tag) {
    this(extractParameterNames(tag), extractStatement(tag), extractClosure(tag));
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

  public static List<Statement> extractStatement(final NBTTagCompound tag) {
    NBTTagList statementsTag = tag.getTagList(STATEMENTS_KEY, new NBTTagCompound().getId());
    List<Statement> statements = new ArrayList<>();
    for (NBTBase t : statementsTag) {
      statements.add(StatementNBTHelper.getStatementForTag((NBTTagCompound) t));
    }
    return statements;
  }

  public static Scope extractClosure(final NBTTagCompound tag) {
    // TODO deserialize closure -> problem with references
    return null;
  }

  public Scope getClosure() {
    return this.closure;
  }

  // TODO reload state after world reload or "wait" statement
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
    NBTTagList statementsList = new NBTTagList();
    this.statements.forEach(s -> statementsList.appendTag(s.writeToNBT()));
    tag.setTag(STATEMENTS_KEY, statementsList);
    tag.setInteger(IP_KEY, this.ip);
    tag.setTag(CLOSURE_KEY, this.closure.writeToNBT());
    tag.setInteger(IP_KEY, this.ip);
    return tag;
  }

  @Override
  public String toString() {
    return super.toString() + String.format("{%s}", Utils.indentStatements(this.statements));
  }

  private static Map<String, Pair<Integer, ? extends Type<?>>> extractParameters(final Interpreter interpreter, final List<String> parameterNames) {
    Map<String, Pair<Integer, ? extends Type<?>>> map = new HashMap<>();
    for (int i = 0; i < parameterNames.size(); i++) {
      map.put(parameterNames.get(i), new ImmutablePair<>(i, interpreter.getTypeInstance(AnyType.class)));
    }
    return map;
  }
}
