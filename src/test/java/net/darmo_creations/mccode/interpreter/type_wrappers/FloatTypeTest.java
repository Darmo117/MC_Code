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
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class FloatTypeTest extends TypeTest<FloatType> {
  @Test
  void getName() {
    assertEquals("float", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Double.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames(0.0).isEmpty());
  }

  @Test
  void getPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), 1.0, "a"));
  }

  @Test
  void setPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), 1.0, "a", true));
  }

  @Test
  void getMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, double self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.MINUS, 1.0, -1.0),
        Arguments.of(UnaryOperator.MINUS, 0.0, 0.0),
        Arguments.of(UnaryOperator.NOT, 1.0, false),
        Arguments.of(UnaryOperator.NOT, 0.0, true)
    );
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"LENGTH", "ITERATE"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, 1.0, null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, double self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, 1.0, 1L, 2.0),
        Arguments.of(BinaryOperator.PLUS, 1.0, 1.0, 2.0),
        Arguments.of(BinaryOperator.PLUS, 1.0, true, 2.0),
        Arguments.of(BinaryOperator.PLUS, 1.0, false, 1.0),
        Arguments.of(BinaryOperator.PLUS, 1.0, "a", "1.0a"),
        Arguments.of(BinaryOperator.PLUS, -1.0, "a", "-1.0a"),
        Arguments.of(BinaryOperator.PLUS, 0.0, 1L, 1.0),
        Arguments.of(BinaryOperator.PLUS, 0.0, 1.0, 1.0),
        Arguments.of(BinaryOperator.PLUS, 0.0, true, 1.0),
        Arguments.of(BinaryOperator.PLUS, 0.0, false, 0.0),
        Arguments.of(BinaryOperator.PLUS, 0.0, "a", "0.0a"),

        Arguments.of(BinaryOperator.SUB, 1.0, 1L, 0.0),
        Arguments.of(BinaryOperator.SUB, 1.0, 1.0, 0.0),
        Arguments.of(BinaryOperator.SUB, 1.0, true, 0.0),
        Arguments.of(BinaryOperator.SUB, 1.0, false, 1.0),
        Arguments.of(BinaryOperator.SUB, 0.0, 1L, -1.0),
        Arguments.of(BinaryOperator.SUB, 0.0, 1.0, -1.0),
        Arguments.of(BinaryOperator.SUB, 0.0, true, -1.0),
        Arguments.of(BinaryOperator.SUB, 0.0, false, 0.0),

        Arguments.of(BinaryOperator.MUL, 1.0, 2L, 2.0),
        Arguments.of(BinaryOperator.MUL, 1.0, 2.0, 2.0),
        Arguments.of(BinaryOperator.MUL, 1.0, true, 1.0),
        Arguments.of(BinaryOperator.MUL, 1.0, false, 0.0),
        Arguments.of(BinaryOperator.MUL, 0.0, 1L, 0.0),
        Arguments.of(BinaryOperator.MUL, 0.0, 1.0, 0.0),
        Arguments.of(BinaryOperator.MUL, 0.0, true, 0.0),
        Arguments.of(BinaryOperator.MUL, 0.0, false, 0.0),
        Arguments.of(BinaryOperator.MUL, 1.0, new BlockPos(1, 1, 1), new BlockPos(1, 1, 1)),
        Arguments.of(BinaryOperator.MUL, 1.5, new BlockPos(1, 2, 3), new BlockPos(1, 3, 4)),

        Arguments.of(BinaryOperator.DIV, 1.0, 2L, 0.5),
        Arguments.of(BinaryOperator.DIV, 1.0, 2.0, 0.5),
        Arguments.of(BinaryOperator.DIV, 1.0, true, 1.0),
        Arguments.of(BinaryOperator.DIV, 0.0, 1L, 0.0),
        Arguments.of(BinaryOperator.DIV, 0.0, 1.0, 0.0),
        Arguments.of(BinaryOperator.DIV, 0.0, true, 0.0),

        Arguments.of(BinaryOperator.INT_DIV, 1.0, 2L, 0L),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, 2.0, 0L),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, true, 1L),
        Arguments.of(BinaryOperator.INT_DIV, 0.0, 1L, 0L),
        Arguments.of(BinaryOperator.INT_DIV, 0.0, 1.0, 0L),
        Arguments.of(BinaryOperator.INT_DIV, 0.0, true, 0L),

        Arguments.of(BinaryOperator.MOD, 1.0, 2L, 1.0),
        Arguments.of(BinaryOperator.MOD, 1.0, 2.0, 1.0),
        Arguments.of(BinaryOperator.MOD, 2.5, 2.0, 0.5),
        Arguments.of(BinaryOperator.MOD, 1.0, true, 0.0),
        Arguments.of(BinaryOperator.MOD, 0.0, 1L, 0.0),
        Arguments.of(BinaryOperator.MOD, 0.0, 1.0, 0.0),
        Arguments.of(BinaryOperator.MOD, 0.0, true, 0.0),
        Arguments.of(BinaryOperator.MOD, -1.0, 2L, 1.0),
        Arguments.of(BinaryOperator.MOD, -1.0, 2.0, 1.0),
        Arguments.of(BinaryOperator.MOD, -2.5, 2.0, 1.5),

        Arguments.of(BinaryOperator.POW, 1.0, 2L, 1.0),
        Arguments.of(BinaryOperator.POW, 1.0, 2.0, 1.0),
        Arguments.of(BinaryOperator.POW, 1.0, true, 1.0),
        Arguments.of(BinaryOperator.POW, 0.0, 1L, 0.0),
        Arguments.of(BinaryOperator.POW, 0.0, 1.0, 0.0),
        Arguments.of(BinaryOperator.POW, 0.0, true, 0.0),
        Arguments.of(BinaryOperator.POW, 1.0, 0L, 1.0),
        Arguments.of(BinaryOperator.POW, 1.0, 0.0, 1.0),
        Arguments.of(BinaryOperator.POW, 1.0, false, 1.0),
        Arguments.of(BinaryOperator.POW, 0.0, 0L, 1.0),
        Arguments.of(BinaryOperator.POW, 0.0, 0.0, 1.0),
        Arguments.of(BinaryOperator.POW, 0.0, false, 1.0),

        Arguments.of(BinaryOperator.EQUAL, 1.0, true, true),
        Arguments.of(BinaryOperator.EQUAL, 1.0, false, false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, true, false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, false, true),
        Arguments.of(BinaryOperator.EQUAL, 1.0, 1L, true),
        Arguments.of(BinaryOperator.EQUAL, 0.0, 0L, true),
        Arguments.of(BinaryOperator.EQUAL, 1.0, 1.0, true),
        Arguments.of(BinaryOperator.EQUAL, 0.0, 0.0, true),
        Arguments.of(BinaryOperator.EQUAL, 1.0, null, false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, null, false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, "", false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, "1.0", false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, "", false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, "0.0", false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, 1.0, new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.EQUAL, 0.0, new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, 1L, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, 0L, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, 1.0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, 0.0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, "1.0", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, "0.0", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1.0, new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0.0, new ResourceLocation("minecraft:stone"), true),

        Arguments.of(BinaryOperator.GT, 1.0, true, false),
        Arguments.of(BinaryOperator.GT, 1.0, false, true),
        Arguments.of(BinaryOperator.GT, 0.0, true, false),
        Arguments.of(BinaryOperator.GT, 0.0, false, false),
        Arguments.of(BinaryOperator.GT, 1.0, 1L, false),
        Arguments.of(BinaryOperator.GT, 1.0, 0L, true),
        Arguments.of(BinaryOperator.GT, 1.0, 1.0, false),
        Arguments.of(BinaryOperator.GT, 1.0, 0.0, true),
        Arguments.of(BinaryOperator.GT, 0.0, 1L, false),
        Arguments.of(BinaryOperator.GT, 0.0, 0L, false),
        Arguments.of(BinaryOperator.GT, 0.0, 1.0, false),
        Arguments.of(BinaryOperator.GT, 0.0, 0.0, false),

        Arguments.of(BinaryOperator.GE, 1.0, true, true),
        Arguments.of(BinaryOperator.GE, 1.0, false, true),
        Arguments.of(BinaryOperator.GE, 0.0, true, false),
        Arguments.of(BinaryOperator.GE, 0.0, false, true),
        Arguments.of(BinaryOperator.GE, 1.0, 1L, true),
        Arguments.of(BinaryOperator.GE, 1.0, 0L, true),
        Arguments.of(BinaryOperator.GE, 1.0, 1.0, true),
        Arguments.of(BinaryOperator.GE, 1.0, 0.0, true),
        Arguments.of(BinaryOperator.GE, 0.0, 1L, false),
        Arguments.of(BinaryOperator.GE, 0.0, 0L, true),
        Arguments.of(BinaryOperator.GE, 0.0, 1.0, false),
        Arguments.of(BinaryOperator.GE, 0.0, 0.0, true),

        Arguments.of(BinaryOperator.LT, 1.0, true, false),
        Arguments.of(BinaryOperator.LT, 1.0, false, false),
        Arguments.of(BinaryOperator.LT, 0.0, true, true),
        Arguments.of(BinaryOperator.LT, 0.0, false, false),
        Arguments.of(BinaryOperator.LT, 1.0, 1L, false),
        Arguments.of(BinaryOperator.LT, 1.0, 0L, false),
        Arguments.of(BinaryOperator.LT, 1.0, 1.0, false),
        Arguments.of(BinaryOperator.LT, 1.0, 0.0, false),
        Arguments.of(BinaryOperator.LT, 0.0, 1L, true),
        Arguments.of(BinaryOperator.LT, 0.0, 0L, false),
        Arguments.of(BinaryOperator.LT, 0.0, 1.0, true),
        Arguments.of(BinaryOperator.LT, 0.0, 0.0, false),

        Arguments.of(BinaryOperator.LE, 1.0, true, true),
        Arguments.of(BinaryOperator.LE, 1.0, false, false),
        Arguments.of(BinaryOperator.LE, 0.0, true, true),
        Arguments.of(BinaryOperator.LE, 0.0, false, true),
        Arguments.of(BinaryOperator.LE, 1.0, 1L, true),
        Arguments.of(BinaryOperator.LE, 1.0, 0L, false),
        Arguments.of(BinaryOperator.LE, 1.0, 1.0, true),
        Arguments.of(BinaryOperator.LE, 1.0, 0.0, false),
        Arguments.of(BinaryOperator.LE, 0.0, 1L, true),
        Arguments.of(BinaryOperator.LE, 0.0, 0L, true),
        Arguments.of(BinaryOperator.LE, 0.0, 1.0, true),
        Arguments.of(BinaryOperator.LE, 0.0, 0.0, true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, double self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, 1.0, "a", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, 1.0, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, 1.0, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, 1.0, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1.0, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, 1.0, o, null, false));
    assertEquals(0.0, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, 0.0, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, 0.0, o, null, false));
    assertEquals(1.0, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, 1.0, o, null, false));
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
  void applyTernaryOperatorError(TernaryOperator operator, Double self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, 1L, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, new BlockPos(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1.0, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForImplicitCast")
  void implicitCast(double expected, Object o) {
    assertEquals(expected, this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForImplicitCast() {
    return Stream.of(
        Arguments.of(1.0, true),
        Arguments.of(0.0, false),
        Arguments.of(1.0, 1L),
        Arguments.of(-1.0, -1L),
        Arguments.of(0.0, 0L),
        Arguments.of(1.0, 1.0),
        Arguments.of(-1.0, -1.0),
        Arguments.of(0.0, 0.0)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  void explicitCast(double expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCast() {
    return Stream.of(
        Arguments.of(1.0, true),
        Arguments.of(0, false),
        Arguments.of(1.0, 1L),
        Arguments.of(-1.0, -1L),
        Arguments.of(0.0, 0L),
        Arguments.of(1.0, 1.0),
        Arguments.of(-1.0, -1.0),
        Arguments.of(0.0, 0.0),
        Arguments.of(1.0, "1.0"),
        Arguments.of(-1.0, "-1.0"),
        Arguments.of(0.0, "0.0")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCastError")
  void implicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCastError")
  void explicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForCastError() {
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
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new BlockPos(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @Test
  void invalidStringCastError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), "a"));
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), ""));
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(1.0));
    assertTrue(this.typeInstance.toBoolean(-1.0));
    assertFalse(this.typeInstance.toBoolean(0.0));
  }

  @Test
  void copy() {
    assertEquals(0.0, this.typeInstance.copy(this.p.getScope(), 0.0));
    assertEquals(1.0, this.typeInstance.copy(this.p.getScope(), 1.0));
  }

  @ParameterizedTest
  @ValueSource(doubles = {0.0, 1.0})
  void writeToNBT(double i) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(FloatType.NAME_KEY, "float");
    tag.setDouble(FloatType.VALUE_KEY, i);
    assertEquals(tag, this.typeInstance.writeToNBT(i));
  }

  @ParameterizedTest
  @ValueSource(doubles = {0.0, 1.0})
  void readFromNBT(double i) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(FloatType.NAME_KEY, "float");
    tag.setDouble(FloatType.VALUE_KEY, i);
    assertEquals(i, this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<FloatType> getTypeClass() {
    return FloatType.class;
  }
}
