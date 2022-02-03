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
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range"),
        Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(1), new IntLiteralNode(1)));
    List<Statement> statements = Collections.singletonList(new BreakStatement());
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ForLoopStatement.ID_KEY, ForLoopStatement.ID);
    tag.setString(ForLoopStatement.VARIABLE_NAME_KEY, "i");
    tag.setTag(ForLoopStatement.VALUES_KEY, range.writeToNBT());
    tag.setTag(ForLoopStatement.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setInteger(ForLoopStatement.IP_KEY, 0);
    tag.setInteger(ForLoopStatement.ITERATOR_INDEX_KEY, 0);
    tag.setBoolean(ForLoopStatement.PAUSED_KEY, false);
    assertEquals(tag, new ForLoopStatement("i", range, statements).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range"),
        Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(1), new IntLiteralNode(1)));
    List<Statement> statements = Collections.singletonList(new BreakStatement());
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
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range"),
        Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1)));
    List<Statement> statements = Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1)));
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("i"));
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeWait() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range"),
        Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1)));
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1)),
        new WaitStatement(new IntLiteralNode(1))
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(1, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(2, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeWait2() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range"),
        Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1)));
    List<Statement> statements = Arrays.asList(
        new WaitStatement(new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(1, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }

  @Test
  void resume() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range"),
        Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1)));
    List<Statement> statements = Arrays.asList(
        new WaitStatement(new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0, this.p.getScope().getVariable("a", false));
    NBTTagCompound tag = stmt.writeToNBT();
    stmt = new ForLoopStatement(tag);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(1, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }

  @Test
  void testBreak() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range"),
        Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1)));
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1)),
        new BreakStatement()
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("i"));
    assertEquals(1, this.p.getScope().getVariable("a", false));
  }

  @Test
  void testContinue() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0));
    FunctionCallNode range = new FunctionCallNode(new VariableNode("range"),
        Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(3), new IntLiteralNode(1)));
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1)),
        new ContinueStatement(),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
    );
    ForLoopStatement stmt = new ForLoopStatement("i", range, statements);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("i"));
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }
}
