package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class StringLiteralNodeTest extends NodeTest {
  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new StringLiteralNode((String) null));
  }

  @Test
  void evaluate() {
    Object r = new StringLiteralNode("string").evaluate(this.p.getScope());
    assertSame(String.class, r.getClass());
    assertEquals("string", r);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(StringLiteralNode.ID_KEY, StringLiteralNode.ID);
    tag.setString(StringLiteralNode.VALUE_KEY, "string");
    assertEquals(tag, new StringLiteralNode("string").writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(StringLiteralNode.ID_KEY, StringLiteralNode.ID);
    tag.setString(StringLiteralNode.VALUE_KEY, "string");
    assertEquals("string", new StringLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void invalidStringEscapeError() {
    assertThrows(MCCodeException.class, () -> new StringLiteralNode("\r"));
    assertThrows(MCCodeException.class, () -> new StringLiteralNode("\t"));
    assertThrows(MCCodeException.class, () -> new StringLiteralNode("\b"));
    assertThrows(MCCodeException.class, () -> new StringLiteralNode("\f"));
  }

  @Test
  void testToString() {
    assertEquals("\"test\"", new StringLiteralNode("test").toString());
  }

  @Test
  void testToStringLineReturn() {
    assertEquals("\"te\\nst\"", new StringLiteralNode("te\nst").toString());
  }

  @Test
  void testToStringBackslash() {
    assertEquals("\"te\\\\st\"", new StringLiteralNode("te\\st").toString());
  }

  @Test
  void testToStringQuote() {
    assertEquals("\"te\\\"st\"", new StringLiteralNode("te\"st").toString());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "a"})
  void testEquals(String s) {
    assertEquals(new StringLiteralNode(s), new StringLiteralNode(s));
  }
}
