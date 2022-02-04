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
    assertSame(Integer.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames(0).isEmpty());
  }

  @Test
  void getPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), 1, "a"));
  }

  @Test
  void setPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), 1, "a", true));
  }

  @Test
  void getMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, int self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.MINUS, 1, -1),
        Arguments.of(UnaryOperator.MINUS, 0, 0),
        Arguments.of(UnaryOperator.NOT, 1, false),
        Arguments.of(UnaryOperator.NOT, 0, true)
    );
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"LENGTH", "ITERATE"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, 1, null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, int self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, 1, 1, 2),
        Arguments.of(BinaryOperator.PLUS, 1, 1.0, 2.0),
        Arguments.of(BinaryOperator.PLUS, 1, true, 2),
        Arguments.of(BinaryOperator.PLUS, 1, false, 1),
        Arguments.of(BinaryOperator.PLUS, 1, "a", "1a"),
        Arguments.of(BinaryOperator.PLUS, -1, "a", "-1a"),
        Arguments.of(BinaryOperator.PLUS, 0, 1, 1),
        Arguments.of(BinaryOperator.PLUS, 0, 1.0, 1.0),
        Arguments.of(BinaryOperator.PLUS, 0, true, 1),
        Arguments.of(BinaryOperator.PLUS, 0, false, 0),
        Arguments.of(BinaryOperator.PLUS, 0, "a", "0a"),

        Arguments.of(BinaryOperator.SUB, 1, 1, 0),
        Arguments.of(BinaryOperator.SUB, 1, 1.0, 0.0),
        Arguments.of(BinaryOperator.SUB, 1, true, 0),
        Arguments.of(BinaryOperator.SUB, 1, false, 1),
        Arguments.of(BinaryOperator.SUB, 0, 1, -1),
        Arguments.of(BinaryOperator.SUB, 0, 1.0, -1.0),
        Arguments.of(BinaryOperator.SUB, 0, true, -1),
        Arguments.of(BinaryOperator.SUB, 0, false, 0),

        Arguments.of(BinaryOperator.MUL, 1, 2, 2),
        Arguments.of(BinaryOperator.MUL, 1, 2.0, 2.0),
        Arguments.of(BinaryOperator.MUL, 1, true, 1),
        Arguments.of(BinaryOperator.MUL, 1, false, 0),
        Arguments.of(BinaryOperator.MUL, 0, 1, 0),
        Arguments.of(BinaryOperator.MUL, 0, 1.0, 0.0),
        Arguments.of(BinaryOperator.MUL, 0, true, 0),
        Arguments.of(BinaryOperator.MUL, 0, false, 0),
        Arguments.of(BinaryOperator.MUL, 1, new BlockPos(1, 1, 1), new BlockPos(1, 1, 1)),
        Arguments.of(BinaryOperator.MUL, 1, "a", "a"),
        Arguments.of(BinaryOperator.MUL, 2, "ab", "abab"),
        Arguments.of(BinaryOperator.MUL, 0, "a", ""),
        Arguments.of(BinaryOperator.MUL, 1, new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(1))),
        Arguments.of(BinaryOperator.MUL, 2, new MCList(Arrays.asList(1, 2)), new MCList(Arrays.asList(1, 2, 1, 2))),
        Arguments.of(BinaryOperator.MUL, 0, new MCList(Collections.singletonList(1)), new MCList()),

        Arguments.of(BinaryOperator.DIV, 1, 2, 0.5),
        Arguments.of(BinaryOperator.DIV, 1, 2.0, 0.5),
        Arguments.of(BinaryOperator.DIV, 1, true, 1.0),
        Arguments.of(BinaryOperator.DIV, 0, 1, 0.0),
        Arguments.of(BinaryOperator.DIV, 0, 1.0, 0.0),
        Arguments.of(BinaryOperator.DIV, 0, true, 0.0),

        Arguments.of(BinaryOperator.INT_DIV, 1, 2, 0),
        Arguments.of(BinaryOperator.INT_DIV, 1, 2.0, 0),
        Arguments.of(BinaryOperator.INT_DIV, 1, true, 1),
        Arguments.of(BinaryOperator.INT_DIV, 0, 1, 0),
        Arguments.of(BinaryOperator.INT_DIV, 0, 1.0, 0),
        Arguments.of(BinaryOperator.INT_DIV, 0, true, 0),

        Arguments.of(BinaryOperator.MOD, 1, 2, 1),
        Arguments.of(BinaryOperator.MOD, 1, 2.0, 1.0),
        Arguments.of(BinaryOperator.MOD, 1, true, 0),
        Arguments.of(BinaryOperator.MOD, 0, 1, 0),
        Arguments.of(BinaryOperator.MOD, 0, 1.0, 0.0),
        Arguments.of(BinaryOperator.MOD, 0, true, 0),
        Arguments.of(BinaryOperator.MOD, -1, 2, 1),
        Arguments.of(BinaryOperator.MOD, -1, 2.0, 1.0),

        Arguments.of(BinaryOperator.POW, 1, 2, 1),
        Arguments.of(BinaryOperator.POW, 1, 2.0, 1.0),
        Arguments.of(BinaryOperator.POW, 1, true, 1),
        Arguments.of(BinaryOperator.POW, 0, 1, 0),
        Arguments.of(BinaryOperator.POW, 0, 1.0, 0.0),
        Arguments.of(BinaryOperator.POW, 0, true, 0),
        Arguments.of(BinaryOperator.POW, 1, 0, 1),
        Arguments.of(BinaryOperator.POW, 1, 0.0, 1.0),
        Arguments.of(BinaryOperator.POW, 1, false, 1),
        Arguments.of(BinaryOperator.POW, 0, 0, 1),
        Arguments.of(BinaryOperator.POW, 0, 0.0, 1.0),
        Arguments.of(BinaryOperator.POW, 0, false, 1),

        Arguments.of(BinaryOperator.EQUAL, 1, true, true),
        Arguments.of(BinaryOperator.EQUAL, 1, false, false),
        Arguments.of(BinaryOperator.EQUAL, 0, true, false),
        Arguments.of(BinaryOperator.EQUAL, 0, false, true),
        Arguments.of(BinaryOperator.EQUAL, 1, 1, true),
        Arguments.of(BinaryOperator.EQUAL, 0, 0, true),
        Arguments.of(BinaryOperator.EQUAL, 1, 1.0, true),
        Arguments.of(BinaryOperator.EQUAL, 0, 0.0, true),
        Arguments.of(BinaryOperator.EQUAL, 1, null, false),
        Arguments.of(BinaryOperator.EQUAL, 0, null, false),
        Arguments.of(BinaryOperator.EQUAL, 1, "", false),
        Arguments.of(BinaryOperator.EQUAL, 1, "1", false),
        Arguments.of(BinaryOperator.EQUAL, 0, "", false),
        Arguments.of(BinaryOperator.EQUAL, 0, "0", false),
        Arguments.of(BinaryOperator.EQUAL, 1, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, 0, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, 1, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, 0, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, 1, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, 0, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, 1, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, 0, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, 1, new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, 0, new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, 1, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, 0, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, 1, new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.EQUAL, 0, new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.NOT_EQUAL, 1, true, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, false, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, 1, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, 0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, 1.0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, 0.0, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, "1", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, "0", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 1, new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, 0, new ResourceLocation("minecraft:stone"), true),

        Arguments.of(BinaryOperator.GT, 1, true, false),
        Arguments.of(BinaryOperator.GT, 1, false, true),
        Arguments.of(BinaryOperator.GT, 0, true, false),
        Arguments.of(BinaryOperator.GT, 0, false, false),
        Arguments.of(BinaryOperator.GT, 1, 1, false),
        Arguments.of(BinaryOperator.GT, 1, 0, true),
        Arguments.of(BinaryOperator.GT, 1, 1.0, false),
        Arguments.of(BinaryOperator.GT, 1, 0.0, true),
        Arguments.of(BinaryOperator.GT, 0, 1, false),
        Arguments.of(BinaryOperator.GT, 0, 0, false),
        Arguments.of(BinaryOperator.GT, 0, 1.0, false),
        Arguments.of(BinaryOperator.GT, 0, 0.0, false),

        Arguments.of(BinaryOperator.GE, 1, true, true),
        Arguments.of(BinaryOperator.GE, 1, false, true),
        Arguments.of(BinaryOperator.GE, 0, true, false),
        Arguments.of(BinaryOperator.GE, 0, false, true),
        Arguments.of(BinaryOperator.GE, 1, 1, true),
        Arguments.of(BinaryOperator.GE, 1, 0, true),
        Arguments.of(BinaryOperator.GE, 1, 1.0, true),
        Arguments.of(BinaryOperator.GE, 1, 0.0, true),
        Arguments.of(BinaryOperator.GE, 0, 1, false),
        Arguments.of(BinaryOperator.GE, 0, 0, true),
        Arguments.of(BinaryOperator.GE, 0, 1.0, false),
        Arguments.of(BinaryOperator.GE, 0, 0.0, true),

        Arguments.of(BinaryOperator.LT, 1, true, false),
        Arguments.of(BinaryOperator.LT, 1, false, false),
        Arguments.of(BinaryOperator.LT, 0, true, true),
        Arguments.of(BinaryOperator.LT, 0, false, false),
        Arguments.of(BinaryOperator.LT, 1, 1, false),
        Arguments.of(BinaryOperator.LT, 1, 0, false),
        Arguments.of(BinaryOperator.LT, 1, 1.0, false),
        Arguments.of(BinaryOperator.LT, 1, 0.0, false),
        Arguments.of(BinaryOperator.LT, 0, 1, true),
        Arguments.of(BinaryOperator.LT, 0, 0, false),
        Arguments.of(BinaryOperator.LT, 0, 1.0, true),
        Arguments.of(BinaryOperator.LT, 0, 0.0, false),

        Arguments.of(BinaryOperator.LE, 1, true, true),
        Arguments.of(BinaryOperator.LE, 1, false, false),
        Arguments.of(BinaryOperator.LE, 0, true, true),
        Arguments.of(BinaryOperator.LE, 0, false, true),
        Arguments.of(BinaryOperator.LE, 1, 1, true),
        Arguments.of(BinaryOperator.LE, 1, 0, false),
        Arguments.of(BinaryOperator.LE, 1, 1.0, true),
        Arguments.of(BinaryOperator.LE, 1, 0.0, false),
        Arguments.of(BinaryOperator.LE, 0, 1, true),
        Arguments.of(BinaryOperator.LE, 0, 0, true),
        Arguments.of(BinaryOperator.LE, 0, 1.0, true),
        Arguments.of(BinaryOperator.LE, 0, 0.0, true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, int self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, 1, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1, 0, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, 1, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, 0, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, 1, false, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1, 0, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1, 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, 1, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, 1, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, 1, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, 1, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, 1, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, 1, o, null, false));
    assertEquals(0, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, 0, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, 0, o, null, false));
    assertEquals(1, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, 1, o, null, false));
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
  @MethodSource("provideArgsForImplicitCast")
  void implicitCast(int expected, Object o) {
    assertEquals(expected, this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForImplicitCast() {
    return Stream.of(
        Arguments.of(1, true),
        Arguments.of(0, false),
        Arguments.of(1, 1),
        Arguments.of(-1, -1),
        Arguments.of(0, 0)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  void explicitCast(int expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCast() {
    return Stream.of(
        Arguments.of(1, true),
        Arguments.of(0, false),
        Arguments.of(1, 1),
        Arguments.of(-1, -1),
        Arguments.of(0, 0),
        Arguments.of(1, 1.0),
        Arguments.of(-1, -1.0),
        Arguments.of(0, 0.0),
        Arguments.of(1, "1"),
        Arguments.of(-1, "-1"),
        Arguments.of(0, "0")
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
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new BlockPos(1, 1, 1)),
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
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new BlockPos(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyTernaryOperatorError")
  void applyTernaryOperatorError(TernaryOperator operator, int self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, 1, true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, 1, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, new BlockPos(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, 1, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @Test
  void invalidStringCastError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), "a"));
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), ""));
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(1));
    assertTrue(this.typeInstance.toBoolean(-1));
    assertFalse(this.typeInstance.toBoolean(0));
  }

  @Test
  void copy() {
    assertEquals(0, this.typeInstance.copy(this.p.getScope(), 0));
    assertEquals(1, this.typeInstance.copy(this.p.getScope(), 1));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1})
  void writeToNBT(int i) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(IntType.NAME_KEY, "int");
    tag.setInteger(IntType.VALUE_KEY, i);
    assertEquals(tag, this.typeInstance.writeToNBT(i));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1})
  void readFromNBT(int i) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(IntType.NAME_KEY, "int");
    tag.setInteger(IntType.VALUE_KEY, i);
    assertEquals(i, this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<IntType> getTypeClass() {
    return IntType.class;
  }
}
