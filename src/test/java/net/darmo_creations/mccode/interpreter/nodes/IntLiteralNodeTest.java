package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SetupProgramManager.class)
class IntLiteralNodeTest extends NodeTest {
  @Test
  void evaluate() {
    Object r = new IntLiteralNode(1).evaluate(this.p.getScope());
    assertSame(Integer.class, r.getClass());
    assertEquals(1, r);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(IntLiteralNode.ID_KEY, IntLiteralNode.ID);
    tag.setInteger(IntLiteralNode.VALUE_KEY, 1);
    assertEquals(tag, new IntLiteralNode(1).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(IntLiteralNode.ID_KEY, IntLiteralNode.ID);
    tag.setInteger(IntLiteralNode.VALUE_KEY, 1);
    assertEquals(1, new IntLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void testToString() {
    assertEquals("1", new IntLiteralNode(1).toString());
  }

  @Test
  void testEquals() {
    assertEquals(new IntLiteralNode(1), new IntLiteralNode(1));
  }
}
