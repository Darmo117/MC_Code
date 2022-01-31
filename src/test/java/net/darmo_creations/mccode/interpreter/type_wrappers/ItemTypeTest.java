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
import org.junit.jupiter.api.Disabled;
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
class ItemTypeTest extends TypeTest<ItemType> {
  @Test
  void getName() {
    assertEquals("item", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(Item.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertFalse(this.typeInstance.getPropertiesNames().isEmpty());
  }

  @Test
  @Disabled
  void getProperty() { // FIXME Minecraft classes not initialized
//    assertEquals(new ResourceLocation("minecraft:stick"), this.typeInstance.getProperty(this.p.getScope(), Items.STICK, "id"));
//    assertEquals(64, this.typeInstance.getProperty(this.p.getScope(), Items.STICK, "max_stack_size"));
  }

  @Test
  void getPropertyError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getProperty(this.p.getScope(), new Item(), "a"));
  }

  @Test
  void setPropertyError() { // FIXME Minecraft classes not initialized
//    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), Items.STICK, "id", "s"));
//    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), Items.STICK, "max_stack_size", 1));
    assertThrows(EvaluationException.class, () -> this.typeInstance.setProperty(this.p.getScope(), new Item(), "a", "s"));
  }

  @Test
  void getMethodError() {
    assertThrows(EvaluationException.class, () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @Test
  void applyUnaryOperator() {
    assertEquals(false, this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.NOT, new Item(), null, null, false));
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"MINUS", "LENGTH", "ITERATE"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, new Item(), null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, Item self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
//        Arguments.of(BinaryOperator.PLUS, Items.STICK, "a", "minecraft:sticka"), // FIXME Minecraft classes not initialized

        Arguments.of(BinaryOperator.EQUAL, new Item(), true, false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), false, false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), 1, false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), 0, false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), 1.0, false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), 0.0, false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), null, false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), "", false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), "1", false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), "0", false),
//        Arguments.of(BinaryOperator.EQUAL, Items.STICK, Items.STICK, true), // FIXME Minecraft classes not initialized
        Arguments.of(BinaryOperator.EQUAL, new Item(), new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, new Item(), new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), 1, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), 0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), 1.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), 0.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), "1", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), "0", true),
//        Arguments.of(BinaryOperator.NOT_EQUAL, Items.STICK, Items.STICK, false), // FIXME Minecraft classes not initialized
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new Item(), new ResourceLocation("minecraft:stone"), true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, Item self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new Item(), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, new Item(), o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    Item self = new Item();
    assertSame(self, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, self, o, null, false));
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
  void applyTernaryOperatorError(TernaryOperator operator, Item self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), 1, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), new BlockPos(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new Item(), new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @Test
  void implicitCast() {
    Item item = new Item();
    assertSame(item, this.typeInstance.implicitCast(this.p.getScope(), item));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  @Disabled
  void explicitCastError(Item expected, Object o) {
    assertEquals(expected, this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCast() { // FIXME Minecraft classes not initialized
    return Stream.of(
//        Arguments.of(??, Blocks.STONE),
//        Arguments.of(Items.STICK, new ResourceLocation("minecraft:stick")),
//        Arguments.of(Items.STICK, "minecraft:stick")
    );
  }

  @Test
  @Disabled
  void explicitCastWrongStringError() {
//    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), "")); // FIXME Minecraft classes not initialized
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
        Arguments.of((Object) null),
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new BlockPos(1, 1, 1)),
        Arguments.of(new Range(1, 1, 1))
    );
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(new Item()));
  }

  @Test
  void copy() {
    Item item = new Item();
    assertSame(item, this.typeInstance.copy(this.p.getScope(), item));
  }

  @Test
  @Disabled
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(ItemType.NAME_KEY, "item");
    tag.setString(ItemType.ID_KEY, "minecraft:stick");
//    assertEquals(tag, this.typeInstance.writeToNBT(Items.STICK)); // FIXME Minecraft classes not initialized
  }

  @Test
  @Disabled
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(ItemType.NAME_KEY, "item");
    tag.setString(ItemType.ID_KEY, "minecraft:stick");
//    assertEquals(Items.STICK, this.typeInstance.readFromNBT(this.p.getScope(), tag)); // FIXME Minecraft classes not initialized
  }

  @Override
  Class<ItemType> getTypeClass() {
    return ItemType.class;
  }
}
