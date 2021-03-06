package net.darmo_creations.mccode.interpreter.nodes;

import jdk.nashorn.internal.ir.annotations.Ignore;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class MapLiteralNodeTest extends NodeTest {
  @Test
  void evaluate() {
    MCMap expectedMap = new MCMap();
    expectedMap.put("a", 1L);
    expectedMap.put("b", "string");
    Map<String, Node> map = new HashMap<>();
    map.put("a", new IntLiteralNode(1, 0, 0));
    map.put("b", new StringLiteralNode("string", 0, 0));
    Object r = new MapLiteralNode(map, 0, 0).evaluate(this.p.getScope());
    assertSame(MCMap.class, r.getClass());
    assertEquals(expectedMap, r);
  }

  @Test
  void writeToNBT() {
    Map<String, Node> map = new HashMap<>();
    map.put("a", new IntLiteralNode(1, 0, 0));
    map.put("b", new StringLiteralNode("string", 0, 0));
    NBTTagCompound actualTag = new MapLiteralNode(map, 0, 0).writeToNBT();
    assertEquals(MapLiteralNode.ID, actualTag.getInteger(MapLiteralNode.ID_KEY));
    Map<String, NBTTagCompound> actualTagMap = new HashMap<>();
    NBTTagCompound tag = actualTag.getCompoundTag(MapLiteralNode.VALUES_KEY);
    for (String k : tag.getKeySet()) {
      actualTagMap.put(k, tag.getCompoundTag(k));
    }
    Map<String, NBTTagCompound> expectedTagMap = new HashMap<>();
    expectedTagMap.put("a", new IntLiteralNode(1, 0, 0).writeToNBT());
    expectedTagMap.put("b", new StringLiteralNode("string", 0, 0).writeToNBT());
    assertEquals(expectedTagMap, actualTagMap);
  }

  @Test
  void deserializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(MapLiteralNode.ID_KEY, MapLiteralNode.ID);
    NBTTagCompound values = new NBTTagCompound();
    values.setTag("a", new IntLiteralNode(1, 0, 0).writeToNBT());
    values.setTag("b", new StringLiteralNode("string", 0, 0).writeToNBT());
    tag.setTag(MapLiteralNode.VALUES_KEY, values);
    MCMap expectedMap = new MCMap();
    expectedMap.put("a", 1L);
    expectedMap.put("b", "string");
    assertEquals(expectedMap, new MapLiteralNode(tag).evaluate(this.p.getScope()));
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new MapLiteralNode((Map<String, Node>) null, 0, 0));
  }

  @Test
  @Ignore
  void testToString() {
    assertEquals("{}", new MapLiteralNode(Collections.emptyMap(), 0, 0).toString());
    assertEquals("{\"a\\n\": \"b\\n\"}", new MapLiteralNode(Collections.singletonMap("a\n", new StringLiteralNode("b\n", 0, 0)), 0, 0).toString());
    assertEquals("{\"a\\n\": [\"b\\n\"]}", new MapLiteralNode(Collections.singletonMap("a\n", new ListLiteralNode(Collections.singleton(new StringLiteralNode("b\n", 0, 0)), 0, 0)), 0, 0).toString());
    assertEquals("{\"a\\n\": {\"b\\n\"}}", new MapLiteralNode(Collections.singletonMap("a\n", new SetLiteralNode(Collections.singleton(new StringLiteralNode("b\n", 0, 0)), 0, 0)), 0, 0).toString());
    assertEquals("{\"a\\n\": {\"c\\n\": \"b\\n\"}}", new MapLiteralNode(Collections.singletonMap("a\n", (new MapLiteralNode(Collections.singletonMap("c\n", new StringLiteralNode("b\n", 0, 0)), 0, 0))), 0, 0).toString());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForEquals")
  void testEquals(Map<String, Node> list) {
    assertEquals(new MapLiteralNode(list, 0, 0), new MapLiteralNode(list, 0, 0));
  }

  static Stream<Arguments> provideArgsForEquals() {
    return Stream.of(
        Arguments.of(Collections.emptyMap()),
        Arguments.of(Collections.singletonMap("a", new IntLiteralNode(1, 0, 0)))
    );
  }
}
