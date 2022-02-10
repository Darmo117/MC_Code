package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SetupProgramManager.class)
class NullLiteralNodeTest extends NodeTest {
  @Test
  void evaluate() {
    assertNull(new NullLiteralNode(0, 0).evaluate(this.p.getScope()));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(NullLiteralNode.ID_KEY, NullLiteralNode.ID);
    assertEquals(tag, new NullLiteralNode(0, 0).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(NullLiteralNode.ID_KEY, NullLiteralNode.ID);
    assertNull(new NullLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void testToString() {
    assertEquals("null", new NullLiteralNode(0, 0).toString());
  }

  @Test
  void testEquals() {
    assertEquals(new NullLiteralNode(0, 0), new NullLiteralNode(0, 0));
  }
}
