package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class VariableTest extends TestBase {
  Variable v;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.v = new Variable("v", true, true, false, true, 1L);
  }

  @Test
  void getName() {
    assertEquals("v", this.v.getName());
  }

  @Test
  void getValue() {
    assertEquals(1L, this.v.getValue(this.p.getScope(), false));
  }

  @Test
  void getValueFromCommand() {
    assertEquals(1L, this.v.getValue(this.p.getScope(), true));
  }

  @Test
  void getValueFromCommandError() {
    assertThrows(EvaluationException.class, () -> new Variable("v", false, false, false, true, 1L)
        .getValue(this.p.getScope(), true));
  }

  @Test
  void setValue() {
    this.v.setValue(this.p.getScope(), 2L, false);
    assertEquals(2L, this.v.getValue(this.p.getScope(), false));
  }

  @Test
  void setValueFromOutside() {
    this.v.setValue(this.p.getScope(), 2L, true);
    assertEquals(2L, this.v.getValue(this.p.getScope(), true));
  }

  @Test
  void setValueNotVisibleFromOutsideError() {
    Variable v = new Variable("v", false, false, false, true, 1L);
    assertThrows(EvaluationException.class, () -> v.setValue(this.p.getScope(), 2L, true));
  }

  @Test
  void setValueNotEditableFromOutsideError() {
    Variable v = new Variable("v", true, false, false, true, 1L);
    assertThrows(EvaluationException.class, () -> v.setValue(this.p.getScope(), 2L, true));
  }

  @Test
  void isConstant() {
    assertTrue(new Variable("v", true, false, true, true, 1L).isConstant());
  }

  @Test
  void isPubliclyVisible() {
    assertTrue(new Variable("v", true, false, true, true, 1L).isPubliclyVisible());
  }

  @Test
  void isEditableFromOutside() {
    assertTrue(new Variable("v", true, true, false, true, 1L).isEditableFromOutside());
  }

  @Test
  void isDeletable() {
    assertTrue(new Variable("v", true, false, true, true, 1L).isDeletable());
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(Variable.NAME_KEY, "v");
    tag.setBoolean(Variable.PUBLIC_KEY, true);
    tag.setBoolean(Variable.EDITABLE_KEY, false);
    tag.setBoolean(Variable.CONSTANT_KEY, true);
    tag.setBoolean(Variable.DELETABLE_KEY, true);
    tag.setString(Variable.TYPE_KEY, "int");
    tag.setTag(Variable.VALUE_KEY, ProgramManager.getTypeForValue(1L).writeToNBT(1L));
    assertEquals(tag, new Variable("v", true, false, true, true, 1L).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(Variable.NAME_KEY, "v");
    tag.setBoolean(Variable.PUBLIC_KEY, true);
    tag.setBoolean(Variable.EDITABLE_KEY, false);
    tag.setBoolean(Variable.CONSTANT_KEY, true);
    tag.setBoolean(Variable.DELETABLE_KEY, true);
    tag.setString(Variable.TYPE_KEY, "int");
    tag.setTag(Variable.VALUE_KEY, ProgramManager.getTypeForValue(1L).writeToNBT(1L));
    assertEquals(new Variable("v", true, false, true, true, 1L),
        new Variable(tag, this.p.getScope()));
  }

  @Test
  void testEquals() {
    assertEquals(new Variable("v", true, true, false, true, 1L), this.v);
  }
}
