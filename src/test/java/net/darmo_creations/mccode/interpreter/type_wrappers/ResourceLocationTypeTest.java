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

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class ResourceLocationTypeTest extends TypeTest<ResourceLocationType> {
  @Test
  void getName() {
    assertEquals("resource_location", this.typeInstance.getName());
  }

  @Test
  void getWrappedType() {
    assertSame(ResourceLocation.class, this.typeInstance.getWrappedType());
  }

  @Test
  void generateCastOperator() {
    assertTrue(this.typeInstance.generateCastOperator());
  }

  @Test
  void getPropertiesNames() {
    assertEquals(Arrays.asList("namespace", "path"), this.typeInstance.getPropertiesNames(new ResourceLocation("minecraft:stone")));
  }

  @Test
  void getProperty() {
    assertEquals("minecraft", this.typeInstance.getProperty(this.p.getScope(), new ResourceLocation("minecraft:stone"), "namespace"));
    assertEquals("stone", this.typeInstance.getProperty(this.p.getScope(), new ResourceLocation("minecraft:stone"), "path"));
  }

  @Test
  void getUndefinedPropertyError() {
    assertThrows(EvaluationException.class,
        () -> this.typeInstance.getProperty(this.p.getScope(), new ResourceLocation("a"), "a"));
  }

  @Test
  void setPropertyError() {
    assertThrows(EvaluationException.class,
        () -> this.typeInstance.setProperty(this.p.getScope(), new ResourceLocation("a"), "namespace", "b"));
    assertThrows(EvaluationException.class,
        () -> this.typeInstance.setProperty(this.p.getScope(), new ResourceLocation("a"), "path", "b"));
  }

  @Test
  void setUndefinedPropertyError() {
    assertThrows(EvaluationException.class,
        () -> this.typeInstance.setProperty(this.p.getScope(), new ResourceLocation("a"), "a", "b"));
  }

  @Test
  void getUndefinedMethodError() {
    assertThrows(EvaluationException.class,
        () -> this.typeInstance.getMethod(this.p.getScope(), "a"));
  }

  @Test
  void applyUnaryOperator() {
    assertEquals(false, this.typeInstance.applyOperator(this.p.getScope(), UnaryOperator.NOT, new ResourceLocation("a"), null, null, false));
  }

  @ParameterizedTest
  @EnumSource(value = UnaryOperator.class, names = {"MINUS", "LENGTH", "ITERATE"})
  void applyUnaryOperatorError(UnaryOperator operator) {
    assertThrows(UnsupportedOperatorException.class,
        () -> this.typeInstance.applyOperator(this.p.getScope(), operator, new ResourceLocation("a"), null, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperator")
  void applyBinaryOperator(BinaryOperator operator, ResourceLocation self, Object o, Object expected) {
    Object r = this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false);
    assertEquals(expected, r);
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperator() {
    return Stream.of(
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), true, false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), false, false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), 1, false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), 0, false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), 1.0, false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), 0.0, false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), null, false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), "", false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), "minecraft:stone", true),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), new Item(), false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), new MCList(), false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), new MCSet(), false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), new MCMap(), false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), new Range(1, 2, 1), false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), false),
        Arguments.of(BinaryOperator.EQUAL, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), true),

        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), true, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), false, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), 1, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), 0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), 1.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), 0.0, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), null, true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), "", true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), "minecraft:stone", false),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), new Item(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), new MCList(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), new MCSet(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), new MCMap(), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), new Range(1, 2, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), true),
        Arguments.of(BinaryOperator.NOT_EQUAL, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), false),

        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecrafta:stone"), new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stonea"), new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecrafta:stonea"), new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecrafta:stone"), false),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stonea"), false),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecrafta:stonea"), false),

        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecrafta:stone"), new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stonea"), new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecrafta:stonea"), new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecrafta:stone"), false),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stonea"), false),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecrafta:stonea"), false),

        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecrafta:stone"), new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stonea"), new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecrafta:stonea"), new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecrafta:stone"), true),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stonea"), true),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecrafta:stonea"), true),

        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), true),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecrafta:stone"), new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stonea"), new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecrafta:stonea"), new ResourceLocation("minecraft:stone"), false),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecrafta:stone"), true),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stonea"), true),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecrafta:stonea"), true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyBinaryOperatorError")
  void applyBinaryOperatorError(BinaryOperator operator, ResourceLocation self, Object o, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o, null, false));
  }

  public static Stream<Arguments> provideArgsForApplyBinaryOperatorError() {
    return Stream.of(
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.PLUS, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.SUB, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MUL, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DIV, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.INT_DIV, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.MOD, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.POW, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GT, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GE, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LT, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), false, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), 0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), 0.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.LE, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.IN, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.NOT_IN, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.GET_ITEM, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class),

        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), 1, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), 1.0, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), "", UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), new Item(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), null, UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), new MCList(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), new MCSet(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), new MCMap(), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), UnsupportedOperatorException.class),
        Arguments.of(BinaryOperator.DEL_ITEM, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), UnsupportedOperatorException.class)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyAndOperator(Object o) {
    ResourceLocation resource = new ResourceLocation("minecraft:stone");
    assertSame(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, resource, o, null, false));
    assertEquals(o, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.AND, resource, o, null, false));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyLogicOperator")
  void applyOrOperator(Object o) {
    ResourceLocation resource = new ResourceLocation("minecraft:stone");
    assertSame(resource, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, resource, o, null, false));
    assertEquals(resource, this.typeInstance.applyOperator(this.p.getScope(), BinaryOperator.OR, resource, o, null, false));
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
  void applyTernaryOperatorError(TernaryOperator operator, ResourceLocation self, Object o1, Object o2, Class<? extends Throwable> exceptionClass) {
    assertThrows(exceptionClass, () -> this.typeInstance.applyOperator(this.p.getScope(), operator, self, o1, o2, true));
  }

  static Stream<Arguments> provideArgsForApplyTernaryOperatorError() {
    return Stream.of(
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), true, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), 1, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), 1.0, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), "", true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), new Item(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), null, true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), new MCList(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), new MCSet(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), new MCMap(), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), new BlockPos(0, 0, 0), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), new Range(1, 1, 1), true, UnsupportedOperatorException.class),
        Arguments.of(TernaryOperator.SET_ITEM, new ResourceLocation("minecraft:stone"), new ResourceLocation("minecraft:stone"), true, UnsupportedOperatorException.class)

    );
  }

  @Test
  void explicitCastInvalidMap() {
    MCMap map = new MCMap();
    map.put("namespace", "minecraft");
    map.put("path", "stone");
    map.put("a", "b");
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map));
    MCMap map1 = new MCMap();
    map1.put("a", "minecraft");
    map1.put("path", "stone");
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map1));
    MCMap map2 = new MCMap();
    map2.put("namespace", "minecraft");
    map2.put("a", "stone");
    assertThrows(EvaluationException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map2));
    MCMap map3 = new MCMap();
    map3.put("namespace", 1);
    map3.put("path", "stone");
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map3));
    MCMap map4 = new MCMap();
    map4.put("namespace", "minecraft");
    map4.put("path", 1);
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map4));
    MCMap map5 = new MCMap();
    map5.put("namespace", "minecraft");
    map5.put("path", null);
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), map5));
  }

  @Test
  void implicitCast() {
    ResourceLocation resource = new ResourceLocation("minecraft:stone");
    assertSame(resource, this.typeInstance.implicitCast(this.p.getScope(), resource));
    assertEquals(resource, this.typeInstance.implicitCast(this.p.getScope(), resource));
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCast")
  void explicitCast(String id, Object o) {
    assertEquals(new ResourceLocation(id), this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  static Stream<Arguments> provideArgsForExplicitCast() {
    MCMap map = new MCMap();
    map.put("namespace", "minecraft");
    map.put("path", "stick");
    MCMap map1 = new MCMap();
    map1.put("namespace", null);
    map1.put("path", "stick");
    return Stream.of(
        Arguments.of("minecraft:stick", new ResourceLocation("minecraft:stick")),
        Arguments.of("minecraft:stick", "minecraft:stick"),
        Arguments.of("minecraft:stick", map),
        Arguments.of("minecraft:stick", map1)
//        Arguments.of("minecraft:stick", Items.STICK), // FIXME raises error because of sound system not initialized
//        Arguments.of("minecraft:stone", Blocks.STONE) // FIXME raises error because of sound system not initialized
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForImplicitCastError")
  void implicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.implicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForImplicitCastError() {
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
        Arguments.of("a"),
        Arguments.of("")
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForExplicitCastError")
  void explicitCastError(Object o) {
    assertThrows(CastException.class, () -> this.typeInstance.explicitCast(this.p.getScope(), o));
  }

  private static Stream<Arguments> provideArgsForExplicitCastError() {
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
        Arguments.of(new Item()),
//        Arguments.of(new Block(Material.AIR)), // FIXME raises error because of sound system not initialized
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new BlockPos(1, 1, 1))
    );
  }

  @Test
  void toBoolean() {
    assertTrue(this.typeInstance.toBoolean(new ResourceLocation("minecraft:stick")));
  }

  @Test
  void copy() {
    ResourceLocation resource = new ResourceLocation("minecraft:stick");
    assertSame(resource, this.typeInstance.copy(this.p.getScope(), resource));
    assertEquals(resource, this.typeInstance.copy(this.p.getScope(), resource));
  }

  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(ResourceLocationType.NAME_KEY, "resource_location");
    tag.setString(ResourceLocationType.VALUE_KEY, "minecraft:stone");
    assertEquals(tag, this.typeInstance.writeToNBT(new ResourceLocation("minecraft:stone")));
  }

  @Test
  void readFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(ResourceLocationType.NAME_KEY, "resource_location");
    tag.setString(ResourceLocationType.VALUE_KEY, "minecraft:stone");
    assertEquals(new ResourceLocation("minecraft:stone"), this.typeInstance.readFromNBT(this.p.getScope(), tag));
  }

  @Override
  Class<ResourceLocationType> getTypeClass() {
    return ResourceLocationType.class;
  }
}
