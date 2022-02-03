package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SetupProgramManager.class)
class FloatLiteralNodeTest extends NodeTest {
  @Test
  void evaluate() {
    Object r = new FloatLiteralNode(1.0).evaluate(this.p.getScope());
    assertSame(Double.class, r.getClass());
    assertEquals(1.0, r);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(FloatLiteralNode.ID_KEY, FloatLiteralNode.ID);
    tag.setDouble(FloatLiteralNode.VALUE_KEY, 1.0);
    assertEquals(tag, new FloatLiteralNode(1.0).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(FloatLiteralNode.ID_KEY, FloatLiteralNode.ID);
    tag.setDouble(FloatLiteralNode.VALUE_KEY, 1.0);
    assertEquals(1.0, new FloatLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void testToString() {
    assertEquals("1.0", new FloatLiteralNode(1.0).toString());
  }

  @Test
  void testEquals() {
    assertEquals(new FloatLiteralNode(1.0), new FloatLiteralNode(1.0));
  }
}
