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

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class PosTypeTest extends TypeTest<PosType> {
  @Test
  void getName() {
    assertEquals("pos", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Position.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertEquals(Arrays.asList("x", "y", "z"), this.typeInstance.getPropertiesNames(new Position(0, 0, 0)));
  }

  @Test
  void getPropertyX() {
    assertEquals(1, this.typeInstance.getPropertyValue(this.p.getScope(), new Position(1, 2, 3), "x"));
  }

  @Test
  void getPropertyY() {
    assertEquals(2, this.typeInstance.getPropertyValue(this.p.getScope(), new Position(1, 2, 3), "y"));
  }

  @Test
  void getPropertyZ() {
    assertEquals(3, this.typeInstance.getPropertyValue(this.p.getScope(), new Position(1, 2, 3), "z"));
  }

  @Test
  void getUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getPropertyValue(this.p.getScope(), new Position(1, 1, 1), "a"));
  }

  @Test
  void setPropertyXError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), new Position(0, 0, 0), "x", 1));
  }

  @Test
  void setPropertyYError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), new Position(0, 0, 0), "y", 1));
  }

  @Test
  void setPropertyZError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), new Position(0, 0, 0), "z", 1));
  }

  @Test
  void setUndefinedPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), new Position(0, 0, 0), "a", 1));
  }

  @Test
  void getUndefinedMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod("a"));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyUnaryOperator")
  void applyUnaryOperator(UnaryOperator operator, Position self, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, null, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyUnaryOperator() {
    return Stream.of(
        Arguments.of(UnaryOperator.MINUS, new Position(1, 2, 3), new Position(-1, -2, -3)),
        Arguments.of(UnaryOperator.NOT, new Position(0, 0, 0), false),
        Arguments.of(UnaryOperator.NOT, new Position(1, 1, 1), false)
    );
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"LENGTH", "ITERATE"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, new Position(1, 1, 1), null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, Position self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), new Position(1, 2, 3), new Position(2, 3, 4)),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), "a", "(1.0, 1.0, 1.0)a"),

        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), new Position(1, 2, 3), new Position(0, -1, -2)),

        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), true, new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), false, new Position(0, 0, 0)),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), 2, new Position(2, 2, 2)),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), 2.0, new Position(2, 2, 2)),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), 2.5, new Position(2, 2, 2)),

        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), true, new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.DIV, new Position(4, 4, 4), 2, new Position(2, 2, 2)),
        Arguments.of(BinaryOperator.DIV, new Position(4, 4, 4), 2.0, new Position(2, 2, 2)),
        Arguments.of(BinaryOperator.DIV, new Position(4, 4, 4), 2.5, new Position(1, 1, 1)),

        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), true, new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.INT_DIV, new Position(4, 4, 4), 2, new Position(2, 2, 2)),
        Arguments.of(BinaryOperator.INT_DIV, new Position(4, 4, 4), 2.0, new Position(2, 2, 2)),
        Arguments.of(BinaryOperator.INT_DIV, new Position(4, 4, 4), 2.5, new Position(1, 1, 1)),

        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), true, new Position(0, 0, 0)),
        Arguments.of(BinaryOperator.MOD, new Position(3, 3, 3), 2, new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.MOD, new Position(-3, -3, -3), 2, new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.MOD, new Position(3, 3, 3), 2.0, new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.MOD, new Position(-3, -3, -3), 2.0, new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.MOD, new Position(4, 4, 4), 2.5, new Position(1, 1, 1)),

        Arguments.of(BinaryOperator.POW, new Position(2, 2, 2), true, new Position(2, 2, 2)),
        Arguments.of(BinaryOperator.POW, new Position(2, 2, 2), false, new Position(1, 1, 1)),
        Arguments.of(BinaryOperator.POW, new Position(3, 3, 3), 2, new Position(9, 9, 9)),
        Arguments.of(BinaryOperator.POW, new Position(-3, -3, -3), 2, new Position(9, 9, 9)),
        Arguments.of(BinaryOperator.POW, new Position(3, 3, 3), 2.0, new Position(9, 9, 9)),
        Arguments.of(BinaryOperator.POW, new Position(-3, -3, -3), 2.0, new Position(9, 9, 9)),
        Arguments.of(BinaryOperator.POW, new Position(4, 4, 4), 2.5, new Position(32, 32, 32)),

        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), true, false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), false, false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), 1, false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), 1.0, false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), null, false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), "", false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), "(1.0, 1.0, 1.0)", false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), new Position(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), new Position(1, 1, 1), true),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), new Position(1, 1, 1), false),
        Arguments.of(BinaryOperator.GT, new Position(1, 2, 1), new Position(1, 1, 1), true),
        Arguments.of(BinaryOperator.GT, new Position(1, 2, 2), new Position(1, 2, 1), true),
        Arguments.of(BinaryOperator.GT, new Position(2, 2, 2), new Position(1, 2, 2), true),
        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), new Position(1, 2, 1), false),
        Arguments.of(BinaryOperator.GT, new Position(1, 2, 1), new Position(1, 2, 2), false),
        Arguments.of(BinaryOperator.GT, new Position(1, 2, 2), new Position(2, 2, 2), false),

        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), new Position(1, 1, 1), true),
        Arguments.of(BinaryOperator.GE, new Position(1, 2, 1), new Position(1, 1, 1), true),
        Arguments.of(BinaryOperator.GE, new Position(1, 2, 2), new Position(1, 2, 1), true),
        Arguments.of(BinaryOperator.GE, new Position(2, 2, 2), new Position(1, 2, 2), true),
        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), new Position(1, 2, 1), false),
        Arguments.of(BinaryOperator.GE, new Position(1, 2, 1), new Position(1, 2, 2), false),
        Arguments.of(BinaryOperator.GE, new Position(1, 2, 2), new Position(2, 2, 2), false),

        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), new Position(1, 1, 1), false),
        Arguments.of(BinaryOperator.LT, new Position(1, 2, 1), new Position(1, 1, 1), false),
        Arguments.of(BinaryOperator.LT, new Position(1, 2, 2), new Position(1, 2, 1), false),
        Arguments.of(BinaryOperator.LT, new Position(2, 2, 2), new Position(1, 2, 2), false),
        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), new Position(1, 2, 1), true),
        Arguments.of(BinaryOperator.LT, new Position(1, 2, 1), new Position(1, 2, 2), true),
        Arguments.of(BinaryOperator.LT, new Position(1, 2, 2), new Position(2, 2, 2), true),

        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), new Position(1, 1, 1), true),
        Arguments.of(BinaryOperator.LE, new Position(1, 2, 1), new Position(1, 1, 1), false),
        Arguments.of(BinaryOperator.LE, new Position(1, 2, 2), new Position(1, 2, 1), false),
        Arguments.of(BinaryOperator.LE, new Position(2, 2, 2), new Position(1, 2, 2), false),
        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), new Position(1, 2, 1), true),
        Arguments.of(BinaryOperator.LE, new Position(1, 2, 1), new Position(1, 2, 2), true),
        Arguments.of(BinaryOperator.LE, new Position(1, 2, 2), new Position(2, 2, 2), true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, Position self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), new Position(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), "a", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), false, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), 0, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), false, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), 0, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), false, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), 0, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), 0.0, ArithmeticException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Position(1, 1, 1), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    Position pos = new Position(1, 1, 1);
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, pos, o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, pos, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    Position pos = new Position(1, 1, 1);
    assertSame(pos, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, pos, o, null, false));
    assertEquals(pos, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, pos, o, null, false));
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
        Arguments.of(new Position(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyTernaryOperatorError")
  void applyTernaryOperatorError(TernaryOperator operator, Position self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), 1, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), new Position(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Position(0, 0, 0), new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @Test
  void implicitCast() {
    Position o = new Position(1, 1, 1);
    assertSame(o, this.typeInstance.implicitCast(this.p.getScope(), o));
    assertEquals(o, this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  void explicitCast(Position expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCast() {
    MCMap map = new MCMap();
    map.put("x", 1L);
    map.put("y", 2L);
    map.put("z", 3L);
    return Stream.of(
        Arguments.of(new Position(1, 2, 3), new Position(1, 2, 3)),
        Arguments.of(new Position(1, 2, 3), new MCList(Arrays.asList(1L, 2L, 3L))),
        Arguments.of(new Position(1, 2, 3), map)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForImplicitCastError")
  void implicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForImplicitCastError() {
    MCMap map = new MCMap();
    map.put("x", 1);
    map.put("y", 1);
    map.put("z", 1);
    return Stream.of(
        Arguments.of(true),
        Arguments.of(1),
        Arguments.of(1.0),
        Arguments.of(""),
        Arguments.of(new MCList(Arrays.asList(1, 2, 3))),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(map),
        Arguments.of(new Item()),
//        Arguments.of(new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of((Object) null),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCastError")
  void explicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCastError() {
    MCMap map1 = new MCMap();
    map1.put("x", 1.0);
    map1.put("y", 2.0);
    map1.put("z", 3.0);
    return Stream.of(
        Arguments.of(true),
        Arguments.of(1),
        Arguments.of(1.0),
        Arguments.of(""),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new Item()),
//        Arguments.of(new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of((Object) null),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone")),
        Arguments.of(map1)
    );
  }

  @Test
  void invalidListCastError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), new MCList(Arrays.asList(1, 2))));
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), new MCList(Arrays.asList(1, 2, 3, 4))));
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), new MCList(Arrays.asList("1", 2, 3))));
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), new MCList(Arrays.asList("a", 2, 3))));
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), new MCList(Arrays.asList(1, "a", 3))));
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), new MCList(Arrays.asList(1, 2, "a"))));
  }

  @Test
  void invalidMapCastError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), new MCMap()));
    MCMap map = new MCMap();
    map.put("x", 1);
    map.put("y", 2);
    map.put("z", 3);
    map.put("a", 3);
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map));
    MCMap map1 = new MCMap();
    map1.put("x", "a");
    map1.put("y", 2);
    map1.put("z", 3);
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map1));
    MCMap map2 = new MCMap();
    map2.put("x", 1);
    map2.put("y", "a");
    map2.put("z", 3);
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map2));
    MCMap map3 = new MCMap();
    map3.put("x", 1);
    map3.put("y", 2);
    map3.put("z", "a");
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map3));
    MCMap map4 = new MCMap();
    map4.put("x", "1");
    map4.put("y", 2);
    map4.put("z", 3);
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map4));
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(new Position(1, 1, 1)));
    assertTrue(this.typeInstance.toBoolean(new Position(0, 0, 0)));
  }

  @Test
  void copy() {
    Position pos = new Position(0, 0, 0);
    assertSame(pos, this.typeInstance.copy(this.p.getScope(), pos));
    assertEquals(pos, this.typeInstance.copy(this.p.getScope(), pos));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(PosType.NAME_KEY, "pos");
    tag.setInteger(PosType.X_KEY, 1);
    tag.setInteger(PosType.Y_KEY, 2);
    tag.setInteger(PosType.Z_KEY, 3);
    assertEquals(tag, this.typeInstance.writeToNBT(new Position(1, 2, 3)));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(PosType.NAME_KEY, "pos");
    tag.setInteger(PosType.X_KEY, 1);
    tag.setInteger(PosType.Y_KEY, 2);
    tag.setInteger(PosType.Z_KEY, 3);
    assertEquals(new Position(1, 2, 3), this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<PosType> getTypeClass() {
    return PosType.class;
  }
}
