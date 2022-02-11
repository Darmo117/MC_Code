package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.NullLiteralNode;
import net.darmo_creations.mccode.interpreter.statements.*;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class ProgramTest {
  Program p;

  @BeforeEach
  void setUp() {
    List<Statement> statements = Arrays.asList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)
    );
    this.p = new Program("p", statements, 1L, 1L, new ProgramManager(null));
  }

  @Test
  void getName() {
    assertEquals("p", this.p.getName());
  }

  @Test
  void getScheduleDelay() {
    //noinspection OptionalGetWithoutIsPresent
    assertEquals(1, this.p.getScheduleDelay().get());
  }

  @Test
  void getRepeatAmount() {
    //noinspection OptionalGetWithoutIsPresent
    assertEquals(1, this.p.getRepeatAmount().get());
  }

  @Test
  void hasTerminatedTrue() {
    this.p.execute();
    assertTrue(this.p.hasTerminated());
  }

  @Test
  void hasTerminatedFalse() {
    assertFalse(this.p.hasTerminated());
  }

  @Test
  void getWaitTime0() {
    assertEquals(0, this.p.getWaitTime());
  }

  @Test
  void testWait() {
    List<Statement> statements = Arrays.asList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0),
        new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)
    );
    Program p = new Program("p", statements, 1L, 1L, new ProgramManager(null));
    p.execute();
    assertEquals(1L, p.getWaitTime());
    assertEquals(1L, p.getScope().getVariable("a", false));
    p.execute();
    assertEquals(0L, p.getWaitTime());
    assertEquals(1L, p.getScope().getVariable("a", false));
    p.execute();
    assertEquals(0L, p.getWaitTime());
    assertEquals(2L, p.getScope().getVariable("a", false));
    assertTrue(p.hasTerminated());
  }

  @Test
  void execute() {
    this.p.execute();
    assertEquals(2L, this.p.getScope().getVariable("a", false));
  }

  @Test
  void breakOutsideLoopError() {
    List<Statement> statements = Collections.singletonList(new BreakStatement(0, 0));
    Program p = new Program("p", statements, 1L, 1L, new ProgramManager(null));
    assertThrows(SyntaxErrorException.class, p::execute);
  }

  @Test
  void continueOutsideLoopError() {
    List<Statement> statements = Collections.singletonList(new ContinueStatement(0, 0));
    Program p = new Program("p", statements, 1L, 1L, new ProgramManager(null));
    assertThrows(SyntaxErrorException.class, p::execute);
  }

  @Test
  void returnOutsideFunctionError() {
    List<Statement> statements = Collections.singletonList(new ReturnStatement(new NullLiteralNode(0, 0), 0, 0));
    Program p = new Program("p", statements, 1L, 1L, new ProgramManager(null));
    assertThrows(SyntaxErrorException.class, p::execute);
  }

  @Test
  void waitInModuleError() {
    Program p = new Program("p", Collections.singletonList(new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0)),
        new ProgramManager(null));
    assertThrows(EvaluationException.class, p::execute);
  }

  @Test
  void reset() {
    this.p.execute();
    assertEquals(2L, this.p.getScope().getVariable("a", false));
    this.p.reset();
    assertFalse(this.p.getScope().isVariableDefined("a"));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(Program.NAME_KEY, "p");
    List<Statement> statements = Arrays.asList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)
    );
    tag.setTag(Program.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setTag(Program.SCOPE_KEY, this.p.getScope().writeToNBT());
    tag.setLong(Program.SCHEDULE_DELAY_KEY, 1);
    tag.setLong(Program.REPEAT_AMOUNT_KEY, 1);
    tag.setLong(Program.WAIT_TIME_KEY, 0);
    tag.setInteger(Program.IP_KEY, 0);
    tag.setBoolean(Program.IS_MODULE_KEY, false);
    assertEquals(tag, this.p.writeToNBT());
  }

  @Test
  void writeModuleToNBT() {
    Program p = new Program("p", Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)),
        new ProgramManager(null));
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(Program.NAME_KEY, "p");
    tag.setTag(Program.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0))));
    tag.setTag(Program.SCOPE_KEY, p.getScope().writeToNBT());
    tag.setInteger(Program.IP_KEY, 0);
    tag.setBoolean(Program.IS_MODULE_KEY, true);
    assertEquals(tag, p.writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(Program.NAME_KEY, "p");
    List<Statement> statements = Arrays.asList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)
    );
    tag.setTag(Program.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setTag(Program.SCOPE_KEY, this.p.getScope().writeToNBT());
    tag.setLong(Program.SCHEDULE_DELAY_KEY, 1);
    tag.setLong(Program.REPEAT_AMOUNT_KEY, 1);
    tag.setLong(Program.WAIT_TIME_KEY, 0);
    tag.setInteger(Program.IP_KEY, 0);
    tag.setBoolean(Program.IS_MODULE_KEY, false);
    assertEquals(new Program("p", statements, 1L, 1L, new ProgramManager(null)),
        new Program(tag, new ProgramManager(null)));
  }

  @Test
  void constructModuleFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(Program.NAME_KEY, "p");
    tag.setTag(Program.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0))));
    tag.setTag(Program.SCOPE_KEY, this.p.getScope().writeToNBT());
    tag.setInteger(Program.IP_KEY, 0);
    tag.setBoolean(Program.IS_MODULE_KEY, true);
    assertEquals(new Program("p", Collections.singletonList(new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0)),
            new ProgramManager(null)),
        new Program(tag, new ProgramManager(null)));
  }
}
