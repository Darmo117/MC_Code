package net.darmo_creations.mccode.interpreter.type_wrappers;

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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class RangeTypeTest extends TypeTest<RangeType> {
  @Test
  void getName() {
    assertEquals("range", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Range.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertFalse(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames(new Range(1, 1, 1)).isEmpty());
  }

  @Test
  void getUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), new Range(1, 1, 1), "a"));
  }

  @Test
  void setUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), new Range(1, 1, 1), "a", true));
  }

  @Test
  void getUndefinedMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @Test
  void iteration() {
    int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    Iterator<?> it = (Iterator<?>) this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, new Range(1, 10, 1), null, null, false);
    int i = 0;
    while (it.hasNext()) {
      assertEquals(values[i], it.next());
      i++;
    }
  }

  @Test
  void iterationSteps() {
    int[] values = {1, 3, 5, 7, 9};
    Iterator<?> it = (Iterator<?>) this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, new Range(1, 10, 2), null, null, false);
    int i = 0;
    while (it.hasNext()) {
      assertEquals(values[i], it.next());
      i++;
    }
  }

  @Test
  void iterationNegative() {
    int[] values = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    Iterator<?> it = (Iterator<?>) this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, new Range(10, 0, -1), null, null, false);
    int i = 0;
    while (it.hasNext()) {
      assertEquals(values[i], it.next());
      i++;
    }
  }

  @Test
  void iterationNegativeSteps() {
    int[] values = {10, 8, 6, 4, 2};
    Iterator<?> it = (Iterator<?>) this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, new Range(10, 0, -2), null, null, false);
    int i = 0;
    while (it.hasNext()) {
      assertEquals(values[i], it.next());
      i++;
    }
  }

  @Test
  void applyUnaryOperator() {
    assertEquals(false, this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.NOT, new Range(1, 1, 1), null, null, false));
    assertTrue(Iterator.class.isAssignableFrom(this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, new Range(1, 1, 1), null, null, false).getClass()));
    assertNotNull(this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.ITERATE, new Range(1, 1, 1), null, null, false));
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"MINUS", "LENGTH"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, new Range(1, 2, 1), null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, Range self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), "a", "Range{start=1, end=2, step=1}a"),

        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), true, false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), false, false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), 1, false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), 0, false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), 1.0, false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), 0.0, false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), null, false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), "", false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), "1", false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), "0", false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), new Range(1, 2, 1), true),
        Arguments.of(BinaryOperator.EQUAL, new Range(2, 1, 1), new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 1, 2), new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), 1, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), 0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), 1.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), 0.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), "1", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), "0", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), new Range(1, 2, 1), false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(2, 1, 1), new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 1, 2), new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, Range self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Range(1, 2, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    Range range = new Range(1, 2, 1);
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, range, o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, range, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    Range range = new Range(1, 2, 1);
    assertSame(range, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, range, o, null, false));
    assertEquals(range, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, range, o, null, false));
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
  void applyTernaryOperatorError(TernaryOperator operator, Range self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), 1, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), new BlockPos(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Range(1, 1, 1), new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @Test
  void implicitCast() {
    Range range = new Range(1, 2, 1);
    assertSame(range, this.typeInstance.implicitCast(this.p.getScope(), range));
    assertEquals(range, this.typeInstance.implicitCast(this.p.getScope(), range));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCastError")
  void implicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCastError")
  void explicitCastError(Object o) {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForCastError() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(false),
        Arguments.of(1.0),
        Arguments.of(-1.0),
        Arguments.of(0.0),
        Arguments.of((Object) null),
        Arguments.of(new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap()),
        Arguments.of(new Item()),
//        Arguments.of(new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new BlockPos(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone")),
        Arguments.of("a"),
        Arguments.of("")
    );
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(new Range(1, 2, 1)));
  }

  @Test
  void copy() {
    Range range = new Range(1, 2, 1);
    assertNotSame(range, this.typeInstance.copy(this.p.getScope(), range));
    assertEquals(range, this.typeInstance.copy(this.p.getScope(), range));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(RangeType.NAME_KEY, "range");
    tag.setInteger(RangeType.START_KEY, 1);
    tag.setInteger(RangeType.END_KEY, 2);
    tag.setInteger(RangeType.STEP_KEY, 1);
    assertEquals(tag, this.typeInstance.writeToNBT(new Range(1, 2, 1)));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(RangeType.NAME_KEY, "range");
    tag.setInteger(RangeType.START_KEY, 1);
    tag.setInteger(RangeType.END_KEY, 2);
    tag.setInteger(RangeType.STEP_KEY, 1);
    assertEquals(new Range(1, 2, 1), this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<RangeType> getTypeClass() {
    return RangeType.class;
  }
}
