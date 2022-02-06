package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ScopeTest {
  Scope s;
  Scope sub;

  @BeforeEach
  void setUp() {
    this.s = new Scope(new Program("p", Collections.emptyList(), null, null, new ProgramManager("pm")));
    this.s.declareVariable(new Variable("var", false, false, false, true, 1L));
    this.sub = new Scope("sub", this.s);
  }

  @Test
  void getName() {
    assertEquals(Scope.MAIN_SCOPE_NAME, this.s.getName());
  }

  @Test
  void getVariablesEmpty() {
    assertFalse(this.s.getVariables().isEmpty());
  }

  @Test
  void isVariableDefinedTrue() {
    assertTrue(this.s.isVariableDefined("var"));
  }

  @Test
  void isVariableDefinedFalse() {
    assertFalse(this.s.isVariableDefined("a"));
  }

  @Test
  void getVariable() {
    assertEquals(1L, this.s.getVariable("var", false));
  }

  @Test
  void getVariableFromOutside() {
    this.s.declareVariable(new Variable("a", true, false, false, true, 1L));
    assertEquals(1L, this.s.getVariable("a", true));
  }

  @Test
  void getVariableFromOutsideError() {
    assertThrows(EvaluationException.class, () -> this.s.getVariable("var", true));
  }

  @Test
  void setVariable() {
    this.s.setVariable("var", 2L, false);
    assertEquals(2L, this.s.getVariable("var", false));
  }

  @Test
  void setConstantError() {
    this.s.declareVariable(new Variable("a", false, false, true, true, 1L));
    assertThrows(EvaluationException.class, () -> this.s.setVariable("a", 2L, true));
  }

  @Test
  void setVariableFromOutside() {
    this.s.declareVariable(new Variable("a", true, true, false, true, 1L));
    this.s.setVariable("a", 2L, true);
    assertEquals(2L, this.s.getVariable("a", true));
  }

  @Test
  void setVariableFromOutsideError1() {
    this.s.declareVariable(new Variable("a", true, false, false, true, 1L));
    assertThrows(EvaluationException.class, () -> this.s.setVariable("a", 2L, true));
  }

  @Test
  void setVariableFromOutsideError2() {
    assertThrows(EvaluationException.class, () -> this.s.setVariable("var", 2L, true));
  }

  @Test
  void declareVariable() {
    this.s.declareVariable(new Variable("a", false, false, false, true, 1L));
    assertEquals(1L, this.s.getVariable("a", false));
  }

  @Test
  void deleteVariable() {
    this.s.declareVariable(new Variable("a", false, false, false, true, 1L));
    assertEquals(1L, this.s.getVariable("a", false));
    this.s.deleteVariable("a", false);
    assertFalse(this.s.isVariableDefined("a"));
  }

  @Test
  void deleteVariableError() {
    this.s.declareVariable(new Variable("a", false, false, false, false, 1L));
    assertThrows(EvaluationException.class, () -> this.s.deleteVariable("a", false));
  }

  @Test
  void deleteVariableFromOutside() {
    this.s.declareVariable(new Variable("a", true, true, false, true, 1L));
    assertEquals(1L, this.s.getVariable("a", true));
    this.s.deleteVariable("a", true);
    assertFalse(this.s.isVariableDefined("a"));
  }

  @Test
  void deleteVariableFromOutsideError1() {
    this.s.declareVariable(new Variable("a", true, true, false, false, 1L));
    assertThrows(EvaluationException.class, () -> this.s.deleteVariable("a", true));
  }

  @Test
  void deleteVariableFromOutsideError2() {
    this.s.declareVariable(new Variable("a", true, false, false, true, 1L));
    assertThrows(EvaluationException.class, () -> this.s.deleteVariable("a", true));
  }

  @Test
  void reset() {
    this.s.declareVariable(new Variable("a", true, false, false, true, 1L));
    assertTrue(this.s.isVariableDefined("a"));
    this.s.reset();
    assertFalse(this.s.isVariableDefined("a"));
  }

  @Test
  void isVariableDefinedTrueFromSubScope() {
    assertTrue(this.sub.isVariableDefined("var"));
  }

  @Test
  void isVariableDefinedFalseFromSubScope() {
    assertFalse(this.sub.isVariableDefined("a"));
  }

  @Test
  void getVariableFromSubScope() {
    assertEquals(1L, this.sub.getVariable("var", false));
  }

  @Test
  void getVariableFromOutsideFromSubScope() {
    this.s.declareVariable(new Variable("a", true, false, false, true, 1L));
    assertEquals(1L, this.sub.getVariable("a", true));
  }

  @Test
  void getVariableFromOutsideErrorFromSubScope() {
    // Declare variable in parent scope
    this.s.declareVariable(new Variable("a", false, false, false, true, 1L));
    assertThrows(EvaluationException.class, () -> this.sub.getVariable("a", true));
  }

  @Test
  void setVariableFromSubScope() {
    this.sub.setVariable("var", 2L, false);
    assertEquals(2L, this.sub.getVariable("var", false));
  }

  @Test
  void setConstantErrorFromSubScope() {
    // Declare variable in parent scope
    this.s.declareVariable(new Variable("a", false, false, true, true, 1L));
    assertThrows(EvaluationException.class, () -> this.sub.setVariable("a", 2L, true));
  }

  @Test
  void setVariableFromOutsideFromSubScope() {
    // Declare variable in parent scope
    this.s.declareVariable(new Variable("a", true, true, false, true, 1L));
    this.sub.setVariable("a", 2L, true);
    assertEquals(2L, this.sub.getVariable("a", true));
  }

  @Test
  void setVariableFromOutsideError1FromSubScope() {
    // Declare variable in parent scope
    this.s.declareVariable(new Variable("a", true, false, false, true, 1L));
    assertThrows(EvaluationException.class, () -> this.sub.setVariable("a", 2L, true));
  }

  @Test
  void setVariableFromOutsideError2FromSubScope() {
    assertThrows(EvaluationException.class, () -> this.sub.setVariable("var", 2L, true));
  }

  @Test
  void declareVariableFromSubScope() {
    // Declare variable in parent scope
    this.sub.declareVariable(new Variable("a", false, false, false, true, 1L));
    assertEquals(1L, this.sub.getVariable("a", false));
    assertFalse(this.s.isVariableDefined("a"));
  }

  @Test
  void deleteVariableFromSubScope() {
    this.sub.deleteVariable("var", false);
    assertFalse(this.s.isVariableDefined("var"));
    assertFalse(this.sub.isVariableDefined("var"));
  }

  @Test
  void deleteVariableErrorFromSubScope() {
    this.s.declareVariable(new Variable("a", false, false, false, false, 1L));
    assertThrows(EvaluationException.class, () -> this.sub.deleteVariable("a", false));
  }

  @Test
  void deleteVariableFromOutsideFromSubScope() {
    // Declare variable in parent scope
    this.s.declareVariable(new Variable("a", true, true, false, true, 1L));
    assertEquals(1L, this.sub.getVariable("a", true));
    this.sub.deleteVariable("a", true);
    assertFalse(this.sub.isVariableDefined("a"));
  }

  @Test
  void deleteVariableFromOutsideError1FromSubScope() {
    // Declare variable in parent scope
    this.s.declareVariable(new Variable("a", true, true, false, false, 1L));
    assertThrows(EvaluationException.class, () -> this.sub.deleteVariable("a", true));
  }

  @Test
  void deleteVariableFromOutsideError2FromSubScope() {
    // Declare variable in parent scope
    this.s.declareVariable(new Variable("a", true, false, false, true, 1L));
    assertThrows(EvaluationException.class, () -> this.sub.deleteVariable("a", true));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList vars = new NBTTagList();
    vars.appendTag(new Variable("var", false, false, false, true, 1L).writeToNBT());
    tag.setTag(Scope.VARIABLES_KEY, vars);
    assertEquals(tag, this.s.writeToNBT());
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList vars = new NBTTagList();
    vars.appendTag(new Variable("var", false, false, false, true, 1L).writeToNBT());
    tag.setTag(Scope.VARIABLES_KEY, vars);
    Scope s = new Scope(new Program("p", Collections.emptyList(), null, null, new ProgramManager("pm")));
    s.readFromNBT(tag);
    assertEquals(this.s.getVariables(), s.getVariables());
  }
}
