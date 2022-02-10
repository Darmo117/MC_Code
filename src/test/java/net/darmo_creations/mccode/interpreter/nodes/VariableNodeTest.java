package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class VariableNodeTest extends NodeTest {
  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.p.getScope().declareVariable(new Variable("variable", false, false, true, true, 1));
  }

  @AfterEach
  void tearDown() {
    this.p.getScope().deleteVariable("variable", false);
  }

  @Test
  void evaluate() {
    Object r = new VariableNode("variable", 0, 0).evaluate(this.p.getScope());
    assertSame(Integer.class, r.getClass());
    assertEquals(1, r);
  }

  @Test
  void evaluateUndefinedError() {
    assertThrows(EvaluationException.class, () -> new VariableNode("auieauie", 0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(VariableNode.ID_KEY, VariableNode.ID);
    tag.setString(VariableNode.NAME_KEY, "variable");
    assertEquals(tag, new VariableNode("variable", 0, 0).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(VariableNode.ID_KEY, VariableNode.ID);
    tag.setString(VariableNode.NAME_KEY, "variable");
    assertEquals(1, new VariableNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new VariableNode((String) null, 0, 0));
  }

  @Test
  void testToString() {
    assertEquals("variable", new VariableNode("variable", 0, 0).toString());
  }

  @Test
  void testEquals() {
    assertEquals(new VariableNode("a", 0, 0), new VariableNode("a", 0, 0));
  }
}
