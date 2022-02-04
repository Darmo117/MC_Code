package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.NoSuchKeyException;
import net.darmo_creations.mccode.interpreter.exceptions.UnsupportedOperatorException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.darmo_creations.mccode.interpreter.types.Range;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class MapTypeTest extends TypeTest<MapType> {
  @Test
  void getName() {
    assertEquals("map", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(MCMap.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertEquals(Arrays.asList("keys", "values"), this.typeInstance.getPropertiesNames(new MCMap()));
  }

  @Test
  void clearMap() {
    MCMap map = new MCMap();
    map.put("a", 1);
    map.put("b", 2);
    assertNull(this.typeInstance.clear(this.p.getScope(), map));
    assertTrue(map.isEmpty());
  }

  @Test
  void getKeys() {
    MCMap map = new MCMap();
    map.put("a", 1);
    map.put("b", 1);
    map.put("c", 2);
    assertEquals(new MCSet(Arrays.asList("a", "b", "c")), this.typeInstance.getKeys(map));
  }

  @Test
  void getValues() {
    MCMap map = new MCMap();
    map.put("a", 1);
    map.put("b", 1);
    map.put("c", 2);
    // TODO order of list is not guaranteed
    assertEquals(new MCList(Arrays.asList(1, 1, 2)), this.typeInstance.getValues(map));
  }

  @Test
  void getUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), new MCMap(), "a"));
  }

  @Test
  void setUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), new MCMap(), "a", true));
  }

  @Test
  void getUndefinedMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @Test
  void iteration() {
    MCMap map = new MCMap();
    map.put("a", 1);
    map.put("b", 2);
    Iterator<?> it = (Iterator<?>) this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, map, null, null, false);
    while (it.hasNext()) {
      //noinspection SuspiciousMethodCalls
      assertTrue(map.containsKey(it.next()));
    }
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, MCMap self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.NOT, new MCMap(Collections.singletonMap("a", 1)), false),
        Arguments.of(UnaryOperator.NOT, new MCMap(), true),

        Arguments.of(UnaryOperator.LENGTH, new MCMap(), 0),
        Arguments.of(UnaryOperator.LENGTH, new MCMap(Collections.singletonMap("a", 1)), 1)
    );
  }

  @Test
  void applyUnaryOperatorError() {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.MINUS, new MCMap(), null, null, false));
  }

  @Test
  void deleteItem() {
    MCMap map = new MCMap(Collections.singletonMap("a", 1));
    assertNull(this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.DEL_ITEM, map, "a", null, true));
    assertTrue(map.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForSetItem")
  void setItem(MCMap self, String key, Object value, MCMap expected) {
    assertNull(this.typeInstance.applyOperator(this.p.getScope(), TernaryOperator.SET_ITEM, self, key, value, true));
    assertEquals(expected, self);
    assertNotSame(value, self.get(key));
  }

  public static Stream<Arguments> provideArgsForSetItem() {
    return Stream.of(
        Arguments.of(new MCMap(), "a", new MCList(), new MCMap(Collections.singletonMap("a", new MCList()))),
        Arguments.of(new MCMap(Collections.singletonMap("a", new MCList())), "a", new MCSet(), new MCMap(Collections.singletonMap("a", new MCSet())))
    );
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void plusCopy(boolean inPlace) {
    Object o = new MCList();
    MCMap res = (MCMap) this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.PLUS, new MCMap(), new MCMap(Collections.singletonMap("a", o)), null, inPlace);
    assertNotSame(o, res.get("a"));
    MCMap res1 = (MCMap) this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.PLUS, new MCMap(Collections.singletonMap("a", o)), new MCMap(), null, inPlace);
    if (inPlace) {
      assertSame(o, res1.get("a"));
    } else {
      assertNotSame(o, res1.get("a"));
    }
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, MCMap self, Object o, Object expected, boolean inPlace) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, inPlace);
    assertEquals(expected, r);
    if (inPlace) {
      assertSame(self, r);
    } else {
      assertNotSame(self, r);
    }
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    MCMap map = new MCMap();
    map.put("a", 1);
    map.put("b", 2);
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new MCMap(), new MCMap(), false),
        Arguments.of(BinaryOperator.PLUS, new MCMap(Collections.singletonMap("a", 1)), new MCMap(), new MCMap(Collections.singletonMap("a", 1)), false),
        Arguments.of(BinaryOperator.PLUS, new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("a", 2)), new MCMap(Collections.singletonMap("a", 2)), false),
        Arguments.of(BinaryOperator.PLUS, new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("b", 2)), new MCMap(map), false),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new MCMap(), new MCMap(), true),
        Arguments.of(BinaryOperator.PLUS, new MCMap(Collections.singletonMap("a", 1)), new MCMap(), new MCMap(Collections.singletonMap("a", 1)), true),
        Arguments.of(BinaryOperator.PLUS, new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("a", 2)), new MCMap(Collections.singletonMap("a", 2)), true),
        Arguments.of(BinaryOperator.PLUS, new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("b", 2)), new MCMap(map), true),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), "a", "{}a", false),
        Arguments.of(BinaryOperator.PLUS, new MCMap(Collections.singletonMap("a", 1)), "b", "{a=1}b", false),

        Arguments.of(BinaryOperator.SUB, new MCMap(), new MCMap(), new MCMap(), false),
        Arguments.of(BinaryOperator.SUB, new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("a", 1)), new MCMap(), false),
        Arguments.of(BinaryOperator.SUB, new MCMap(map), new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("b", 2)), false),
        Arguments.of(BinaryOperator.SUB, new MCMap(map), new MCMap(Collections.singletonMap("c", 3)), new MCMap(map), false),
        Arguments.of(BinaryOperator.SUB, new MCMap(), new MCMap(), new MCMap(), true),
        Arguments.of(BinaryOperator.SUB, new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("a", 1)), new MCMap(), true),
        Arguments.of(BinaryOperator.SUB, new MCMap(map), new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("b", 2)), true),
        Arguments.of(BinaryOperator.SUB, new MCMap(map), new MCMap(Collections.singletonMap("c", 3)), new MCMap(map), true),

        Arguments.of(BinaryOperator.EQUAL, new MCMap(), "", false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), true, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), 1, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), 1.0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), null, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), new Item(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), new MCList(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), new MCSet(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), new MCMap(), true, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("a", 1)), true, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(Collections.singletonMap("a", 2)), new MCMap(Collections.singletonMap("a", 1)), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(Collections.singletonMap("b", 1)), new MCMap(Collections.singletonMap("a", 1)), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(Collections.singletonMap("b", 2)), new MCMap(Collections.singletonMap("a", 1)), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), new BlockPos(0, 0, 0), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), new Range(1, 1, 1), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCMap(), new ResourceLocation("minecraft:stone"), false, false),

        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), "", true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), true, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), 1, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), 1.0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), null, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), new Item(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), new MCList(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), new MCSet(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), new MCMap(), false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("a", 1)), false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(Collections.singletonMap("a", 2)), new MCMap(Collections.singletonMap("a", 1)), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(Collections.singletonMap("b", 1)), new MCMap(Collections.singletonMap("a", 1)), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(Collections.singletonMap("b", 2)), new MCMap(Collections.singletonMap("a", 1)), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), new BlockPos(0, 0, 0), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), new Range(1, 1, 1), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCMap(), new ResourceLocation("minecraft:stone"), true, false),

        Arguments.of(BinaryOperator.IN, new MCMap(Collections.singletonMap("a", 1)), "a", true, false),
        Arguments.of(BinaryOperator.IN, new MCMap(), "a", false, false),
        Arguments.of(BinaryOperator.IN, new MCMap(Collections.singletonMap("b", "a")), "a", false, false),

        Arguments.of(BinaryOperator.NOT_IN, new MCMap(Collections.singletonMap("a", 1)), "a", false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), "a", true, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(Collections.singletonMap("b", "a")), "a", true, false),

        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(Collections.singletonMap("a", 1)), "a", 1, false)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, MCMap self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new MCMap(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.PLUS, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.PLUS, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new MCList(Collections.singletonList(1)), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new MCSet(Collections.singletonList(1)), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, new MCMap(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.SUB, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.SUB, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.MUL, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.MUL, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.DIV, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.DIV, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.MOD, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.MOD, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.POW, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.POW, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GT, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GT, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GE, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GE, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), "", UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.LT, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.LT, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.LE, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.LE, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new MCMap(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new MCMap(), 1.0, UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.IN, new MCMap(Collections.singletonMap("a", Items.STICK)), Items.STICK, false, false), // FIXME not initialized
//        Arguments.of(BinaryOperator.IN, new MCMap(Collections.singletonMap("a", Blocks.STONE)), Blocks.STONE, false, false), // FIXME not initialized
        Arguments.of(BinaryOperator.IN, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), 1.0, UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.NOT_IN, new MCMap(Collections.singletonMap("a", Items.STICK)), Items.STICK, false, false), // FIXME not initialized
//        Arguments.of(BinaryOperator.NOT_IN, new MCMap(Collections.singletonMap("a", Blocks.STONE)), Blocks.STONE, false, false), // FIXME not initialized
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), "a", NoSuchKeyException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(Collections.singletonMap("b", "a")), "a", NoSuchKeyException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), "a", NoSuchKeyException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, new MCMap(Collections.singletonMap("a", 1)), o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, new MCMap(Collections.singletonMap("a", 1)), o, null, false));
    MCMap map = new MCMap();
    assertSame(map, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, map, o, null, false));
    assertEquals(map, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, map, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    MCMap map = new MCMap(Collections.singletonMap("a", 1));
    assertSame(map, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, map, o, null, false));
    assertEquals(map, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, map, o, null, false));
    MCMap map1 = new MCMap();
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, map1, o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, map1, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyLogicOperator() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(false),
        Arguments.of(1),
        Arguments.of(0),
        Arguments.of(1.0),
        Arguments.of(0.0),
        Arguments.of("a"),
        Arguments.of(""),
        Arguments.of(new Item()),
        Arguments.of(new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap()),
        Arguments.of((Object) null),
        Arguments.of(new ResourceLocation("minecraft:stone")),
        Arguments.of(new BlockPos(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyTernaryOperatorError")
  void applyTernaryOperatorError(TernaryOperator operator, MCMap self, Object o1, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, 1, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), 1, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), null, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCMap(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @Test
  void implicitCast() {
    assertEquals(new MCMap(Collections.singletonMap("a", 1)), this.typeInstance.implicitCast(this.p.getScope(), new MCMap(Collections.singletonMap("a", 1))));
    assertEquals(new MCMap(), this.typeInstance.implicitCast(this.p.getScope(), new MCMap()));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  void explicitCast(MCMap expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCast() {
    MCMap posMap = new MCMap();
    posMap.put("x", 1);
    posMap.put("y", 2);
    posMap.put("z", 3);
    MCMap resourceMap = new MCMap();
    resourceMap.put("namespace", "minecraft");
    resourceMap.put("path", "stone");
    return Stream.of(
        Arguments.of(new MCMap(Collections.singletonMap("a", 1)), new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap(), new MCMap()),
//        Arguments.of(new MCMap(Collections.singletonMap("id", "minecraft:stick")), Items.STICK), // FIXME raises error because of sound system not initialized
//        Arguments.of(new MCMap(Collections.singletonMap("id", "minecraft:stone")), Blocks.STONE), // FIXME raises error because of sound system not initialized
        Arguments.of(posMap, new BlockPos(1, 2, 3)),
        Arguments.of(resourceMap, new ResourceLocation("minecraft:stone"))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForImplicitCastError")
  @Disabled
  void implicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForImplicitCastError() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(false),
        Arguments.of(1),
        Arguments.of(-1),
        Arguments.of(0),
        Arguments.of(1.0),
        Arguments.of(-1.0),
        Arguments.of(0.0),
        Arguments.of("a"),
        Arguments.of(""),
        Arguments.of(new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new Item()),
//        Arguments.of(Blocks.STONE), // FIXME raises error because of sound system not initialized
        Arguments.of((Object) null),
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCastError")
  @Disabled
  void explicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCastError() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(false),
        Arguments.of(1),
        Arguments.of(-1),
        Arguments.of(0),
        Arguments.of(1.0),
        Arguments.of(-1.0),
        Arguments.of(0.0),
        Arguments.of("a"),
        Arguments.of(""),
        Arguments.of((Object) null),
        Arguments.of(new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new Range(1, 1, 1))
    );
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(new MCMap(Collections.singletonMap("a", 1))));
    assertFalse(this.typeInstance.toBoolean(new MCMap()));
  }

  @Test
  void copy() {
    MCMap map = new MCMap();
    map.put("a", 1);
    MCMap copy = this.typeInstance.copy(this.p.getScope(), map);
    assertNotSame(map, copy);
    assertEquals(map, copy);
  }

  @Test
  void copyDeep() {
    MCList innerList = new MCList();
    MCMap list = new MCMap(Collections.singletonMap("a", innerList));
    MCMap copy = this.typeInstance.copy(this.p.getScope(), list);
    assertNotSame(innerList, copy.get("a"));
    assertEquals(innerList, copy.get("a"));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(MapType.NAME_KEY, "map");
    NBTTagCompound entries = new NBTTagCompound();
    entries.setTag("a", ProgramManager.getTypeInstance(IntType.class).writeToNBT(1));
    entries.setTag("b", ProgramManager.getTypeInstance(PosType.class).writeToNBT(new BlockPos(1, 2, 3)));
    tag.setTag(MapType.ENTRIES_KEY, entries);
    MCMap map = new MCMap();
    map.put("a", 1);
    map.put("b", new BlockPos(1, 2, 3));
    assertEquals(tag, this.typeInstance.writeToNBT(map));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(MapType.NAME_KEY, "map");
    NBTTagCompound entries = new NBTTagCompound();
    entries.setTag("a", ProgramManager.getTypeInstance(IntType.class).writeToNBT(1));
    entries.setTag("b", ProgramManager.getTypeInstance(PosType.class).writeToNBT(new BlockPos(1, 2, 3)));
    tag.setTag(MapType.ENTRIES_KEY, entries);
    MCMap map = new MCMap();
    map.put("a", 1);
    map.put("b", new BlockPos(1, 2, 3));
    assertEquals(map, this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<MapType> getTypeClass() {
    return MapType.class;
  }
}
