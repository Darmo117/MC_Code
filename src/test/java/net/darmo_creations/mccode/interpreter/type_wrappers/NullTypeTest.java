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

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class NullTypeTest extends TypeTest<NullType> {
  @Test
  void getName() {
    assertEquals("null", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Void.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertFalse(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertTrue(this.typeInstance.getPropertiesNames(null).isEmpty());
  }

  @Test
  void getPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getPropertyValue(this.p.getScope(), null, "a"));
  }

  @Test
  void setPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.setPropertyValue(this.p.getScope(), null, "a", true));
  }

  @Test
  void getMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod("a"));
  }

  @Test
  void applyUnaryOperator() {
    assertEquals(true, this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.NOT, null, null, null, false));
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"MINUS", "LENGTH", "ITERATE"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, null, null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, Void self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, null, "a", "nulla"),

        Arguments.of(BinaryOperator.EQUAL, null, true, false),
        Arguments.of(BinaryOperator.EQUAL, null, false, false),
        Arguments.of(BinaryOperator.EQUAL, null, 1, false),
        Arguments.of(BinaryOperator.EQUAL, null, 0, false),
        Arguments.of(BinaryOperator.EQUAL, null, 1.0, false),
        Arguments.of(BinaryOperator.EQUAL, null, 0.0, false),
        Arguments.of(BinaryOperator.EQUAL, null, null, true),
        Arguments.of(BinaryOperator.EQUAL, null, "", false),
        Arguments.of(BinaryOperator.EQUAL, null, "1", false),
        Arguments.of(BinaryOperator.EQUAL, null, "0", false),
        Arguments.of(BinaryOperator.EQUAL, null, new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, null, new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, null, new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, null, new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, null, new Position(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, null, new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, null, new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.NOT_EQUAL, null, true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, 1, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, 0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, 1.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, 0.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, null, false),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, "1", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, "0", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, new Position(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, null, new ResourceLocation("minecraft:stone"), true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, Void self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, null, true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, new Position(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, null, new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertNull(this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, null, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, null, o, null, false));
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
  void applyTernaryOperatorError(TernaryOperator operator, Void self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, null, true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, 1, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, new Position(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, null, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @Test
  void implicitCast() {
    assertNull(this.typeInstance.implicitCast(this.p.getScope(), null));
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
        Arguments.of(new MCList(Collections.singletonList(1))),
        Arguments.of(new MCList()),
        Arguments.of(new MCSet(Collections.singletonList(1))),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1))),
        Arguments.of(new MCMap()),
        Arguments.of(new Item()),
//        Arguments.of(new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of(new Position(0, 0, 0)),
        Arguments.of(new Position(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone")),
        Arguments.of("a"),
        Arguments.of("")
    );
  }

  @Test
  void toBoolean() {
    assertFalse(this.typeInstance.toBoolean(null));
  }

  @Test
  void copy() {
    assertNull(this.typeInstance.copy(this.p.getScope(), null));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(NullType.NAME_KEY, "null");
    assertEquals(tag, this.typeInstance.writeToNBT(null));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(NullType.NAME_KEY, "null");
    assertNull(this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<NullType> getTypeClass() {
    return NullType.class;
  }
}
