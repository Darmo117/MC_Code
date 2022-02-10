package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.statements.DeclareVariableStatement;
import net.darmo_creations.mccode.interpreter.statements.Statement;
import net.darmo_creations.mccode.interpreter.statements.WaitStatement;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class ProgramManagerTest {
  ProgramManager pm;

  @BeforeEach
  void setUp() {
    this.pm = new ProgramManager("pm");
  }

  @Test
  void loadProgram() {
    // Cannot test loadProgram(String) as it requires a running server
    Program p = new Program("p", Collections.emptyList(), null, null, this.pm);
    this.pm.loadProgram(p);
    assertTrue(this.pm.getProgram(p.getName()).isPresent());
  }

  @Test
  void unloadProgram() {
    Program p = new Program("p", Collections.emptyList(), null, null, this.pm);
    this.pm.loadProgram(p);
    assertTrue(this.pm.getProgram(p.getName()).isPresent());
    this.pm.unloadProgram(p.getName());
    assertFalse(this.pm.getProgram(p.getName()).isPresent());
  }

  @Test
  void getLoadedPrograms() {
    Program p = new Program("p", Collections.emptyList(), null, null, this.pm);
    this.pm.loadProgram(p);
    assertEquals(Collections.singletonList(p.getName()), this.pm.getLoadedPrograms());
  }

  @Test
  void resetProgram() {
    List<Statement> statements = Collections.singletonList(new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0));
    Program p = new Program("p", statements, null, null, this.pm);
    this.pm.loadProgram(p);
    p.execute();
    assertTrue(p.getScope().isVariableDefined("a"));
    this.pm.resetProgram(p.getName());
    assertFalse(p.getScope().isVariableDefined("a"));
  }

  @Test
  @Disabled("Requires running server")
  void runAndExecutePrograms() {
    List<Statement> statements = Collections.singletonList(new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0));
    Program p = new Program("p", statements, null, null, this.pm);
    this.pm.loadProgram(p);
    this.pm.runProgram(p.getName());
    assertFalse(p.hasTerminated());
    this.pm.executePrograms();
    assertFalse(this.pm.getProgram(p.getName()).isPresent());
  }

  @Test
  @Disabled("Requires running server")
  void executeProgramsWithoutRun() {
    List<Statement> statements = Collections.singletonList(new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0));
    Program p = new Program("p", statements, null, null, this.pm);
    this.pm.loadProgram(p);
    assertFalse(p.hasTerminated());
    this.pm.executePrograms();
    assertFalse(p.hasTerminated());
  }

  @Test
  @Disabled("Requires running server")
  void pauseProgram() {
    List<Statement> statements = Collections.singletonList(new WaitStatement(new IntLiteralNode(1, 0, 0), 0, 0));
    Program p = new Program("p", statements, null, null, this.pm);
    this.pm.loadProgram(p);
    this.pm.runProgram(p.getName());
    assertFalse(p.hasTerminated());
    this.pm.executePrograms();
    assertFalse(p.hasTerminated());
    this.pm.pauseProgram(p.getName());
    this.pm.executePrograms();
    assertFalse(p.hasTerminated());
    this.pm.runProgram(p.getName());
    this.pm.executePrograms();
    assertFalse(this.pm.getProgram(p.getName()).isPresent());
  }

  @Test
  @Disabled("Requires running server")
  void scheduleOnceNoDelay() {
    List<Statement> statements = Collections.singletonList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0));
    Program p = new Program("p", statements, 0L, null, this.pm);
    this.pm.loadProgram(p);
    this.pm.runProgram(p.getName());
    this.pm.executePrograms();
    this.pm.executePrograms();
    assertFalse(this.pm.getProgram(p.getName()).isPresent());
  }

  @Test
  @Disabled("Requires running server")
  void scheduleOnceDelay() {
    List<Statement> statements = Collections.singletonList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0));
    Program p = new Program("p", statements, 1L, null, this.pm);
    this.pm.loadProgram(p);
    this.pm.runProgram(p.getName());
    this.pm.executePrograms();
    assertTrue(p.getScope().isVariableDefined("a"));
    assertTrue(p.hasTerminated());
    this.pm.executePrograms();
    // Program should have been reset
    assertFalse(p.getScope().isVariableDefined("a"));
    assertFalse(p.hasTerminated());
    this.pm.executePrograms();
    assertFalse(this.pm.getProgram(p.getName()).isPresent());
  }

  @Test
  @Disabled("Requires running server")
  void repeatSeveralTimesNoDelay() {
    List<Statement> statements = Collections.singletonList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0));
    Program p = new Program("p", statements, 0L, 2L, this.pm);
    this.pm.loadProgram(p);
    this.pm.runProgram(p.getName());
    this.pm.executePrograms();
    this.pm.executePrograms();
    this.pm.executePrograms();
    assertFalse(this.pm.getProgram(p.getName()).isPresent());
  }

  @Test
  @Disabled("Requires running server")
  void repeatSeveralTimesDelay() {
    List<Statement> statements = Collections.singletonList(
        new DeclareVariableStatement(false, false, false, "a", new IntLiteralNode(1, 0, 0), 0, 0));
    Program p = new Program("p", statements, 1L, 2L, this.pm);
    this.pm.loadProgram(p);
    this.pm.runProgram(p.getName());
    this.pm.executePrograms();
    assertTrue(p.getScope().isVariableDefined("a"));
    assertTrue(p.hasTerminated());
    this.pm.executePrograms();
    // Program should have been reset
    assertFalse(p.getScope().isVariableDefined("a"));
    assertFalse(p.hasTerminated());
    this.pm.executePrograms();
    assertTrue(p.getScope().isVariableDefined("a"));
    assertTrue(p.hasTerminated());
    this.pm.executePrograms();
    // Program should have been reset
    assertFalse(p.getScope().isVariableDefined("a"));
    assertFalse(p.hasTerminated());
    this.pm.executePrograms();
    assertFalse(this.pm.getProgram(p.getName()).isPresent());
  }

  @Test
  void writeToNBT() {
    Program p = new Program("p", Collections.emptyList(), 1L, 2L, this.pm);
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList programs = new NBTTagList();
    NBTTagCompound programTag = new NBTTagCompound();
    programTag.setTag(ProgramManager.PROGRAM_KEY, p.writeToNBT());
    programTag.setLong(ProgramManager.SCHEDULE_KEY, 1);
    programTag.setLong(ProgramManager.REPEAT_AMOUNT_KEY, 2);
    programTag.setBoolean(ProgramManager.RUNNING_KEY, false);
    programs.appendTag(programTag);
    tag.setTag(ProgramManager.PROGRAMS_KEY, programs);
    this.pm.loadProgram(p);
    assertEquals(tag, this.pm.writeToNBT(new NBTTagCompound()));
  }

  @Test
  void readFromNBT() {
    ProgramManager pm = new ProgramManager("pm");
    Program p = new Program("p", Collections.emptyList(), 1L, 2L, pm);
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList programs = new NBTTagList();
    NBTTagCompound programTag = new NBTTagCompound();
    programTag.setTag(ProgramManager.PROGRAM_KEY, p.writeToNBT());
    programTag.setLong(ProgramManager.SCHEDULE_KEY, 1);
    programTag.setLong(ProgramManager.REPEAT_AMOUNT_KEY, 2);
    programTag.setBoolean(ProgramManager.RUNNING_KEY, false);
    programs.appendTag(programTag);
    tag.setTag(ProgramManager.PROGRAMS_KEY, programs);
    pm.loadProgram(p);
    ProgramManager pm2 = new ProgramManager("pm");
    pm2.readFromNBT(tag);
    assertEquals(pm.writeToNBT(new NBTTagCompound()), pm2.writeToNBT(new NBTTagCompound()));
  }
}
