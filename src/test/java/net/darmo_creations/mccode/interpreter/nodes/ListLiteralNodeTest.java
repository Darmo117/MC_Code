package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class ListLiteralNodeTest extends NodeTest {
  @Test
  void evaluate() {
    Object r = new ListLiteralNode(Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("test", 0, 0)), 0, 0).evaluate(this.p.getScope());
    assertSame(MCList.class, r.getClass());
    assertEquals(new MCList(Arrays.asList(1L, "test")), r);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ListLiteralNode.ID_KEY, ListLiteralNode.ID);
    NBTTagList list = new NBTTagList();
    list.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    list.appendTag(new StringLiteralNode("test", 0, 0).writeToNBT());
    tag.setTag(ListLiteralNode.VALUES_KEY, list);
    assertEquals(tag, new ListLiteralNode(Arrays.asList(new IntLiteralNode(1, 0, 0), new StringLiteralNode("test", 0, 0)), 0, 0).writeToNBT());
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ListLiteralNode.ID_KEY, ListLiteralNode.ID);
    NBTTagList list = new NBTTagList();
    list.appendTag(new IntLiteralNode(1, 0, 0).writeToNBT());
    list.appendTag(new StringLiteralNode("test", 0, 0).writeToNBT());
    tag.setTag(ListLiteralNode.VALUES_KEY, list);
    assertEquals(new MCList(Arrays.asList(1L, "test")), new ListLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new ListLiteralNode((Collection<Node>) null, 0, 0));
  }

  @Test
  void testToString() {
    assertEquals("[]", new ListLiteralNode(Collections.emptyList(), 0, 0).toString());
    Map<String, Node> map = new HashMap<>();
    map.put("a", new IntLiteralNode(2, 0, 0));
    assertEquals("[1, \"te\\nst\", {\"a\": 2}, {3}]", new ListLiteralNode(Arrays.asList(
        new IntLiteralNode(1, 0, 0),
        new StringLiteralNode("te\nst", 0, 0),
        new MapLiteralNode(map, 0, 0),
        new SetLiteralNode(Collections.singleton(new IntLiteralNode(3, 0, 0)), 0, 0)
    ), 0, 0).toString());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForEquals")
  void testEquals(List<Node> list) {
    assertEquals(new ListLiteralNode(list, 0, 0), new ListLiteralNode(list, 0, 0));
  }

  static Stream<Arguments> provideArgsForEquals() {
    return Stream.of(
        Arguments.of(Collections.emptyList()),
        Arguments.of(Collections.singletonList(new IntLiteralNode(1, 0, 0)))
    );
  }
}
