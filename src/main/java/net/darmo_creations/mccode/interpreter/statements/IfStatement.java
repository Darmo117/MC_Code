package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
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
  public static final String BRANCH_INDEX_KEY = "BranchIndex";
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
   * @param line               The line this statement starts on.
   * @param column             The column in the line this statement starts at.
   */
  public IfStatement(final List<Node> conditions, final List<List<Statement>> branchesStatements,
                     final List<Statement> elseStatements, final int line, final int column) {
    super(line, column);
    this.conditions = Objects.requireNonNull(conditions);
    if (conditions.size() != branchesStatements.size()) {
      throw new MCCodeException("\"if\" statement should have the same number of branches and conditions");
    }
    this.branchesStatements = new ArrayList<>(branchesStatements);
    this.branchesStatements.add(elseStatements);
    this.branchIndex = -1;
    this.ip = 0;
  }

  /**
   * Create a statement that represents an if-elseif-else statement from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public IfStatement(final NBTTagCompound tag) {
    super(tag);
    this.conditions = NodeNBTHelper.deserializeNodesList(tag, CONDITIONS_KEY);
    this.branchesStatements = new ArrayList<>();
    NBTTagList list = tag.getTagList(BRANCHES_KEY, new NBTTagList().getId());
    for (NBTBase subList : list) {
      List<Statement> statements = new ArrayList<>();
      for (NBTBase t : (NBTTagList) subList) {
        statements.add(StatementNBTHelper.getStatementForTag((NBTTagCompound) t));
      }
      this.branchesStatements.add(statements);
    }
    if (this.conditions.size() != this.branchesStatements.size() - 1) {
      throw new MCCodeException("\"if\" statement should have the same number of branches and conditions");
    }
    this.branchIndex = tag.getInteger(BRANCH_INDEX_KEY);
    this.ip = tag.getInteger(IP_KEY);
  }

  @Override
  protected StatementAction executeWrapped(Scope scope) {
    if (this.branchIndex == -1) {
      for (int i = 0; i < this.conditions.size(); i++) { // Check every branch until a condition evaluates to true
        Object value = this.conditions.get(i).evaluate(scope);
        Type<?> valueType = ProgramManager.getTypeForValue(value);
        if (valueType.toBoolean(value)) {
          this.branchIndex = i;
          break;
        }
      }
      if (this.branchIndex == -1) { // Else branch
        this.branchIndex = this.branchesStatements.size() - 1;
      }
    }

    List<Statement> statements = this.branchesStatements.get(this.branchIndex);
    while (this.ip < statements.size()) {
      Statement statement = statements.get(this.ip);
      StatementAction action = statement.execute(scope);
      if (action == StatementAction.EXIT_FUNCTION || action == StatementAction.WAIT
          || action == StatementAction.EXIT_LOOP || action == StatementAction.CONTINUE_LOOP) {
        if (action == StatementAction.WAIT) {
          if (statement instanceof WaitStatement) {
            this.ip++;
          }
        } else {
          this.branchIndex = -1;
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
    tag.setInteger(BRANCH_INDEX_KEY, this.branchIndex);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    IfStatement that = (IfStatement) o;
    return this.branchIndex == that.branchIndex && this.ip == that.ip && this.conditions.equals(that.conditions)
        && this.branchesStatements.equals(that.branchesStatements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.conditions, this.branchesStatements, this.branchIndex, this.ip);
  }
}
