package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.nodes.VariableNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SetupProgramManager.class)
class IfStatementTest extends StatementTest {
  @Test
  void writeToNBTIfOnly() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(IfStatement.ID_KEY, IfStatement.ID);
    tag.setTag(IfStatement.CONDITIONS_KEY, NodeNBTHelper.serializeNodesList(Collections.singletonList(new VariableNode("b"))));
    NBTTagList branchesList = new NBTTagList();
    List<List<Statement>> branches = Collections.singletonList(Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))));
    branches.forEach(l -> branchesList.appendTag(StatementNBTHelper.serializeStatementsList(l)));
    branchesList.appendTag(StatementNBTHelper.serializeStatementsList(Collections.emptyList()));
    tag.setTag(IfStatement.BRANCHES_KEY, branchesList);
    tag.setInteger(IfStatement.BRANCH_INDEX_KEY, -1);
    tag.setInteger(IfStatement.IP_KEY, 0);
    assertEquals(tag, new IfStatement(Collections.singletonList(new VariableNode("b")), branches, Collections.emptyList()).writeToNBT());
  }

  @Test
  void writeToNBTIfElse() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(IfStatement.ID_KEY, IfStatement.ID);
    tag.setTag(IfStatement.CONDITIONS_KEY, NodeNBTHelper.serializeNodesList(Collections.singletonList(new VariableNode("b"))));
    NBTTagList branchesList = new NBTTagList();
    List<List<Statement>> branches = Collections.singletonList(Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))));
    branches.forEach(l -> branchesList.appendTag(StatementNBTHelper.serializeStatementsList(l)));
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.SUB, new IntLiteralNode(1)));
    branchesList.appendTag(StatementNBTHelper.serializeStatementsList(elseBranch));
    tag.setTag(IfStatement.BRANCHES_KEY, branchesList);
    tag.setInteger(IfStatement.BRANCH_INDEX_KEY, -1);
    tag.setInteger(IfStatement.IP_KEY, 0);
    assertEquals(tag, new IfStatement(Collections.singletonList(new VariableNode("b")), branches, elseBranch).writeToNBT());
  }

  @Test
  void writeToNBTIfElseifElse() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(IfStatement.ID_KEY, IfStatement.ID);
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    tag.setTag(IfStatement.CONDITIONS_KEY, NodeNBTHelper.serializeNodesList(conditions));
    NBTTagList branchesList = new NBTTagList();
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    branches.forEach(l -> branchesList.appendTag(StatementNBTHelper.serializeStatementsList(l)));
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.SUB, new IntLiteralNode(1)));
    branchesList.appendTag(StatementNBTHelper.serializeStatementsList(elseBranch));
    tag.setTag(IfStatement.BRANCHES_KEY, branchesList);
    tag.setInteger(IfStatement.BRANCH_INDEX_KEY, -1);
    tag.setInteger(IfStatement.IP_KEY, 0);
    assertEquals(tag, new IfStatement(conditions, branches, elseBranch).writeToNBT());
  }

  @Test
  void constructFromNBTIfOnly() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(IfStatement.ID_KEY, IfStatement.ID);
    tag.setTag(IfStatement.CONDITIONS_KEY, NodeNBTHelper.serializeNodesList(Collections.singletonList(new VariableNode("b"))));
    NBTTagList branchesList = new NBTTagList();
    List<List<Statement>> branches = Collections.singletonList(Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))));
    branches.forEach(l -> branchesList.appendTag(StatementNBTHelper.serializeStatementsList(l)));
    branchesList.appendTag(StatementNBTHelper.serializeStatementsList(Collections.emptyList()));
    tag.setTag(IfStatement.BRANCHES_KEY, branchesList);
    tag.setInteger(IfStatement.BRANCH_INDEX_KEY, -1);
    tag.setInteger(IfStatement.IP_KEY, 0);
    assertEquals(new IfStatement(Collections.singletonList(new VariableNode("b")), branches, Collections.emptyList()), new IfStatement(tag));
  }

  @Test
  void constructFromNBTIfElse() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(IfStatement.ID_KEY, IfStatement.ID);
    tag.setTag(IfStatement.CONDITIONS_KEY, NodeNBTHelper.serializeNodesList(Collections.singletonList(new VariableNode("b"))));
    NBTTagList branchesList = new NBTTagList();
    List<List<Statement>> branches = Collections.singletonList(Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))));
    branches.forEach(l -> branchesList.appendTag(StatementNBTHelper.serializeStatementsList(l)));
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.SUB, new IntLiteralNode(1)));
    branchesList.appendTag(StatementNBTHelper.serializeStatementsList(elseBranch));
    tag.setTag(IfStatement.BRANCHES_KEY, branchesList);
    tag.setInteger(IfStatement.BRANCH_INDEX_KEY, -1);
    tag.setInteger(IfStatement.IP_KEY, 0);
    assertEquals(new IfStatement(Collections.singletonList(new VariableNode("b")), branches, elseBranch), new IfStatement(tag));
  }

  @Test
  void constructFromNBTIfElseifElse() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(IfStatement.ID_KEY, IfStatement.ID);
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    tag.setTag(IfStatement.CONDITIONS_KEY, NodeNBTHelper.serializeNodesList(conditions));
    NBTTagList branchesList = new NBTTagList();
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    branches.forEach(l -> branchesList.appendTag(StatementNBTHelper.serializeStatementsList(l)));
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.SUB, new IntLiteralNode(1)));
    branchesList.appendTag(StatementNBTHelper.serializeStatementsList(elseBranch));
    tag.setTag(IfStatement.BRANCHES_KEY, branchesList);
    tag.setInteger(IfStatement.BRANCH_INDEX_KEY, -1);
    tag.setInteger(IfStatement.IP_KEY, 0);
    assertEquals(new IfStatement(conditions, branches, elseBranch), new IfStatement(tag));
  }

  @Test
  void executeIf() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3)));
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(1, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeElseif() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, false));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, true));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3)));
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeElse() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, false));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, false));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3)));
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(3, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeNothing() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, false));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, false));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    List<Statement> elseBranch = Collections.emptyList();
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeWaitIf() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, false));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Arrays.asList(
            new WaitStatement(new IntLiteralNode(1)),
            new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
        ),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3)));
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(1, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeWaitElseif() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, false));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, true));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Arrays.asList(
            new WaitStatement(new IntLiteralNode(1)),
            new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2))
        )
    );
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3)));
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeWaitElse() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, false));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, false));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    List<Statement> elseBranch = Arrays.asList(
        new WaitStatement(new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3))
    );
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(3, this.p.getScope().getVariable("a", false));
  }

  @Test
  void resumeIf() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, false));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Arrays.asList(
            new WaitStatement(new IntLiteralNode(1)),
            new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
        ),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3)));
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
    NBTTagCompound tag = stmt.writeToNBT();
    stmt = new IfStatement(tag);
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(1, this.p.getScope().getVariable("a", false));
  }

  @Test
  void resumeElseif() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, false));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, true));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Arrays.asList(
            new WaitStatement(new IntLiteralNode(1)),
            new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2))
        )
    );
    List<Statement> elseBranch = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3)));
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
    NBTTagCompound tag = stmt.writeToNBT();
    stmt = new IfStatement(tag);
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }

  @Test
  void resumeElse() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, false));
    this.p.getScope().declareVariable(new Variable("c", false, false, false, true, false));
    List<Node> conditions = Arrays.asList(new VariableNode("b"), new VariableNode("c"));
    List<List<Statement>> branches = Arrays.asList(
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))),
        Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(2)))
    );
    List<Statement> elseBranch = Arrays.asList(
        new WaitStatement(new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.ASSIGN, new IntLiteralNode(3))
    );
    IfStatement stmt = new IfStatement(conditions, branches, elseBranch);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
    NBTTagCompound tag = stmt.writeToNBT();
    stmt = new IfStatement(tag);
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(3, this.p.getScope().getVariable("a", false));
  }
}
