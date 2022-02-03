package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class PropertyCallNodeTest extends NodeTest {
  Node instance;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.instance = new FunctionCallNode(
        new VariableNode("to_pos"),
        Collections.singletonList(new ListLiteralNode(Arrays.asList(new IntLiteralNode(1), new IntLiteralNode(2), new IntLiteralNode(3))))
    );
  }

  @Test
  void evaluate() {
    Object r = new PropertyCallNode(this.instance, "x").evaluate(this.p.getScope());
    assertSame(Integer.class, r.getClass());
    assertEquals(1, r);
  }

  @Test
  void evaluateUndefinedError() {
    assertThrows(EvaluationException.class, () -> new PropertyCallNode(new IntLiteralNode(1), "x").evaluate(this.p.getScope()));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(PropertyCallNode.ID_KEY, PropertyCallNode.ID);
    tag.setTag(PropertyCallNode.INSTANCE_KEY, this.instance.writeToNBT());
    tag.setString(PropertyCallNode.PROPERTY_NAME_KEY, "x");
    assertEquals(tag, new PropertyCallNode(this.instance, "x").writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(PropertyCallNode.ID_KEY, PropertyCallNode.ID);
    tag.setTag(PropertyCallNode.INSTANCE_KEY, this.instance.writeToNBT());
    tag.setString(PropertyCallNode.PROPERTY_NAME_KEY, "x");
    assertEquals("to_pos([1, 2, 3]).x", new PropertyCallNode(tag).toString());
  }

  @Test
  void testToString() {
    assertEquals("to_pos([1, 2, 3]).x", new PropertyCallNode(this.instance, "x").toString());
  }

  @Test
  void testEquals() {
    assertEquals(new PropertyCallNode(new VariableNode("floor"), "a"),
        new PropertyCallNode(new VariableNode("floor"), "a"));
  }
}
