package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Statement that represents an if-elseif-else statement.
 */
public class IfStatement extends Statement {
  public static final int ID = 40;

  public static final String CONDITIONS_KEY = "Conditions";
  public static final String BRANCHES_KEY = "Branches";
  public static final String BRANCH_INDEX = "BranchIndex";
  public static final String IP_KEY = "IP";

  private final List<Node> conditions;
  private final List<List<Statement>> branchesStatements;
  /**
   * Index of the current branch.
   */
  private int branchIndex;
  /**
   * Instruction pointer.
   */
  private int ip;

  /**
   * Create a statement that represents an if-elseif-else statement.
   *
   * @param conditions         List of conditions for each branch.
   *                           Each expression should evaluate to boolean values.
   * @param branchesStatements List of statements for each branch.
   * @param elseStatements     Statements for the default branch.
   */
  public IfStatement(final List<Node> conditions, final List<List<Statement>> branchesStatements, final List<Statement> elseStatements) {
    this.conditions = Objects.requireNonNull(conditions);
    if (conditions.size() != branchesStatements.size()) {
      throw new MCCodeException("\"if\" statement should have the same number of branches and conditions");
    }
    branchesStatements.add(elseStatements);
    this.branchesStatements = branchesStatements;
    this.branchIndex = -1;
    this.ip = 0;
  }

  /**
   * Create a statement that represents an if-elseif-else statement from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public IfStatement(final NBTTagCompound tag) {
    this.conditions = NodeNBTHelper.deserializeNodesList(tag, CONDITIONS_KEY);
    this.branchesStatements = new ArrayList<>();
    NBTTagList list = tag.getTagList(BRANCHES_KEY, new NBTTagCompound().getId());
    for (NBTBase subList : list) {
      List<Statement> statements = new ArrayList<>();
      for (NBTBase t : (NBTTagList) subList) {
        statements.add(StatementNBTHelper.getStatementForTag((NBTTagCompound) t));
      }
      this.branchesStatements.add(statements);
    }
    this.branchIndex = tag.getInteger(BRANCH_INDEX);
    this.ip = tag.getInteger(IP_KEY);
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    if (this.branchIndex == -1) {
      for (int i = 0; i < this.conditions.size(); i++) { // Check every branch until a condition evaluates to true
        Object value = this.conditions.get(i).evaluate(scope);
        Type<?> valueType = ProgramManager.getTypeForValue(value);
        if (valueType.toBoolean(valueType)) {
          this.branchIndex = i;
        }
      }
      if (this.branchIndex == -1) { // Else branch
        this.branchIndex = this.branchesStatements.size();
      }
    }

    List<Statement> statements = this.branchesStatements.get(this.branchIndex);
    while (this.ip < statements.size()) {
      StatementAction action = statements.get(this.ip).execute(scope);
      if (action == StatementAction.EXIT_FUNCTION || action == StatementAction.WAIT) {
        if (action == StatementAction.WAIT) {
          this.ip++;
        } else {
          this.ip = 0;
        }
        return action;
      } else {
        this.ip++;
      }
    }

    this.branchIndex = -1;
    this.ip = 0;

    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(CONDITIONS_KEY, NodeNBTHelper.serializeNodesList(this.conditions));
    NBTTagList branchesList = new NBTTagList();
    this.branchesStatements.forEach(l -> branchesList.appendTag(StatementNBTHelper.serializeStatementsList(l)));
    tag.setTag(BRANCHES_KEY, branchesList);
    tag.setInteger(BRANCH_INDEX, this.branchIndex);
    tag.setInteger(IP_KEY, this.ip);
    return tag;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < this.conditions.size(); i++) {
      if (i > 0) {
        s.append("else");
      }
      s.append(String.format("if %s then", this.conditions.get(i)));
      s.append(Utils.indentStatements(this.branchesStatements.get(i)));
    }
    if (!this.branchesStatements.get(this.branchesStatements.size() - 1).isEmpty()) {
      s.append("else");
      s.append(Utils.indentStatements(this.branchesStatements.get(this.branchesStatements.size() - 1)));
    }
    s.append("end");
    return s.toString();
  }
}
