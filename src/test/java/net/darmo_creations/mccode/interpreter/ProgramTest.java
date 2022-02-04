package net.darmo_creations.mccode.interpreter;

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
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
    );
    this.p = new Program("p", statements, 1, 1, new ProgramManager("pm"));
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
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1)),
        new WaitStatement(new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
    );
    Program p = new Program("p", statements, 1, 1, new ProgramManager("pm"));
    p.execute();
    assertEquals(1, p.getWaitTime());
    assertEquals(1, p.getScope().getVariable("a", false));
    p.execute();
    assertEquals(0, p.getWaitTime());
    assertEquals(1, p.getScope().getVariable("a", false));
    p.execute();
    assertEquals(0, p.getWaitTime());
    assertEquals(2, p.getScope().getVariable("a", false));
    assertTrue(p.hasTerminated());
  }

  @Test
  void execute() {
    this.p.execute();
    assertEquals(2, this.p.getScope().getVariable("a", false));
  }

  @Test
  void breakOutsideLoopError() {
    List<Statement> statements = Collections.singletonList(new BreakStatement());
    Program p = new Program("p", statements, 1, 1, new ProgramManager("pm"));
    assertThrows(SyntaxErrorException.class, p::execute);
  }

  @Test
  void continueOutsideLoopError() {
    List<Statement> statements = Collections.singletonList(new ContinueStatement());
    Program p = new Program("p", statements, 1, 1, new ProgramManager("pm"));
    assertThrows(SyntaxErrorException.class, p::execute);
  }

  @Test
  void returnOutsideFunctionError() {
    List<Statement> statements = Collections.singletonList(new ReturnStatement(new NullLiteralNode()));
    Program p = new Program("p", statements, 1, 1, new ProgramManager("pm"));
    assertThrows(SyntaxErrorException.class, p::execute);
  }

  @Test
  void reset() {
    this.p.execute();
    assertEquals(2, this.p.getScope().getVariable("a", false));
    this.p.reset();
    assertFalse(this.p.getScope().isVariableDefined("a"));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(Program.NAME_KEY, "p");
    List<Statement> statements = Arrays.asList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
    );
    tag.setTag(Program.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setTag(Program.SCOPE_KEY, this.p.getScope().writeToNBT());
    tag.setInteger(Program.SCHEDULE_DELAY_KEY, 1);
    tag.setInteger(Program.REPEAT_AMOUNT_KEY, 1);
    tag.setInteger(Program.WAIT_TIME_KEY, 0);
    tag.setInteger(Program.IP_KEY, 0);
    assertEquals(tag, this.p.writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(Program.NAME_KEY, "p");
    List<Statement> statements = Arrays.asList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1)),
        new AssignVariableStatement("a", AssigmentOperator.PLUS, new IntLiteralNode(1))
    );
    tag.setTag(Program.STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(statements));
    tag.setTag(Program.SCOPE_KEY, this.p.getScope().writeToNBT());
    tag.setInteger(Program.SCHEDULE_DELAY_KEY, 1);
    tag.setInteger(Program.REPEAT_AMOUNT_KEY, 1);
    tag.setInteger(Program.WAIT_TIME_KEY, 0);
    tag.setInteger(Program.IP_KEY, 0);
    assertEquals(new Program("p", statements, 1, 1, new ProgramManager("pm")),
        new Program(tag, new ProgramManager("pm")));
  }
}
