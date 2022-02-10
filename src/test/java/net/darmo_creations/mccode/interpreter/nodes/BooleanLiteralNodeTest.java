package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SetupProgramManager.class)
class BooleanLiteralNodeTest extends NodeTest {
  @Test
  void evaluateTrue() {
    Object r = new BooleanLiteralNode(true, 0, 0).evaluate(this.p.getScope());
    assertSame(Boolean.class, r.getClass());
    assertEquals(true, r);
  }

  @Test
  void evaluateFalse() {
    Object r = new BooleanLiteralNode(false, 0, 0).evaluate(this.p.getScope());
    assertSame(Boolean.class, r.getClass());
    assertEquals(false, r);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BooleanLiteralNode.ID_KEY, BooleanLiteralNode.ID);
    tag.setBoolean(BooleanLiteralNode.VALUE_KEY, true);
    assertEquals(tag, new BooleanLiteralNode(true, 0, 0).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BooleanLiteralNode.ID_KEY, BooleanLiteralNode.ID);
    tag.setBoolean(BooleanLiteralNode.VALUE_KEY, true);
    assertEquals(true, new BooleanLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void testToString(boolean b) {
    assertEquals("" + b, new BooleanLiteralNode(b, 0, 0).toString());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void testEquals(boolean b) {
    assertEquals(new BooleanLiteralNode(b, 0, 0), new BooleanLiteralNode(b, 0, 0));
  }
}
