package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class ListLiteralNodeTest extends NodeTest {
  @Test
  void evaluate() {
    Object r = new ListLiteralNode(Arrays.asList(new IntLiteralNode(1), new StringLiteralNode("test"))).evaluate(this.p.getScope());
    assertSame(MCList.class, r.getClass());
    assertEquals(new MCList(Arrays.asList(1, "test")), r);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ListLiteralNode.ID_KEY, ListLiteralNode.ID);
    NBTTagList list = new NBTTagList();
    list.appendTag(new IntLiteralNode(1).writeToNBT());
    list.appendTag(new StringLiteralNode("test").writeToNBT());
    tag.setTag(ListLiteralNode.VALUES_KEY, list);
    assertEquals(tag, new ListLiteralNode(Arrays.asList(new IntLiteralNode(1), new StringLiteralNode("test"))).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ListLiteralNode.ID_KEY, ListLiteralNode.ID);
    NBTTagList list = new NBTTagList();
    list.appendTag(new IntLiteralNode(1).writeToNBT());
    list.appendTag(new StringLiteralNode("test").writeToNBT());
    tag.setTag(ListLiteralNode.VALUES_KEY, list);
    assertEquals(new MCList(Arrays.asList(1, "test")), new ListLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new ListLiteralNode((Collection<Node>) null));
  }

  @Test
  void testToString() {
    assertEquals("[]", new ListLiteralNode(Collections.emptyList()).toString());
    Map<String, Node> map = new HashMap<>();
    map.put("a", new IntLiteralNode(2));
    assertEquals("[1, \"te\\nst\", {\"a\": 2}, {3}]", new ListLiteralNode(Arrays.asList(
        new IntLiteralNode(1),
        new StringLiteralNode("te\nst"),
        new MapLiteralNode(map),
        new SetLiteralNode(Collections.singleton(new IntLiteralNode(3)))
    )).toString());
  }
}
