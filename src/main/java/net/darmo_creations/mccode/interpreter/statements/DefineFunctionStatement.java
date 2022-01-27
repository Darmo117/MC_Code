package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.types.UserFunction;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefineFunctionStatement extends Statement {
  public static final int ID = 11;

  private static final String NAME_KEY = "Name";
  private static final String PARAMS_LIST_KEY = "Parameters";
  private static final String STATEMENTS_LIST_KEY = "Statements";

  private final String name;
  private final List<String> parametersNames;
  private final List<Statement> statements;

  public DefineFunctionStatement(final String name, final List<String> parametersNames, final List<Statement> statements) {
    this.name = Objects.requireNonNull(name);
    this.parametersNames = parametersNames;
    this.statements = statements;
  }

  public DefineFunctionStatement(final NBTTagCompound tag) {
    this.name = tag.getString(NAME_KEY);
    NBTTagList paramsTag = tag.getTagList(PARAMS_LIST_KEY, new NBTTagString().getId());
    this.parametersNames = new ArrayList<>();
    for (NBTBase t : paramsTag) {
      this.parametersNames.add(((NBTTagString) t).getString());
    }
    NBTTagList statementsTag = tag.getTagList(STATEMENTS_LIST_KEY, new NBTTagCompound().getId());
    this.statements = new ArrayList<>();
    for (NBTBase t : statementsTag) {
      this.statements.add(StatementNBTHelper.getStatementForTag((NBTTagCompound) t));
    }
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    UserFunction function = new UserFunction(scope.getProgramManager(), this.parametersNames, this.statements);
    scope.declareVariable(new Variable(this.name, false, false, true, true, function));
    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setString(NAME_KEY, this.name);
    NBTTagList paramsList = new NBTTagList();
    this.parametersNames.forEach(s -> paramsList.appendTag(new NBTTagString(s)));
    tag.setTag(PARAMS_LIST_KEY, paramsList);
    NBTTagList statementsList = new NBTTagList();
    this.statements.forEach(s -> statementsList.appendTag(s.writeToNBT()));
    tag.setTag(STATEMENTS_LIST_KEY, statementsList);
    return tag;
  }

  @Override
  public String toString() {
    String params = String.join(", ", this.parametersNames);
    String s = " ";
    if (!this.statements.isEmpty()) {
      s = Utils.indentStatements(this.statements);
    }
    return String.format("function %s(%s)%send", this.name, params, s);
  }
}
