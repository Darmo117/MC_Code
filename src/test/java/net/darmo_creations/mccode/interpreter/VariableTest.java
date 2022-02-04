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
    this.v = new Variable("v", true, true, false, true, 1);
  }

  @Test
  void getName() {
    assertEquals("v", this.v.getName());
  }

  @Test
  void getValue() {
    assertEquals(1, this.v.getValue(this.p.getScope(), false));
  }

  @Test
  void getValueFromCommand() {
    assertEquals(1, this.v.getValue(this.p.getScope(), true));
  }

  @Test
  void getValueFromCommandError() {
    assertThrows(EvaluationException.class, () -> new Variable("v", false, false, false, true, 1)
        .getValue(this.p.getScope(), true));
  }

  @Test
  void setValue() {
    this.v.setValue(this.p.getScope(), 2, false);
    assertEquals(2, this.v.getValue(this.p.getScope(), false));
  }

  @Test
  void setValueFromCommand() {
    this.v.setValue(this.p.getScope(), 2, true);
    assertEquals(2, this.v.getValue(this.p.getScope(), true));
  }

  @Test
  void setValueNotVisibleFromCommandError() {
    Variable v = new Variable("v", false, false, false, true, 1);
    assertThrows(EvaluationException.class, () -> v.setValue(this.p.getScope(), 2, true));
  }

  @Test
  void setValueNotEditableFromCommandError() {
    Variable v = new Variable("v", true, false, false, true, 1);
    assertThrows(EvaluationException.class, () -> v.setValue(this.p.getScope(), 2, true));
  }

  @Test
  void isConstant() {
    assertTrue(new Variable("v", true, false, true, true, 1).isConstant());
  }

  @Test
  void isPubliclyVisible() {
    assertTrue(new Variable("v", true, false, true, true, 1).isPubliclyVisible());
  }

  @Test
  void isEditableThroughCommands() {
    assertTrue(new Variable("v", true, true, false, true, 1).isEditableThroughCommands());
  }

  @Test
  void isDeletable() {
    assertTrue(new Variable("v", true, false, true, true, 1).isDeletable());
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
    tag.setTag(Variable.VALUE_KEY, ProgramManager.getTypeForValue(1).writeToNBT(1));
    assertEquals(tag, new Variable("v", true, false, true, true, 1).writeToNBT());
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
    tag.setTag(Variable.VALUE_KEY, ProgramManager.getTypeForValue(1).writeToNBT(1));
    assertEquals(new Variable("v", true, false, true, true, 1),
        new Variable(tag, this.p.getScope()));
  }

  @Test
  void testEquals() {
    assertEquals(new Variable("v", true, true, false, true, 1), this.v);
  }
}
