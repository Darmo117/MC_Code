package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
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

public class IfStatement extends Statement {
  public static final int ID = 40;

  public static final String CONDITIONS_KEY = "Conditions";
  public static final String BRANCHES_KEY = "Branches";
  public static final String BRANCH_INDEX = "BranchIndex";
  public static final String IP_KEY = "IP";

  private final List<Node> conditions;
  private final List<List<Statement>> branchesStatements;
  private int branchIndex;
  private int ip;

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
      for (int i = 0; i < this.conditions.size(); i++) {
        Object value = this.conditions.get(i).evaluate(scope);
        Type<?> valueType = scope.getInterpreter().getTypeForValue(value);
        if (valueType.toBoolean(valueType)) {
          this.branchIndex = i;
        }
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
    return null; // TODO
  }
}
