package net.darmo_creations.mccode.interpreter.nodes;

import jdk.nashorn.internal.ir.annotations.Ignore;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.minecraft.nbt.NBTBase;
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
class SetLiteralNodeTest extends NodeTest {
  @Test
  void evaluate() {
    Object r = new SetLiteralNode(Arrays.asList(new IntLiteralNode(1), new StringLiteralNode("test"))).evaluate(this.p.getScope());
    assertSame(MCSet.class, r.getClass());
    assertEquals(new MCSet(Arrays.asList(1L, "test")), r);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound actualTag = new SetLiteralNode(Arrays.asList(new IntLiteralNode(1), new StringLiteralNode("test"))).writeToNBT();
    assertEquals(SetLiteralNode.ID, actualTag.getInteger(SetLiteralNode.ID_KEY));
    NBTTagList list = actualTag.getTagList(SetLiteralNode.VALUES_KEY, new NBTTagCompound().getId());
    Set<NBTBase> actualTagSet = new HashSet<>();
    list.forEach(actualTagSet::add);
    Set<NBTBase> expectedTagSet = new HashSet<>(Arrays.asList(new IntLiteralNode(1).writeToNBT(), new StringLiteralNode("test").writeToNBT()));
    assertEquals(expectedTagSet, actualTagSet);
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(SetLiteralNode.ID_KEY, SetLiteralNode.ID);
    NBTTagList list = new NBTTagList();
    list.appendTag(new IntLiteralNode(1).writeToNBT());
    list.appendTag(new StringLiteralNode("test").writeToNBT());
    tag.setTag(SetLiteralNode.VALUES_KEY, list);
    assertEquals(new MCSet(Arrays.asList(1L, "test")), new SetLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new SetLiteralNode((Collection<Node>) null));
  }

  @Test
  @Ignore
  void testToString() {
    assertEquals("{}", new SetLiteralNode(Collections.emptyList()).toString());
    assertEquals("{\"a\\n\"}", new SetLiteralNode(Collections.singleton(new StringLiteralNode("a\n"))).toString());
    assertEquals("{[\"a\\n\"]}", new SetLiteralNode(Collections.singleton(new ListLiteralNode(Collections.singleton(new StringLiteralNode("a\n"))))).toString());
    assertEquals("{{\"a\\n\"}}", new SetLiteralNode(Collections.singleton(new SetLiteralNode(Collections.singleton(new StringLiteralNode("a\n"))))).toString());
    assertEquals("{{\"a\\n\": \"b\\n\"}}", new SetLiteralNode(Collections.singleton(new MapLiteralNode(Collections.singletonMap("a\n", new StringLiteralNode("b\n"))))).toString());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForEquals")
  void testEquals(List<Node> list) {
    assertEquals(new SetLiteralNode(list), new SetLiteralNode(list));
  }

  static Stream<Arguments> provideArgsForEquals() {
    return Stream.of(
        Arguments.of(Collections.emptyList()),
        Arguments.of(Collections.singletonList(new IntLiteralNode(1)))
    );
  }
}
