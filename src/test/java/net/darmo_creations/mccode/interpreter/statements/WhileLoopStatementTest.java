package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.nodes.BinaryOperatorNode;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.VariableNode;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SetupProgramManager.class)
class WhileLoopStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    List<Statement> statements = Collections.singletonList(new BreakStatement(0, 0));
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(WhileLoopStatement.ID_KEY, WhileLoopStatement.ID);
    tag.setTag(WhileLoopStatement.CONDITION_KEY, new VariableNode("b", 0, 0).writeToNBT());
    tag.setTag(WhileLoopStatement.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setInteger(WhileLoopStatement.IP_KEY, 0);
    tag.setBoolean(WhileLoopStatement.PAUSED_KEY, false);
    assertEquals(tag, new WhileLoopStatement(new VariableNode("b", 0, 0), statements, 0, 0).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    List<Statement> statements = Collections.singletonList(new BreakStatement(0, 0));
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(WhileLoopStatement.ID_KEY, WhileLoopStatement.ID);
    tag.setTag(WhileLoopStatement.CONDITION_KEY, new VariableNode("b", 0, 0).writeToNBT());
    tag.setTag(WhileLoopStatement.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setInteger(WhileLoopStatement.IP_KEY, 0);
    tag.setBoolean(WhileLoopStatement.PAUSED_KEY, false);
    assertEquals(new WhileLoopStatement(new VariableNode("b", 0, 0), statements, 0, 0), new WhileLoopStatement(tag));
  }

  @Test
  void execute() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("b", AssigmentOperator.ASSIGN,
            new BinaryOperatorNode(BinaryOperator.LT, new VariableNode("a", 0, 0), new IntLiteralNode(3, 0, 0), 0, 0), 0, 0)
    );
    WhileLoopStatement stmt = new WhileLoopStatement(new VariableNode("b", 0, 0), statements, 0, 0);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(3L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void executeWait() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("b", AssigmentOperator.ASSIGN,
            new BinaryOperatorNode(BinaryOperator.LT, new VariableNode("a", 0, 0), new IntLiteralNode(2, 0, 0), 0, 0), 0, 0),
        new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0)
    );
    WhileLoopStatement stmt = new WhileLoopStatement(new VariableNode("b", 0, 0), statements, 0, 0);
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
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    List<Statement> statements = Arrays.asList(
        new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("b", AssigmentOperator.ASSIGN,
            new BinaryOperatorNode(BinaryOperator.LT, new VariableNode("a", 0, 0), new IntLiteralNode(2, 0, 0), 0, 0), 0, 0)
    );
    WhileLoopStatement stmt = new WhileLoopStatement(new VariableNode("b", 0, 0), statements, 0, 0);
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
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    List<Statement> statements = Arrays.asList(
        new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("b", AssigmentOperator.ASSIGN,
            new BinaryOperatorNode(BinaryOperator.LT, new VariableNode("a", 0, 0), new IntLiteralNode(2, 0, 0), 0, 0), 0, 0)
    );
    WhileLoopStatement stmt = new WhileLoopStatement(new VariableNode("b", 0, 0), statements, 0, 0);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(0L, this.p.getScope().getVariable("a", false));
    NBTTagCompound tag = stmt.writeToNBT();
    stmt = new WhileLoopStatement(tag);
    assertEquals(StatementAction.WAIT, stmt.execute(this.p.getScope()));
    assertEquals(1L, this.p.getScope().getVariable("a", false));
    this.p.getScope().getProgram().execute();
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertEquals(2L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void testBreak() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new BreakStatement(0, 0)
    );
    WhileLoopStatement stmt = new WhileLoopStatement(new VariableNode("b", 0, 0), statements, 0, 0);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("i"));
    assertEquals(1L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void testContinue() {
    this.p.getScope().declareVariable(new Variable("a", false, false, false, true, 0L));
    this.p.getScope().declareVariable(new Variable("b", false, false, false, true, true));
    List<Statement> statements = Arrays.asList(
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0),
        new ContinueStatement(0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)
    );
    WhileLoopStatement stmt = new WhileLoopStatement(new BinaryOperatorNode(BinaryOperator.LT, new VariableNode("a", 0, 0), new IntLiteralNode(2, 0, 0), 0, 0), statements, 0, 0);
    assertEquals(StatementAction.PROCEED, stmt.execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("i"));
    assertEquals(2L, this.p.getScope().getVariable("a", false));
  }
}
