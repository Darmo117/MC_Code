package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.statements.Statement;
import net.darmo_creations.mccode.interpreter.statements.StatementNBTHelper;
import net.darmo_creations.mccode.interpreter.types.UserFunction;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Node} representing a function literal.
 */
public class FunctionLiteralNode extends Node {
  public static final int ID = 8;

  private static final String PARAMS_LIST_KEY = "Parameters";
  private static final String STATEMENTS_LIST_KEY = "Statements";

  private final List<String> parametersNames;
  private final List<Statement> statements;

  /**
   * Create a function literal {@link Node}.
   *
   * @param parametersNames List of function’s parameter names.
   * @param statements      Function’s statements.
   */
  public FunctionLiteralNode(final List<String> parametersNames, final List<Statement> statements) {
    this.parametersNames = new ArrayList<>(parametersNames);
    this.statements = new ArrayList<>(statements);
  }

  /**
   * Create a function literal {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public FunctionLiteralNode(final NBTTagCompound tag) {
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
  public Object evaluate(Scope scope) throws EvaluationException, ArithmeticException {
    return new UserFunction(this.parametersNames, this.statements, scope);
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
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
    return String.format("function (%s) {%s}", params, Utils.indentStatements(this.statements));
  }
}
