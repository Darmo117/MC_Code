package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
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
class BooleanTypeTest extends TypeTest<BooleanType> {
  @Test
  void getName() {
    assertEquals("boolean", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Boolean.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames(true).isEmpty());
  }

  @Test
  void getPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), true, "a"));
  }

  @Test
  void setPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), true, "a", true));
  }

  @Test
  void getMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, boolean self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.MINUS, true, -1L),
        Arguments.of(UnaryOperator.MINUS, false, 0L),
        Arguments.of(UnaryOperator.NOT, true, false),
        Arguments.of(UnaryOperator.NOT, false, true)
    );
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"LENGTH", "ITERATE"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, true, null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, boolean self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, true, 1L, 2L),
        Arguments.of(BinaryOperator.PLUS, true, 1.0, 2.0),
        Arguments.of(BinaryOperator.PLUS, true, true, 2L),
        Arguments.of(BinaryOperator.PLUS, true, false, 1L),
        Arguments.of(BinaryOperator.PLUS, true, "a", "truea"),
        Arguments.of(BinaryOperator.PLUS, false, 1L, 1L),
        Arguments.of(BinaryOperator.PLUS, false, 1.0, 1.0),
        Arguments.of(BinaryOperator.PLUS, false, true, 1L),
        Arguments.of(BinaryOperator.PLUS, false, false, 0L),
        Arguments.of(BinaryOperator.PLUS, false, "a", "falsea"),

        Arguments.of(BinaryOperator.SUB, true, 1L, 0L),
        Arguments.of(BinaryOperator.SUB, true, 1.0, 0.0),
        Arguments.of(BinaryOperator.SUB, true, true, 0L),
        Arguments.of(BinaryOperator.SUB, true, false, 1L),
        Arguments.of(BinaryOperator.SUB, false, 1L, -1L),
        Arguments.of(BinaryOperator.SUB, false, 1.0, -1.0),
        Arguments.of(BinaryOperator.SUB, false, true, -1L),
        Arguments.of(BinaryOperator.SUB, false, false, 0L),

        Arguments.of(BinaryOperator.MUL, true, 2L, 2L),
        Arguments.of(BinaryOperator.MUL, true, 2.0, 2.0),
        Arguments.of(BinaryOperator.MUL, true, true, 1L),
        Arguments.of(BinaryOperator.MUL, true, false, 0L),
        Arguments.of(BinaryOperator.MUL, false, 1L, 0L),
        Arguments.of(BinaryOperator.MUL, false, 1.0, 0.0),
        Arguments.of(BinaryOperator.MUL, false, true, 0L),
        Arguments.of(BinaryOperator.MUL, false, false, 0L),
        Arguments.of(BinaryOperator.MUL, true, new BlockPos(1, 1, 1), new BlockPos(1, 1, 1)),
        Arguments.of(BinaryOperator.MUL, true, "a", "a"),
        Arguments.of(BinaryOperator.MUL, false, "a", ""),
        Arguments.of(BinaryOperator.MUL, true, new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(1))),
        Arguments.of(BinaryOperator.MUL, false, new MCList(Collections.singletonList(1)), new MCList()),

        Arguments.of(BinaryOperator.DIV, true, 2L, 0.5),
        Arguments.of(BinaryOperator.DIV, true, 2.0, 0.5),
        Arguments.of(BinaryOperator.DIV, true, true, 1.0),
        Arguments.of(BinaryOperator.DIV, false, 1L, 0.0),
        Arguments.of(BinaryOperator.DIV, false, 1.0, 0.0),
        Arguments.of(BinaryOperator.DIV, false, true, 0.0),

        Arguments.of(BinaryOperator.INT_DIV, true, 2L, 0L),
        Arguments.of(BinaryOperator.INT_DIV, true, 2.0, 0L),
        Arguments.of(BinaryOperator.INT_DIV, true, true, 1L),
        Arguments.of(BinaryOperator.INT_DIV, false, 1L, 0L),
        Arguments.of(BinaryOperator.INT_DIV, false, 1.0, 0L),
        Arguments.of(BinaryOperator.INT_DIV, false, true, 0L),

        Arguments.of(BinaryOperator.MOD, true, 2L, 1L),
        Arguments.of(BinaryOperator.MOD, true, 2.0, 1.0),
        Arguments.of(BinaryOperator.MOD, true, true, 0L),
        Arguments.of(BinaryOperator.MOD, false, 1L, 0L),
        Arguments.of(BinaryOperator.MOD, false, 1.0, 0.0),
        Arguments.of(BinaryOperator.MOD, false, true, 0L),

        Arguments.of(BinaryOperator.POW, true, 2L, 1L),
        Arguments.of(BinaryOperator.POW, true, 2.0, 1.0),
        Arguments.of(BinaryOperator.POW, true, true, 1L),
        Arguments.of(BinaryOperator.POW, false, 1L, 0L),
        Arguments.of(BinaryOperator.POW, false, 1.0, 0.0),
        Arguments.of(BinaryOperator.POW, false, true, 0L),
        Arguments.of(BinaryOperator.POW, true, 0L, 1L),
        Arguments.of(BinaryOperator.POW, true, 0.0, 1.0),
        Arguments.of(BinaryOperator.POW, true, false, 1L),
        Arguments.of(BinaryOperator.POW, false, 0L, 1L),
        Arguments.of(BinaryOperator.POW, false, 0.0, 1.0),
        Arguments.of(BinaryOperator.POW, false, false, 1L),

        Arguments.of(BinaryOperator.EQUAL, true, true, true),
        Arguments.of(BinaryOperator.EQUAL, true, false, false),
        Arguments.of(BinaryOperator.EQUAL, false, true, false),
        Arguments.of(BinaryOperator.EQUAL, false, false, true),
        Arguments.of(BinaryOperator.EQUAL, true, 1L, true),
        Arguments.of(BinaryOperator.EQUAL, false, 0L, true),
        Arguments.of(BinaryOperator.EQUAL, true, 1.0, true),
        Arguments.of(BinaryOperator.EQUAL, false, 0.0, true),
        Arguments.of(BinaryOperator.EQUAL, true, null, false),
        Arguments.of(BinaryOperator.EQUAL, false, null, false),
        Arguments.of(BinaryOperator.EQUAL, true, "", false),
        Arguments.of(BinaryOperator.EQUAL, true, "1", false),
        Arguments.of(BinaryOperator.EQUAL, false, "", false),
        Arguments.of(BinaryOperator.EQUAL, false, "0", false),
        Arguments.of(BinaryOperator.EQUAL, true, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, false, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, true, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, false, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, true, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, false, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, true, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, false, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, true, new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, false, new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, true, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, false, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, true, new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.EQUAL, false, new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.NOT_EQUAL, true, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, 1L, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, 0L, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, 1.0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, 0.0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, "1", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, "0", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, true, new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, false, new ResourceLocation("minecraft:stone"), true),

        Arguments.of(BinaryOperator.GT, true, true, false),
        Arguments.of(BinaryOperator.GT, true, false, true),
        Arguments.of(BinaryOperator.GT, false, true, false),
        Arguments.of(BinaryOperator.GT, false, false, false),
        Arguments.of(BinaryOperator.GT, true, 1L, false),
        Arguments.of(BinaryOperator.GT, true, 0L, true),
        Arguments.of(BinaryOperator.GT, true, 1.0, false),
        Arguments.of(BinaryOperator.GT, true, 0.0, true),
        Arguments.of(BinaryOperator.GT, false, 1L, false),
        Arguments.of(BinaryOperator.GT, false, 0L, false),
        Arguments.of(BinaryOperator.GT, false, 1.0, false),
        Arguments.of(BinaryOperator.GT, false, 0.0, false),

        Arguments.of(BinaryOperator.GE, true, true, true),
        Arguments.of(BinaryOperator.GE, true, false, true),
        Arguments.of(BinaryOperator.GE, false, true, false),
        Arguments.of(BinaryOperator.GE, false, false, true),
        Arguments.of(BinaryOperator.GE, true, 1L, true),
        Arguments.of(BinaryOperator.GE, true, 0L, true),
        Arguments.of(BinaryOperator.GE, true, 1.0, true),
        Arguments.of(BinaryOperator.GE, true, 0.0, true),
        Arguments.of(BinaryOperator.GE, false, 1L, false),
        Arguments.of(BinaryOperator.GE, false, 0L, true),
        Arguments.of(BinaryOperator.GE, false, 1.0, false),
        Arguments.of(BinaryOperator.GE, false, 0.0, true),

        Arguments.of(BinaryOperator.LT, true, true, false),
        Arguments.of(BinaryOperator.LT, true, false, false),
        Arguments.of(BinaryOperator.LT, false, true, true),
        Arguments.of(BinaryOperator.LT, false, false, false),
        Arguments.of(BinaryOperator.LT, true, 1L, false),
        Arguments.of(BinaryOperator.LT, true, 0L, false),
        Arguments.of(BinaryOperator.LT, true, 1.0, false),
        Arguments.of(BinaryOperator.LT, true, 0.0, false),
        Arguments.of(BinaryOperator.LT, false, 1L, true),
        Arguments.of(BinaryOperator.LT, false, 0L, false),
        Arguments.of(BinaryOperator.LT, false, 1.0, true),
        Arguments.of(BinaryOperator.LT, false, 0.0, false),

        Arguments.of(BinaryOperator.LE, true, true, true),
        Arguments.of(BinaryOperator.LE, true, false, false),
        Arguments.of(BinaryOperator.LE, false, true, true),
        Arguments.of(BinaryOperator.LE, false, false, true),
        Arguments.of(BinaryOperator.LE, true, 1L, true),
        Arguments.of(BinaryOperator.LE, true, 0L, false),
        Arguments.of(BinaryOperator.LE, true, 1.0, true),
        Arguments.of(BinaryOperator.LE, true, 0.0, false),
        Arguments.of(BinaryOperator.LE, false, 1L, true),
        Arguments.of(BinaryOperator.LE, false, 0L, true),
        Arguments.of(BinaryOperator.LE, false, 1.0, true),
        Arguments.of(BinaryOperator.LE, false, 0.0, true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, boolean self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, true, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, true, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, true, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, true, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, true, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, true, 0L, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, true, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, true, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, true, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, true, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, true, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, 1L, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, true, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, true, o, null, false));
    assertEquals(false, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, false, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, false, o, null, false));
    assertEquals(true, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, true, o, null, false));
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
  void applyTernaryOperatorError(TernaryOperator operator, boolean self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, true, true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, 1L, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, new BlockPos(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, true, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCast")
  void implicitCast(boolean expected, Object o) {
    assertEquals(expected, this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForCast")
  void explicitCast(boolean expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForCast() {
    return Stream.of(
        Arguments.of(true, true),
        Arguments.of(false, false),
        Arguments.of(true, 1L),
        Arguments.of(true, -1L),
        Arguments.of(false, 0L),
        Arguments.of(true, 1.0),
        Arguments.of(true, -1.0),
        Arguments.of(false, 0.0),
        Arguments.of(true, new MCList(Collections.singletonList(1))),
        Arguments.of(false, new MCList()),
        Arguments.of(true, new MCSet(Collections.singletonList(1))),
        Arguments.of(false, new MCSet()),
        Arguments.of(true, new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(false, new MCMap()),
        Arguments.of(true, new Item()),
//        Arguments.of(true, new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of(false, null),
        Arguments.of(true, new BlockPos(0, 0, 0)),
        Arguments.of(true, new BlockPos(1, 1, 1)),
        Arguments.of(true, new Range(1, 1, 1)),
        Arguments.of(true, new ResourceLocation("minecraft:stone")),
        Arguments.of(true, "a"),
        Arguments.of(false, "")
    );
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(true));
    assertFalse(this.typeInstance.toBoolean(false));
  }

  @Test
  void copy() {
    assertEquals(true, this.typeInstance.copy(this.p.getScope(), true));
    assertEquals(false, this.typeInstance.copy(this.p.getScope(), false));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void writeToNBT(boolean b) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(BooleanType.NAME_KEY, "boolean");
    tag.setBoolean(BooleanType.VALUE_KEY, b);
    assertEquals(tag, this.typeInstance.writeToNBT(b));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void readFromNBT(boolean b) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(BooleanType.NAME_KEY, "boolean");
    tag.setBoolean(BooleanType.VALUE_KEY, b);
    assertEquals(b, this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<BooleanType> getTypeClass() {
    return BooleanType.class;
  }
}
