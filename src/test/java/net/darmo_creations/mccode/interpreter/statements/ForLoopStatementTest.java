package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.nodes.FunctionCallNode;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.VariableNode;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SetupProgramManager.class)
class ForLoopStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range", 0, 0),
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(1, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0);
    List<Statement> statements = Collections.singletonList(new BreakStatement(0, 0));
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ForLoopStatement.ID_KEY, ForLoopStatement.ID);
    tag.setString(ForLoopStatement.VARIABLE_NAME_KEY, "i");
    tag.setTag(ForLoopStatement.VALUES_KEY, range.writeToNBT());
    tag.setTag(ForLoopStatement.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setInteger(ForLoopStatement.IP_KEY, 0);
    tag.setInteger(ForLoopStatement.ITERATOR_INDEX_KEY, 0);
    tag.setBoolean(ForLoopStatement.PAUSED_KEY, false);
    assertEquals(tag, new ForLoopStatement("i", range, statements, 0, 0).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range", 0, 0),
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(1, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0);
    List<Statement> statements = Collections.singletonList(new BreakStatement(0, 0));
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ForLoopStatement.ID_KEY, ForLoopStatement.ID);
    tag.setString(ForLoopStatement.VARIABLE_NAME_KEY, "i");
    tag.setTag(ForLoopStatement.VALUES_KEY, range.writeToNBT());
    tag.setTag(ForLoopStatement.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setInteger(ForLoopStatement.IP_KEY, 0);
    tag.setInteger(ForLoopStatement.ITERATOR_INDEX_KEY, 0);
    tag.setBoolean(ForLoopStatement.PAUSED_KEY, false);
    assertEquals(tag, new ForLoopStatement(tag).writeToNBT());
  }

  @Test
  void execute() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range", 0, 0),
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0);
    List<Statement> statements = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0));
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements, 0, 0);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("i"));
    assertEquals(2L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeWait() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range", 0, 0),
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0);
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0)
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements, 0, 0);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(1L, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(2L, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeWait2() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range", 0, 0),
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0);
    List<Statement> statements = Arrays.asList(
        new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements, 0, 0);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0L, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(1L, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void resume() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range", 0, 0),
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0);
    List<Statement> statements = Arrays.asList(
        new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements, 0, 0);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0L, this.p.getScope().getVariable("a", false));
    NBTTagCompound tag = stmt.writeToNBT();
    stmt = new ForLoopStatement(tag);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(1L, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void testBreak() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range", 0, 0),
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0);
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new BreakStatement(0, 0)
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements, 0, 0);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("i"));
    assertEquals(1L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void testContinue() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range", 0, 0),
        Arrays.asList(new IntLiteralNode(1, 0, 0), new IntLiteralNode(3, 0, 0), new IntLiteralNode(1, 0, 0)), 0, 0);
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new ContinueStatement(0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements, 0, 0);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("i"));
    assertEquals(2L, this.p.getScope().getVariable("a", false));
  }
}
