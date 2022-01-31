package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
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
class SetTypeTest extends TypeTest<SetType> {
  @Test
  void getName() {
    assertEquals("set", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(MCSet.class, this.typeInstance.getWrappedType());
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
  void clearSet() {
    MCSet set = new MCSet(Arrays.asList(1, 2));
    assertNull(this.typeInstance.clear(this.p.getScope(), set));
    assertTrue(set.isEmpty());
  }

  @Test
  void addItem() {
    MCSet set = new MCSet();
    Object o = new MCSet();
    assertNull(this.typeInstance.add(this.p.getScope(), set, o));
    assertEquals(1, set.size());
    Object item = set.iterator().next();
    assertEquals(o, item);
    assertNotSame(o, item);
  }

  @ParameterizedTest
  @MethodSource("provideArgsForUnion")
  void union(MCSet self, MCSet other, MCSet expected) {
    MCSet res = this.typeInstance.union(this.p.getScope(), self, other);
    assertEquals(expected, res);
    assertNotSame(self, res);
  }

  static Stream<Arguments> provideArgsForUnion() {
    return Stream.of(
        Arguments.of(new MCSet(), new MCSet(), new MCSet()),
        Arguments.of(new MCSet(Arrays.asList(2, 3)), new MCSet(), new MCSet(Arrays.asList(2, 3))),
        Arguments.of(new MCSet(), new MCSet(Arrays.asList(2, 3)), new MCSet(Arrays.asList(2, 3))),
        Arguments.of(new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(2, 3)), new MCSet(Arrays.asList(1, 2, 3))),
        Arguments.of(new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(3, 4)), new MCSet(Arrays.asList(1, 2, 3, 4)))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForIntersection")
  void intersection(MCSet self, MCSet other, MCSet expected) {
    MCSet res = this.typeInstance.intersection(this.p.getScope(), self, other);
    assertEquals(expected, res);
    assertNotSame(self, res);
  }

  static Stream<Arguments> provideArgsForIntersection() {
    return Stream.of(
        Arguments.of(new MCSet(), new MCSet(), new MCSet()),
        Arguments.of(new MCSet(Arrays.asList(2, 3)), new MCSet(), new MCSet()),
        Arguments.of(new MCSet(), new MCSet(Arrays.asList(2, 3)), new MCSet()),
        Arguments.of(new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(2, 3)), new MCSet(Collections.singletonList(2))),
        Arguments.of(new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(3, 4)), new MCSet())
    );
  }

  @Test
  void getUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), new MCSet(), "a"));
  }

  @Test
  void setUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), new MCSet(), "a", true));
  }

  @Test
  void getUndefinedMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @Test
  void iteration() {
    MCSet set = new MCSet(Arrays.asList(1, 2, "a", new BlockPos(1, 2, 3)));
    Iterator<?> it = (Iterator<?>) this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, set, null, null, false);
    while (it.hasNext()) {
      assertTrue(set.contains(it.next()));
    }
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, MCSet self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.NOT, new MCSet(Collections.singletonList(1)), false),
        Arguments.of(UnaryOperator.NOT, new MCSet(), true),

        Arguments.of(UnaryOperator.LENGTH, new MCSet(), 0),
        Arguments.of(UnaryOperator.LENGTH, new MCSet(Collections.singletonList(1)), 1),
        Arguments.of(UnaryOperator.LENGTH, new MCSet(Arrays.asList(1, 2)), 2)
    );
  }

  @Test
  void applyUnaryOperatorError() {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.MINUS, new MCSet(), null, null, false));
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void plusCopy(boolean inPlace) {
    Object o = new MCSet();
    MCSet res = (MCSet) this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.PLUS, new MCSet(), new MCSet(Collections.singletonList(o)), null, inPlace);
    assertNotSame(o, res.iterator().next());
    MCSet res1 = (MCSet) this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.PLUS, new MCSet(Collections.singletonList(o)), new MCSet(), null, inPlace);
    if (inPlace) {
      assertSame(o, res1.iterator().next());
    } else {
      assertNotSame(o, res1.iterator().next());
    }
  }

  @Test
  void unionCopy() {
    Object o = new MCSet();
    MCSet res = this.typeInstance.union(this.p.getScope(), new MCSet(), new MCSet(Collections.singletonList(o)));
    assertNotSame(o, res.iterator().next());
    MCSet res1 = this.typeInstance.union(this.p.getScope(), new MCSet(Collections.singletonList(o)), new MCSet());
    assertNotSame(o, res1.iterator().next());
  }

  @Test
  void intersectionCopy() {
    Object o = new MCSet();
    MCSet res = this.typeInstance.intersection(this.p.getScope(), new MCSet(Arrays.asList(1, 2, o)), new MCSet(Collections.singletonList(o)));
    assertNotSame(o, res.iterator().next());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, MCSet self, Object o, Object expected, boolean inPlace) {
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
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new MCSet(), new MCSet(), false),
        Arguments.of(BinaryOperator.PLUS, new MCSet(Collections.singletonList(1)), new MCSet(), new MCSet(Collections.singletonList(1)), false),
        Arguments.of(BinaryOperator.PLUS, new MCSet(Collections.singletonList(1)), new MCSet(Collections.singletonList(2)), new MCSet(Arrays.asList(1, 2)), false),
        Arguments.of(BinaryOperator.PLUS, new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(2, 3)), new MCSet(Arrays.asList(1, 2, 3)), false),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new MCSet(), new MCSet(), true),
        Arguments.of(BinaryOperator.PLUS, new MCSet(Collections.singletonList(1)), new MCSet(), new MCSet(Collections.singletonList(1)), true),
        Arguments.of(BinaryOperator.PLUS, new MCSet(Collections.singletonList(1)), new MCSet(Collections.singletonList(2)), new MCSet(Arrays.asList(1, 2)), true),
        Arguments.of(BinaryOperator.PLUS, new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(2, 3)), new MCSet(Arrays.asList(1, 2, 3)), true),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), "a", "{}a", false),
        Arguments.of(BinaryOperator.PLUS, new MCSet(Collections.singletonList(1)), "a", "{1}a", false),

        Arguments.of(BinaryOperator.SUB, new MCSet(), new MCSet(), new MCSet(), false),
        Arguments.of(BinaryOperator.SUB, new MCSet(Collections.singletonList(1)), new MCSet(), new MCSet(Collections.singletonList(1)), false),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new MCSet(Collections.singletonList(1)), new MCSet(), false),
        Arguments.of(BinaryOperator.SUB, new MCSet(Collections.singletonList(1)), new MCSet(Collections.singletonList(1)), new MCSet(), false),
        Arguments.of(BinaryOperator.SUB, new MCSet(Arrays.asList(1, 2)), new MCSet(Collections.singletonList(1)), new MCSet(Collections.singletonList(2)), false),
        Arguments.of(BinaryOperator.SUB, new MCSet(Arrays.asList(1, 2)), new MCSet(Collections.singletonList(3)), new MCSet(Arrays.asList(1, 2)), false),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new MCSet(), new MCSet(), true),
        Arguments.of(BinaryOperator.SUB, new MCSet(Collections.singletonList(1)), new MCSet(), new MCSet(Collections.singletonList(1)), true),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new MCSet(Collections.singletonList(1)), new MCSet(), true),
        Arguments.of(BinaryOperator.SUB, new MCSet(Collections.singletonList(1)), new MCSet(Collections.singletonList(1)), new MCSet(), true),
        Arguments.of(BinaryOperator.SUB, new MCSet(Arrays.asList(1, 2)), new MCSet(Collections.singletonList(1)), new MCSet(Collections.singletonList(2)), true),
        Arguments.of(BinaryOperator.SUB, new MCSet(Arrays.asList(1, 2)), new MCSet(Collections.singletonList(3)), new MCSet(Arrays.asList(1, 2)), true),

        Arguments.of(BinaryOperator.EQUAL, new MCSet(), "", false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), "{1, 2}", false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), true, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), false, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Collections.singletonList(1)), 1, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Collections.singletonList(0)), 0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(), 0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Collections.singletonList(1.0)), 1.0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Collections.singletonList(0.0)), 0.0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(), 0.0, false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(), null, false, false),
//        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), Items.STICK, false, false), // FIXME not initialized
        Arguments.of(BinaryOperator.EQUAL, new MCSet(), new MCSet(), true, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), new MCSet(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(), new MCSet(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(), new MCList(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(), new MCMap(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Collections.singletonList(0)), new BlockPos(0, 0, 0), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Collections.singletonList(1)), new Range(1, 1, 1), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), new ResourceLocation("minecraft:stone"), false, false),
        Arguments.of(BinaryOperator.EQUAL, new MCSet(Arrays.asList(1, 2)), new ResourceLocation("minecraft:stone"), false, false),

        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(), "", true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), "{1, 2}", true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), true, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), false, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Collections.singletonList(1)), 1, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Collections.singletonList(0)), 0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(), 0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Collections.singletonList(1.0)), 1.0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Collections.singletonList(0.0)), 0.0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(), 0.0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(), null, true, false),
//        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), Items.STICK, true, false), // FIXME not initialized
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(), new MCSet(), false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), new MCSet(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(), new MCSet(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(), new MCList(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(), new MCMap(), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Collections.singletonList(0)), new BlockPos(0, 0, 0), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Collections.singletonList(1)), new Range(1, 1, 1), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), new ResourceLocation("minecraft:stone"), true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new MCSet(Arrays.asList(1, 2)), new ResourceLocation("minecraft:stone"), true, false),

        Arguments.of(BinaryOperator.GT, new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.GT, new MCSet(Arrays.asList(1, 2)), new MCSet(Collections.singletonList(1)), true, false),
        Arguments.of(BinaryOperator.GT, new MCSet(Collections.singletonList(1)), new MCSet(Arrays.asList(1, 2)), false, false),

        Arguments.of(BinaryOperator.GE, new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.GE, new MCSet(Arrays.asList(1, 2)), new MCSet(Collections.singletonList(1)), true, false),
        Arguments.of(BinaryOperator.GE, new MCSet(Collections.singletonList(1)), new MCSet(Arrays.asList(1, 2)), false, false),

        Arguments.of(BinaryOperator.LT, new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(1, 2)), false, false),
        Arguments.of(BinaryOperator.LT, new MCSet(Arrays.asList(1, 2)), new MCSet(Collections.singletonList(1)), false, false),
        Arguments.of(BinaryOperator.LT, new MCSet(Collections.singletonList(1)), new MCSet(Arrays.asList(1, 2)), true, false),

        Arguments.of(BinaryOperator.LE, new MCSet(Arrays.asList(1, 2)), new MCSet(Arrays.asList(1, 2)), true, false),
        Arguments.of(BinaryOperator.LE, new MCSet(Arrays.asList(1, 2)), new MCSet(Collections.singletonList(1)), false, false),
        Arguments.of(BinaryOperator.LE, new MCSet(Collections.singletonList(1)), new MCSet(Arrays.asList(1, 2)), true, false),

        Arguments.of(BinaryOperator.IN, new MCSet(Arrays.asList(1, 2)), 1, true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Arrays.asList(1, 2)), 2, true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Arrays.asList(1, 2)), 3, false, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(true)), true, true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(1)), 1, true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(1.0)), 1.0, true, false),
//        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(Items.STICK)), Items.STICK, true, false), // FIXME not initialized
//        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(Blocks.STONE)), Blocks.STONE, true, false), // FIXME not initialized
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(null)), null, true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(new MCList())), new MCList(), true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(new MCSet())), new MCSet(), true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(new MCMap())), new MCMap(), true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(new BlockPos(0, 0, 0))), new BlockPos(0, 0, 0), true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(new Range(1, 1, 1))), new Range(1, 1, 1), true, false),
        Arguments.of(BinaryOperator.IN, new MCSet(Collections.singletonList(new ResourceLocation("minecraft:stone"))), new ResourceLocation("minecraft:stone"), true, false),

        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Arrays.asList(1, 2)), 1, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Arrays.asList(1, 2)), 2, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Arrays.asList(1, 2)), 3, true, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(true)), true, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(1)), 1, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(1.0)), 1.0, false, false),
//        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(Items.STICK)), Items.STICK, false, false), // FIXME not initialized
//        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(Blocks.STONE)), Blocks.STONE, false, false), // FIXME not initialized
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(null)), null, false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(new MCList())), new MCList(), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(new MCSet())), new MCSet(), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(new MCMap())), new MCMap(), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(new BlockPos(0, 0, 0))), new BlockPos(0, 0, 0), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(new Range(1, 1, 1))), new Range(1, 1, 1), false, false),
        Arguments.of(BinaryOperator.NOT_IN, new MCSet(Collections.singletonList(new ResourceLocation("minecraft:stone"))), new ResourceLocation("minecraft:stone"), false, false)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, MCSet self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new MCSet(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.PLUS, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.PLUS, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new MCList(Collections.singletonList(1)), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, new MCSet(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.SUB, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.SUB, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new MCSet(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCSet(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.MUL, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.MUL, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCSet(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.DIV, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.DIV, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.MOD, new MCList(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.MOD, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.POW, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.POW, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GT, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GT, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GE, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GE, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), "", UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.LT, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.LT, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, new MCSet(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.LE, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.LE, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), new Item(), UnsupportedOperatorException.class),
//        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), new Block(Material.AIR), UnsupportedOperatorException.class), // FIXME not initialized
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new MCSet(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, new MCSet(Collections.singletonList(1)), o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, new MCSet(Collections.singletonList(1)), o, null, false));
    MCSet set = new MCSet();
    assertSame(set, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, set, o, null, false));
    assertEquals(set, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, set, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    MCSet set = new MCSet(Collections.singletonList(1));
    assertSame(set, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, set, o, null, false));
    assertEquals(set, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, set, o, null, false));
    MCSet set1 = new MCSet();
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, set1, o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, set1, o, null, false));
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
  void applyTernaryOperatorError(TernaryOperator operator, MCSet self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), 0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), new BlockPos(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new MCSet(), new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)
    );
  }

  @Test
  void implicitCast() {
    assertEquals(new MCSet(Collections.singletonList(1)), this.typeInstance.implicitCast(this.p.getScope(), new MCSet(Collections.singletonList(1))));
    assertEquals(new MCSet(), this.typeInstance.implicitCast(this.p.getScope(), new MCSet()));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  void explicitCast(MCSet expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCast() {
    return Stream.of(
        Arguments.of(new MCSet(Collections.singletonList(1)), new MCList(Collections.singletonList(1))),
        Arguments.of(new MCSet(), new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1)), new MCSet(Collections.singleton(1))),
        Arguments.of(new MCSet(), new MCSet()),
        Arguments.of(new MCSet(Arrays.asList("a", "b")), "ab"),
        Arguments.of(new MCSet(Arrays.asList("a", "b")), "abb"),
        Arguments.of(new MCSet(), "")
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
    assertTrue(this.typeInstance.toBoolean(new MCSet(Arrays.asList(1, 2))));
    assertTrue(this.typeInstance.toBoolean(new MCSet(Collections.singletonList(0))));
    assertFalse(this.typeInstance.toBoolean(new MCSet()));
  }

  @Test
  void copy() {
    MCSet set = new MCSet(Arrays.asList(1, 2));
    MCSet copy = this.typeInstance.copy(this.p.getScope(), set);
    assertNotSame(set, copy);
    assertEquals(set, copy);
  }

  @Test
  void copyDeep() {
    MCSet innerSet = new MCSet();
    MCSet set = new MCSet(Arrays.asList(1, 2, innerSet));
    MCSet copy = this.typeInstance.copy(this.p.getScope(), set);
    Object value = copy.iterator().next();
    assertNotSame(innerSet, value);
    assertEquals(innerSet, value);
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(SetType.NAME_KEY, "set");
    NBTTagList list = new NBTTagList();
    list.appendTag(ProgramManager.getTypeInstance(IntType.class).writeToNBT(1));
    tag.setTag(SetType.VALUES_KEY, list);
    assertEquals(tag, this.typeInstance.writeToNBT(new MCSet(Collections.singletonList(1))));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(SetType.NAME_KEY, "set");
    NBTTagList list = new NBTTagList();
    list.appendTag(ProgramManager.getTypeInstance(IntType.class).writeToNBT(1));
    tag.setTag(SetType.VALUES_KEY, list);
    assertEquals(new MCSet(Collections.singletonList(1)), this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<SetType> getTypeClass() {
    return SetType.class;
  }
}
