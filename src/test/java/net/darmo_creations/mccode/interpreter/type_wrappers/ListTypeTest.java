package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.IndexOutOfBoundsException;
import net.darmo_creations.mccode.interpreter.exceptions.UnsupportedOperatorException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.darmo_creations.mccode.interpreter.types.Range;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
class ListTypeTest extends TypeTest<ListType> {
  @Test
  void getName() {
    assertEquals("list", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(MCList.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames().isEmpty());
  }

  @Test
  void clearList() {
    MCList list = new MCList(Arrays.asList(1, 2));
    assertNull(this.typeInstance.clear(this.p.getScope(), list));
    assertTrue(list.isEmpty());
  }

  @Test
  void addItem() {
    MCList list = new MCList();
    Object o = new MCSet();
    assertNull(this.typeInstance.add(this.p.getScope(), list, o));
    assertNotSame(o, list.get(0));
    assertEquals(o, list.get(0));
  }

  @Test
  void insertItemStart() {
    MCList list = new MCList(Arrays.asList(1, 2));
    Object o = new MCSet();
    assertNull(this.typeInstance.insert(this.p.getScope(), list, 0, o));
    assertNotSame(o, list.get(0));
    assertEquals(o, list.get(0));
    assertEquals(1, list.get(1));
    assertEquals(2, list.get(2));
  }

  @Test
  void insertItemMiddle() {
    MCList list = new MCList(Arrays.asList(1, 2));
    Object o = new MCSet();
    assertNull(this.typeInstance.insert(this.p.getScope(), list, 1, o));
    assertEquals(1, list.get(0));
    assertNotSame(o, list.get(1));
    assertEquals(o, list.get(1));
    assertEquals(2, list.get(2));
  }

  @Test
  void insertItemEnd() {
    MCList list = new MCList(Arrays.asList(1, 2));
    Object o = new MCSet();
    assertNull(this.typeInstance.insert(this.p.getScope(), list, 2, o));
    assertEquals(1, list.get(0));
    assertEquals(2, list.get(1));
    assertNotSame(o, list.get(2));
    assertEquals(o, list.get(2));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 1})
  void insertItemError(int index) {
    assertThrows(IndexOutOfBoundsException.class, () -> this.typeInstance.insert(this.p.getScope(), new MCList(), index, ""));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCountOccurences")
  void countOccurences(int nb, Object value) {
    assertEquals(nb, this.typeInstance.count(this.p.getScope(), new MCList(Arrays.asList(1, 2, 1)), value));
  }

  static Stream<Arguments> provideArgsForCountOccurences() {
    return Stream.of(
        Arguments.of(1, 2),
        Arguments.of(2, 1),
        Arguments.of(0, 3)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForItemIndex")
  void itemIndex(int index, Object value) {
    assertEquals(index, this.typeInstance.indexOf(this.p.getScope(), new MCList(Arrays.asList(1, 2, 1)), value));
  }

  static Stream<Arguments> provideArgsForItemIndex() {
    return Stream.of(
        Arguments.of(1, 2),
        Arguments.of(0, 1),
        Arguments.of(-1, 3)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForSortNatural")
  void sortNatural(MCList expected, MCList input) {
    assertNull(this.typeInstance.sort(this.p.getScope(), input, false));
    assertEquals(expected, input);
  }

  static Stream<Arguments> provideArgsForSortNatural() {
    return Stream.of(
        Arguments.of(new MCList(), new MCList()),
        Arguments.of(new MCList(Arrays.asList(1, 2, 3)), new MCList(Arrays.asList(3, 2, 1)))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForSortReversed")
  void sortReversed(MCList expected, MCList input) {
    assertNull(this.typeInstance.sort(this.p.getScope(), input, true));
    assertEquals(expected, input);
  }

  static Stream<Arguments> provideArgsForSortReversed() {
    return Stream.of(
        Arguments.of(new MCList(), new MCList()),
        Arguments.of(new MCList(Arrays.asList(3, 2, 1)), new MCList(Arrays.asList(1, 2, 3)))
    );
  }

  @Test
  void sortNaturalNotComparable() {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.sort(this.p.getScope(), new MCList(Arrays.asList(1, "")), false));
  }

  @Test
  void getUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), new MCList(), "a"));
  }

  @Test
  void setUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), new MCList(), "a", true));
  }

  @Test
  void getUndefinedMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @Test
  void iteration() {
    Object[] o = {1, 2, "a", new BlockPos(1, 2, 3)};
    Iterator<?> it = (Iterator<?>) this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, new MCList(Arrays.asList(o)), null, null, false);
    int i = 0;
    while (it.hasNext()) {
      assertEquals(o[i], it.next());
      i++;
    }
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, MCList self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.NOT, new MCList(Collections.singletonList(1)), false),
        Arguments.of(UnaryOperator.NOT, new MCList(), true),

        Arguments.of(UnaryOperator.LENGTH, new MCList(), 0),
        Arguments.of(UnaryOperator.LENGTH, new MCList(Collections.singletonList(1)), 1),
        Arguments.of(UnaryOperator.LENGTH, new MCList(Arrays.asList(1, 2)), 2)
    );
  }

  @Test
  void applyUnaryOperatorError() {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.MINUS, new MCList(), null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForDeleteItem")
  void deleteItem(MCList self, Object o, MCList expected) {
    assertNull(this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.DEL_ITEM, self, o, null, true));
    // self should have been updated by the operation
    assertEquals(expected, self);
  }

  public static Stream<Arguments> provideArgsForDeleteItem() {
    return Stream.of(
        Arguments.of(new MCList(Arrays.asList(1, 2)), false, new MCList(Collections.singletonList(2))),
        Arguments.of(new MCList(Arrays.asList(1, 2)), true, new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList(Arrays.asList(1, 2)), 0, new MCList(Collections.singletonList(2))),
        Arguments.of(new MCList(Arrays.asList(1, 2)), 1, new MCList(Collections.singletonList(1)))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForSetItem")
  void setItem(MCList self, Object key, Object value, MCList expected) {
    assertNull(this.typeInstance.applyOperator(this.p.getScope(), TernaryOperator.SET_ITEM, self, key, value, true));
    assertEquals(expected, self);
    assertNotSame(value, self.get(key instanceof Boolean ? ((Boolean) key ? 1 : 0) : (Integer) key));
  }

  public static Stream<Arguments> provideArgsForSetItem() {
    return Stream.of(
        Arguments.of(new MCList(Arrays.asList(1, 2)), false, new MCList(), new MCList(Arrays.asList(new MCList(), 2))),
        Arguments.of(new MCList(Arrays.asList(1, 2)), true, new MCList(), new MCList(Arrays.asList(1, new MCList()))),
        Arguments.of(new MCList(Arrays.asList(1, 2)), 0, new MCList(), new MCList(Arrays.asList(new MCList(), 2))),
        Arguments.of(new MCList(Arrays.asList(1, 2)), 1, new MCList(), new MCList(Arrays.asList(1, new MCList())))
    );
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void plusCopy(boolean inPlace) {
    Object o = new MCList();
    MCList res = (MCList) this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.PLUS, new MCList(), new MCList(Collections.singletonList(o)), null, inPlace);
    assertNotSame(o, res.get(0));
    MCList res1 = (MCList) this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.PLUS, new MCList(Collections.singletonList(o)), new MCList(), null, inPlace);
    if (inPlace) {
      assertSame(o, res1.iterator().next());
    } else {
      assertNotSame(o, res1.iterator().next());
    }
  }

  @ParameterizedTest
  @MethodSource("provideArgsForMulCopy")
  void mulCopy(int nb, boolean inPlace) {
    Object o = new MCList();
    MCList res = (MCList) this.typeInstance.applyOperator(
        this.p.getScope(), BinaryOperator.MUL, new MCList(Collections.singletonList(o)), nb, null, inPlace);
    for (int i = 0; i < nb; i++) {
      assertNotSame(o, res.get(i));
    }
  }

  static Stream<Arguments> provideArgsForMulCopy() {
    return Stream.of(
        Arguments.of(1, false),
        Arguments.of(2, false),
        Arguments.of(1, true),
        Arguments.of(2, true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, MCList self, Object o, Object expected, boolean inPlace) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, inPlace);
    assertEquals(expected, r);
    if (inPlace) {
      assertSame(self, r);
    } else {
      assertNotSame(self, r);
    }
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new MCList(), new MCList(), new MCList(), false),
        Arguments.of(BinaryOperator.PLUS, new MCList(Collections.singletonList(1)), new MCList(), new MCList(Collections.singletonList(1)), false),
        Arguments.of(BinaryOperator.PLUS, new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(2)), new MCList(Arrays.asList(1, 2)), false),
        Arguments.of(BinaryOperator.PLUS, new MCList(), new MCList(), new MCList(), true),
        Arguments.of(BinaryOperator.PLUS, new MCList(Collections.singletonList(1)), new MCList(), new MCList(Collections.singletonList(1)), true),
        Arguments.of(BinaryOperator.PLUS, new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(2)), new MCList(Arrays.asList(1, 2)), true),
        Arguments.of(BinaryOperator.PLUS, new MCList(), "a", "[]a", false),
        Arguments.of(BinaryOperator.PLUS, new MCList(Collections.singletonList(1)), "a", "[1]a", false),
        Arguments.of(BinaryOperator.PLUS, new MCList(Arrays.asList(1, 2)), "a", "[1, 2]a", false),

        Arguments.of(BinaryOperator.SUB, new MCList(), new MCList(), new MCList(), false),
        Arguments.of(BinaryOperator.SUB, new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(1)), new MCList(), false),
        Arguments.of(BinaryOperator.SUB, new MCList(Arrays.asList(1, 2)), new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(2)), false),
        Arguments.of(BinaryOperator.SUB, new MCList(Arrays.asList(1, 2)), new MCList(Collections.singletonList(3)), new MCList(Arrays.asList(1, 2)), false),
        Arguments.of(BinaryOperator.SUB, new MCList(Arrays.asList(1, 2, 1)), new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(2)), false),
        Arguments.of(BinaryOperator.SUB, new MCList(), new MCList(), new MCList(), true),
        Arguments.of(BinaryOperator.SUB, new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(1)), new MCList(), true),
        Arguments.of(BinaryOperator.SUB, new MCList(Arrays.asList(1, 2)), new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(2)), true),
        Arguments.of(BinaryOperator.SUB, new MCList(Arrays.asList(1, 2)), new MCList(Collections.singletonList(3)), new MCList(Arrays.asList(1, 2)), true),
        Arguments.of(BinaryOperator.SUB, new MCList(Arrays.asList(1, 2, 1)), new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(2)), true),

        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), 0, new MCList(), false),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), 1, new MCList(Arrays.asList(1, 2)), false),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), 2, new MCList(Arrays.asList(1, 2, 1, 2)), false),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), true, new MCList(Arrays.asList(1, 2)), false),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), false, new MCList(), false),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), 0, new MCList(), true),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), 1, new MCList(Arrays.asList(1, 2)), true),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), 2, new MCList(Arrays.asList(1, 2, 1, 2)), true),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), true, new MCList(Arrays.asList(1, 2)), true),
        Arguments.of(BinaryOperator.MUL, new MCList(Arrays.asList(1, 2)), false, new MCList(), true),

        Arguments.of(BinaryOperator.EQUAL, new MCList(), "", false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), "[1, 2]", false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), true, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), false, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Collections.singletonList(1)), 1, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Collections.singletonList(0)), 0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(), 0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Collections.singletonList(1.0)), 1.0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Collections.singletonList(0.0)), 0.0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(), 0.0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(), null, false, false),
//        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), Items.STICK, false, false), // FIXME not initialized
        Arguments.of(BinaryOperator.EQUAL, new MCList(), new MCList(), true, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), new MCList(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(), new MCList(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(), new MCSet(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), new MCSet(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(), new MCMap(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(0, 0, 0)), new BlockPos(0, 0, 0), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 1, 1)), new Range(1, 1, 1), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), new ResourceLocation("minecraft:stone"), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCList(Arrays.asList(1, 2)), new ResourceLocation("minecraft:stone"), false, false),

        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(), "", true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), "[1, 2]", true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), true, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), false, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Collections.singletonList(1)), 1, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Collections.singletonList(0)), 0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(), 0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Collections.singletonList(1.0)), 1.0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Collections.singletonList(0.0)), 0.0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(), 0.0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(), null, true, false),
//        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), Items.STICK, false, false), // FIXME not initialized
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(), new MCList(), false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), new MCList(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(), new MCList(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(), new MCSet(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), new MCSet(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(), new MCMap(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(0, 0, 0)), new BlockPos(0, 0, 0), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 1, 1)), new Range(1, 1, 1), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), new ResourceLocation("minecraft:stone"), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCList(Arrays.asList(1, 2)), new ResourceLocation("minecraft:stone"), true, false),

        Arguments.of(BinaryOperator.GT, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.GT, new MCList(Arrays.asList(1, 1)), new MCList(Collections.singletonList(1)), true, false),
        Arguments.of(BinaryOperator.GT, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 1)), true, false),
        Arguments.of(BinaryOperator.GT, new MCList(Arrays.asList(2, 1)), new MCList(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.GT, new MCList(Collections.singletonList(1)), new MCList(Arrays.asList(1, 1)), false, false),
        Arguments.of(BinaryOperator.GT, new MCList(Arrays.asList(1, 1)), new MCList(Arrays.asList(1, 2)), false, false),

        Arguments.of(BinaryOperator.GE, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.GE, new MCList(Arrays.asList(1, 1)), new MCList(Collections.singletonList(1)), true, false),
        Arguments.of(BinaryOperator.GE, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 1)), true, false),
        Arguments.of(BinaryOperator.GE, new MCList(Arrays.asList(2, 1)), new MCList(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.GE, new MCList(Collections.singletonList(1)), new MCList(Arrays.asList(1, 1)), false, false),
        Arguments.of(BinaryOperator.GE, new MCList(Arrays.asList(1, 1)), new MCList(Arrays.asList(1, 2)), false, false),

        Arguments.of(BinaryOperator.LT, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.LT, new MCList(Arrays.asList(1, 1)), new MCList(Collections.singletonList(1)), false, false),
        Arguments.of(BinaryOperator.LT, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 1)), false, false),
        Arguments.of(BinaryOperator.LT, new MCList(Arrays.asList(2, 1)), new MCList(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.LT, new MCList(Collections.singletonList(1)), new MCList(Arrays.asList(1, 1)), true, false),
        Arguments.of(BinaryOperator.LT, new MCList(Arrays.asList(1, 1)), new MCList(Arrays.asList(1, 2)), true, false),

        Arguments.of(BinaryOperator.LE, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.LE, new MCList(Arrays.asList(1, 1)), new MCList(Collections.singletonList(1)), false, false),
        Arguments.of(BinaryOperator.LE, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 1)), false, false),
        Arguments.of(BinaryOperator.LE, new MCList(Arrays.asList(2, 1)), new MCList(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.LE, new MCList(Collections.singletonList(1)), new MCList(Arrays.asList(1, 1)), true, false),
        Arguments.of(BinaryOperator.LE, new MCList(Arrays.asList(1, 1)), new MCList(Arrays.asList(1, 2)), true, false),

        Arguments.of(BinaryOperator.IN, new MCList(Arrays.asList(1, 2)), 1, true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Arrays.asList(1, 2)), 2, true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Arrays.asList(1, 2)), 3, false, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(true)), true, true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(1)), 1, true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(1.0)), 1.0, true, false),
//        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(Items.STICK)), Items.STICK, true, false), // FIXME not initialized
//        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(Blocks.STONE)), Blocks.STONE, true, false), // FIXME not initialized
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(null)), null, true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(new MCList())), new MCList(), true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(new MCSet())), new MCSet(), true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(new MCMap())), new MCMap(), true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(new BlockPos(0, 0, 0))), new BlockPos(0, 0, 0), true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(new Range(1, 1, 1))), new Range(1, 1, 1), true, false),
        Arguments.of(BinaryOperator.IN, new MCList(Collections.singletonList(new ResourceLocation("minecraft:stone"))), new ResourceLocation("minecraft:stone"), true, false),

        Arguments.of(BinaryOperator.NOT_IN, new MCList(Arrays.asList(1, 2)), 1, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Arrays.asList(1, 2)), 2, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Arrays.asList(1, 2)), 3, true, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(true)), true, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(1)), 1, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(1.0)), 1.0, false, false),
//        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(Items.STICK)), Items.STICK, false, false), // FIXME not initialized
//        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(Blocks.STONE)), Blocks.STONE, false, false), // FIXME not initialized
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(null)), null, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(new MCList())), new MCList(), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(new MCSet())), new MCSet(), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(new MCMap())), new MCMap(), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(new BlockPos(0, 0, 0))), new BlockPos(0, 0, 0), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(new Range(1, 1, 1))), new Range(1, 1, 1), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCList(Collections.singletonList(new ResourceLocation("minecraft:stone"))), new ResourceLocation("minecraft:stone"), false, false),

        Arguments.of(BinaryOperator.GET_ITEM, new MCList(Arrays.asList(1, 2)), false, 1, false),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(Arrays.asList(1, 2)), true, 2, false),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(Arrays.asList(1, 2)), 0, 1, false),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(Arrays.asList(1, 2)), 1, 2, false)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, MCList self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new MCList(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.PLUS, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.PLUS, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), new MCSet(Collections.singletonList(1)), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, new MCList(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.SUB, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.SUB, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, new MCList(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.MUL, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.MUL, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCList(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.DIV, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.DIV, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.INT_DIV, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.MOD, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.MOD, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.POW, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.POW, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GT, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GT, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GE, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GE, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), "", UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.LT, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.LT, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, new MCList(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.LE, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.LE, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), 0, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(Collections.singletonList(1)), 1, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), -1, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), 0, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(Collections.singletonList(1)), 1, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), -1, IndexOutOfBoundsException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, new MCList(Collections.singletonList(1)), o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, new MCList(Collections.singletonList(1)), o, null, false));
    MCList list = new MCList();
    assertSame(list, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, list, o, null, false));
    assertEquals(list, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, list, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    MCList list = new MCList(Collections.singletonList(1));
    assertSame(list, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, list, o, null, false));
    assertEquals(list, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, list, o, null, false));
    MCList list1 = new MCList();
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, list1, o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, list1, o, null, false));
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
  void applyTernaryOperatorError(TernaryOperator operator, MCList self, Object o1, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, 1, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), 0, IndexOutOfBoundsException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), -1, IndexOutOfBoundsException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), "", UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), null, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCList(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @Test
  void implicitCast() {
    assertEquals(new MCList(Collections.singletonList(1)), this.typeInstance.implicitCast(this.p.getScope(), new MCList(Collections.singletonList(1))));
    assertEquals(new MCList(), this.typeInstance.implicitCast(this.p.getScope(), new MCList()));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  void explicitCast(MCList expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCast() {
    return Stream.of(
        Arguments.of(new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList(), new MCList()),
        Arguments.of(new MCList(Collections.singletonList(1)), new MCSet(Collections.singleton(1))),
        Arguments.of(new MCList(), new MCSet()),
        Arguments.of(new MCList(Arrays.asList("a", "b")), "ab"),
        Arguments.of(new MCList(), "")
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
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap()),
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
        Arguments.of(new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap()),
        Arguments.of(new Item()),
//        Arguments.of(Blocks.STONE), // FIXME raises error because of sound system not initialized
        Arguments.of((Object) null),
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(new MCList(Arrays.asList(1, 2))));
    assertTrue(this.typeInstance.toBoolean(new MCList(Collections.singletonList(0))));
    assertFalse(this.typeInstance.toBoolean(new MCList()));
  }

  @Test
  void copy() {
    MCList list = new MCList(Arrays.asList(1, 2));
    MCList copy = this.typeInstance.copy(this.p.getScope(), list);
    assertNotSame(list, copy);
    assertEquals(list, copy);
  }

  @Test
  void copyDeep() {
    MCList innerList = new MCList();
    MCList list = new MCList(Arrays.asList(1, 2, innerList));
    MCList copy = this.typeInstance.copy(this.p.getScope(), list);
    assertNotSame(innerList, copy.get(2));
    assertEquals(innerList, copy.get(2));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(ListType.NAME_KEY, "list");
    NBTTagList list = new NBTTagList();
    list.appendTag(ProgramManager.getTypeInstance(IntType.class).writeToNBT(1));
    list.appendTag(ProgramManager.getTypeInstance(StringType.class).writeToNBT("ab"));
    list.appendTag(ProgramManager.getTypeInstance(PosType.class).writeToNBT(new BlockPos(1, 2, 3)));
    tag.setTag(ListType.VALUES_KEY, list);
    assertEquals(tag, this.typeInstance.writeToNBT(new MCList(Arrays.asList(1, "ab", new BlockPos(1, 2, 3)))));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(ListType.NAME_KEY, "list");
    NBTTagList list = new NBTTagList();
    list.appendTag(ProgramManager.getTypeInstance(IntType.class).writeToNBT(1));
    list.appendTag(ProgramManager.getTypeInstance(StringType.class).writeToNBT("ab"));
    list.appendTag(ProgramManager.getTypeInstance(PosType.class).writeToNBT(new BlockPos(1, 2, 3)));
    tag.setTag(ListType.VALUES_KEY, list);
    assertEquals(new MCList(Arrays.asList(1, "ab", new BlockPos(1, 2, 3))),
        this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<ListType> getTypeClass() {
    return ListType.class;
  }
}
