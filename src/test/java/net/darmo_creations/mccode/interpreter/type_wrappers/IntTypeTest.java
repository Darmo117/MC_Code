package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.UnsupportedOperatorException;
import net.darmo_creations.mccode.interpreter.types.*;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class IntTypeTest extends TypeTest<IntType> {
  @Test
  void getName() {
    assertEquals("int", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Long.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames(0L).isEmpty());
  }

  @Test
  void getPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getPropertyValue(this.p.getScope(), 1L, "a"));
  }

  @Test
  void setPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), 1L, "a", true));
  }

  @Test
  void getMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod("a"));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, long self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.MINUS, 1L, -1L),
        Arguments.of(UnaryOperator.MINUS, 0L, 0L),
        Arguments.of(UnaryOperator.NOT, 1L, false),
        Arguments.of(UnaryOperator.NOT, 0L, true)
    );
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"LENGTH", "ITERATE"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, 1L, null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, long self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, 1L, 1L, 2L),
        Arguments.of(BinaryOperator.PLUS, 1L, 1.0, 2.0),
        Arguments.of(BinaryOperator.PLUS, 1L, true, 2L),
        Arguments.of(BinaryOperator.PLUS, 1L, false, 1L),
        Arguments.of(BinaryOperator.PLUS, 1L, "a", "1a"),
        Arguments.of(BinaryOperator.PLUS, -1L, "a", "-1a"),
        Arguments.of(BinaryOperator.PLUS, 0L, 1L, 1L),
        Arguments.of(BinaryOperator.PLUS, 0L, 1.0, 1.0),
        Arguments.of(BinaryOperator.PLUS, 0L, true, 1L),
        Arguments.of(BinaryOperator.PLUS, 0L, false, 0L),
        Arguments.of(BinaryOperator.PLUS, 0L, "a", "0a"),

        Arguments.of(BinaryOperator.SUB, 1L, 1L, 0L),
        Arguments.of(BinaryOperator.SUB, 1L, 1.0, 0.0),
        Arguments.of(BinaryOperator.SUB, 1L, true, 0L),
        Arguments.of(BinaryOperator.SUB, 1L, false, 1L),
        Arguments.of(BinaryOperator.SUB, 0L, 1L, -1L),
        Arguments.of(BinaryOperator.SUB, 0L, 1.0, -1.0),
        Arguments.of(BinaryOperator.SUB, 0L, true, -1L),
        Arguments.of(BinaryOperator.SUB, 0L, false, 0L),

        Arguments.of(BinaryOperator.MUL, 1L, 2L, 2L),
        Arguments.of(BinaryOperator.MUL, 1L, 2.0, 2.0),
        Arguments.of(BinaryOperator.MUL, 1L, true, 1L),
        Arguments.of(BinaryOperator.MUL, 1L, false, 0L),
        Arguments.of(BinaryOperator.MUL, 0L, 1L, 0L),
        Arguments.of(BinaryOperator.MUL, 0L, 1.0, 0.0),
        Arguments.of(BinaryOperator.MUL, 0L, true, 0L),
        Arguments.of(BinaryOperator.MUL, 0L, false, 0L),
        Arguments.of(BinaryOperator.MUL, 1L, new Position(1, 1, 1), new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.MUL, 1L, "a", "a"),
        Arguments.of(BinaryOperator.MUL, 2L, "ab", "abab"),
        Arguments.of(BinaryOperator.MUL, 0L, "a", ""),
        Arguments.of(BinaryOperator.MUL, 1L, new MCList(Collections.singletonList(1L)), new MCList(Collections.singletonList(1L))),
        Arguments.of(BinaryOperator.MUL, 2L, new MCList(Arrays.asList(1L, 2L)), new MCList(Arrays.asList(1L, 2L, 1L, 2L))),
        Arguments.of(BinaryOperator.MUL, 0L, new MCList(Collections.singletonList(1L)), new MCList()),

        Arguments.of(BinaryOperator.DIV, 1L, 2L, 0.5),
        Arguments.of(BinaryOperator.DIV, 1L, 2.0, 0.5),
        Arguments.of(BinaryOperator.DIV, 1L, true, 1.0),
        Arguments.of(BinaryOperator.DIV, 0L, 1L, 0.0),
        Arguments.of(BinaryOperator.DIV, 0L, 1.0, 0.0),
        Arguments.of(BinaryOperator.DIV, 0L, true, 0.0),

        Arguments.of(BinaryOperator.INT_DIV, 1L, 2L, 0L),
        Arguments.of(BinaryOperator.INT_DIV, 1L, 2.0, 0L),
        Arguments.of(BinaryOperator.INT_DIV, 1L, true, 1L),
        Arguments.of(BinaryOperator.INT_DIV, 0L, 1L, 0L),
        Arguments.of(BinaryOperator.INT_DIV, 0L, 1.0, 0L),
        Arguments.of(BinaryOperator.INT_DIV, 0L, true, 0L),

        Arguments.of(BinaryOperator.MOD, 1L, 2L, 1L),
        Arguments.of(BinaryOperator.MOD, 1L, 2.0, 1.0),
        Arguments.of(BinaryOperator.MOD, 1L, true, 0L),
        Arguments.of(BinaryOperator.MOD, 0L, 1L, 0L),
        Arguments.of(BinaryOperator.MOD, 0L, 1.0, 0.0),
        Arguments.of(BinaryOperator.MOD, 0L, true, 0L),
        Arguments.of(BinaryOperator.MOD, -1L, 2L, 1L),
        Arguments.of(BinaryOperator.MOD, -1L, 2.0, 1.0),

        Arguments.of(BinaryOperator.POW, 1L, 2L, 1L),
        Arguments.of(BinaryOperator.POW, 1L, 2.0, 1.0),
        Arguments.of(BinaryOperator.POW, 1L, true, 1L),
        Arguments.of(BinaryOperator.POW, 0L, 1L, 0L),
        Arguments.of(BinaryOperator.POW, 0L, 1.0, 0.0),
        Arguments.of(BinaryOperator.POW, 0L, true, 0L),
        Arguments.of(BinaryOperator.POW, 1L, 0L, 1L),
        Arguments.of(BinaryOperator.POW, 1L, 0.0, 1.0),
        Arguments.of(BinaryOperator.POW, 1L, false, 1L),
        Arguments.of(BinaryOperator.POW, 0L, 0L, 1L),
        Arguments.of(BinaryOperator.POW, 0L, 0.0, 1.0),
        Arguments.of(BinaryOperator.POW, 0L, false, 1L),

        Arguments.of(BinaryOperator.EQUAL, 1L, true, true),
        Arguments.of(BinaryOperator.EQUAL, 1L, false, false),
        Arguments.of(BinaryOperator.EQUAL, 0L, true, false),
        Arguments.of(BinaryOperator.EQUAL, 0L, false, true),
        Arguments.of(BinaryOperator.EQUAL, 1L, 1L, true),
        Arguments.of(BinaryOperator.EQUAL, 0L, 0L, true),
        Arguments.of(BinaryOperator.EQUAL, 1L, 1.0, true),
        Arguments.of(BinaryOperator.EQUAL, 0L, 0.0, true),
        Arguments.of(BinaryOperator.EQUAL, 1L, null, false),
        Arguments.of(BinaryOperator.EQUAL, 0L, null, false),
        Arguments.of(BinaryOperator.EQUAL, 1L, "", false),
        Arguments.of(BinaryOperator.EQUAL, 1L, "1", false),
        Arguments.of(BinaryOperator.EQUAL, 0L, "", false),
        Arguments.of(BinaryOperator.EQUAL, 0L, "0", false),
        Arguments.of(BinaryOperator.EQUAL, 1L, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, 0L, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, 1L, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, 0L, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, 1L, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, 0L, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, 1L, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, 0L, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, 1L, new Position(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, 0L, new Position(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, 1L, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, 0L, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, 1L, new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.EQUAL, 0L, new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, 1L, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, 0L, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, 1.0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, 0.0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, "1", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, "0", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, new Position(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, new Position(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1L, new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0L, new ResourceLocation("minecraft:stone"), true),

        Arguments.of(BinaryOperator.GT, 1L, true, false),
        Arguments.of(BinaryOperator.GT, 1L, false, true),
        Arguments.of(BinaryOperator.GT, 0L, true, false),
        Arguments.of(BinaryOperator.GT, 0L, false, false),
        Arguments.of(BinaryOperator.GT, 1L, 1L, false),
        Arguments.of(BinaryOperator.GT, 1L, 0L, true),
        Arguments.of(BinaryOperator.GT, 1L, 1.0, false),
        Arguments.of(BinaryOperator.GT, 1L, 0.0, true),
        Arguments.of(BinaryOperator.GT, 0L, 1, false),
        Arguments.of(BinaryOperator.GT, 0L, 0, false),
        Arguments.of(BinaryOperator.GT, 0L, 1.0, false),
        Arguments.of(BinaryOperator.GT, 0L, 0.0, false),

        Arguments.of(BinaryOperator.GE, 1L, true, true),
        Arguments.of(BinaryOperator.GE, 1L, false, true),
        Arguments.of(BinaryOperator.GE, 0L, true, false),
        Arguments.of(BinaryOperator.GE, 0L, false, true),
        Arguments.of(BinaryOperator.GE, 1L, 1L, true),
        Arguments.of(BinaryOperator.GE, 1L, 0L, true),
        Arguments.of(BinaryOperator.GE, 1L, 1.0, true),
        Arguments.of(BinaryOperator.GE, 1L, 0.0, true),
        Arguments.of(BinaryOperator.GE, 0L, 1L, false),
        Arguments.of(BinaryOperator.GE, 0L, 0L, true),
        Arguments.of(BinaryOperator.GE, 0L, 1.0, false),
        Arguments.of(BinaryOperator.GE, 0L, 0.0, true),

        Arguments.of(BinaryOperator.LT, 1L, true, false),
        Arguments.of(BinaryOperator.LT, 1L, false, false),
        Arguments.of(BinaryOperator.LT, 0L, true, true),
        Arguments.of(BinaryOperator.LT, 0L, false, false),
        Arguments.of(BinaryOperator.LT, 1L, 1L, false),
        Arguments.of(BinaryOperator.LT, 1L, 0L, false),
        Arguments.of(BinaryOperator.LT, 1L, 1.0, false),
        Arguments.of(BinaryOperator.LT, 1L, 0.0, false),
        Arguments.of(BinaryOperator.LT, 0L, 1L, true),
        Arguments.of(BinaryOperator.LT, 0L, 0L, false),
        Arguments.of(BinaryOperator.LT, 0L, 1.0, true),
        Arguments.of(BinaryOperator.LT, 0L, 0.0, false),

        Arguments.of(BinaryOperator.LE, 1L, true, true),
        Arguments.of(BinaryOperator.LE, 1L, false, false),
        Arguments.of(BinaryOperator.LE, 0L, true, true),
        Arguments.of(BinaryOperator.LE, 0L, false, true),
        Arguments.of(BinaryOperator.LE, 1L, 1L, true),
        Arguments.of(BinaryOperator.LE, 1L, 0L, false),
        Arguments.of(BinaryOperator.LE, 1L, 1.0, true),
        Arguments.of(BinaryOperator.LE, 1L, 0.0, false),
        Arguments.of(BinaryOperator.LE, 0L, 1L, true),
        Arguments.of(BinaryOperator.LE, 0L, 0L, true),
        Arguments.of(BinaryOperator.LE, 0L, 1.0, true),
        Arguments.of(BinaryOperator.LE, 0L, 0.0, true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, long self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, 1L, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1L, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1L, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, 1L, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, 1L, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1L, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1L, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, 1L, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, 1, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, 1L, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, 1L, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1L, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, 1L, o, null, false));
    assertEquals(0L, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, 0L, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, 0L, o, null, false));
    assertEquals(1L, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, 1L, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyLogicOperator() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(false),
        Arguments.of(1L),
        Arguments.of(0L),
        Arguments.of(1.0),
        Arguments.of(0.0),
        Arguments.of("a"),
        Arguments.of(""),
        Arguments.of(new Item()),
        Arguments.of(new MCList(Collections.singletonList(1L))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1L))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1L))),
        Arguments.of(new MCMap()),
        Arguments.of((Object) null),
        Arguments.of(new ResourceLocation("minecraft:stone")),
        Arguments.of(new Position(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForImplicitCast")
  void implicitCast(long expected, Object o) {
    assertEquals(expected, this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForImplicitCast() {
    return Stream.of(
        Arguments.of(1L, true),
        Arguments.of(0L, false),
        Arguments.of(1L, 1L),
        Arguments.of(-1L, -1L),
        Arguments.of(0L, 0L)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  void explicitCast(long expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCast() {
    return Stream.of(
        Arguments.of(1L, true),
        Arguments.of(0L, false),
        Arguments.of(1L, 1L),
        Arguments.of(-1L, -1L),
        Arguments.of(0L, 0L),
        Arguments.of(1L, 1.0),
        Arguments.of(-1L, -1.0),
        Arguments.of(0L, 0.0),
        Arguments.of(1L, "1"),
        Arguments.of(-1L, "-1"),
        Arguments.of(0L, "0")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForImplicitCastError")
  void implicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForImplicitCastError() {
    return Stream.of(
        Arguments.of(1.0),
        Arguments.of(-1.0),
        Arguments.of(0.0),
        Arguments.of(new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap()),
        Arguments.of(new Item()),
//        Arguments.of(new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of((Object) null),
        Arguments.of(new Position(0, 0, 0)),
        Arguments.of(new Position(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone")),
        Arguments.of("a"),
        Arguments.of("")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsExplicitForCastError")
  void explicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsExplicitForCastError() {
    return Stream.of(
        Arguments.of(new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap()),
        Arguments.of(new Item()),
//        Arguments.of(new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of((Object) null),
        Arguments.of(new Position(0, 0, 0)),
        Arguments.of(new Position(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyTernaryOperatorError")
  void applyTernaryOperatorError(TernaryOperator operator, long self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, 1L, true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, 1L, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, new Position(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1L, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)
    );
  }

  @Test
  void invalidStringCastError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), "a"));
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), ""));
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(1L));
    assertTrue(this.typeInstance.toBoolean(-1L));
    assertFalse(this.typeInstance.toBoolean(0L));
  }

  @Test
  void copy() {
    assertEquals(0L, this.typeInstance.copy(this.p.getScope(), 0L));
    assertEquals(1L, this.typeInstance.copy(this.p.getScope(), 1L));
  }

  @ParameterizedTest
  @ValueSource(longs = {0, 1})
  void writeToNBT(long i) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(IntType.NAME_KEY, "int");
    tag.setLong(IntType.VALUE_KEY, i);
    assertEquals(tag, this.typeInstance.writeToNBT(i));
  }

  @ParameterizedTest
  @ValueSource(longs = {0, 1})
  void readFromNBT(long i) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(IntType.NAME_KEY, "int");
    tag.setLong(IntType.VALUE_KEY, i);
    assertEquals(i, this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<IntType> getTypeClass() {
    return IntType.class;
  }
}
