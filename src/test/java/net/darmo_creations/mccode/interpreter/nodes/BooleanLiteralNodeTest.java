package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SetupProgramManager.class)
class BooleanLiteralNodeTest extends NodeTest {
  @Test
  void evaluateTrue() {
    Object r = new BooleanLiteralNode(true).evaluate(this.p.getScope());
    assertSame(Boolean.class, r.getClass());
    assertEquals(true, r);
  }

  @Test
  void evaluateFalse() {
    Object r = new BooleanLiteralNode(false).evaluate(this.p.getScope());
    assertSame(Boolean.class, r.getClass());
    assertEquals(false, r);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BooleanLiteralNode.ID_KEY, BooleanLiteralNode.ID);
    tag.setBoolean(BooleanLiteralNode.VALUE_KEY, true);
    assertEquals(tag, new BooleanLiteralNode(true).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BooleanLiteralNode.ID_KEY, BooleanLiteralNode.ID);
    tag.setBoolean(BooleanLiteralNode.VALUE_KEY, true);
    assertEquals(true, new BooleanLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void testToString() {
    assertEquals("true", new BooleanLiteralNode(true).toString());
    assertEquals("false", new BooleanLiteralNode(false).toString());
  }
}
