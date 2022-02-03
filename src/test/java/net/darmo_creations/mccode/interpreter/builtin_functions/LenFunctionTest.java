package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.UnsupportedOperatorException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.IntType;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.darmo_creations.mccode.interpreter.types.Range;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class LenFunctionTest extends BuiltinFunctionTest<LenFunction> {
  @Test
  void getName() {
    assertEquals("len", this.function.getName());
  }

  @Test
  void getReturnType() {
    assertSame(IntType.class, this.function.getReturnType().getClass());
  }

  @Test
  void getParameters() {
    assertEquals(Collections.singletonList(new Parameter("_x0_", ProgramManager.getTypeInstance(AnyType.class))), this.function.getParameters());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApply")
  void apply(Object o, int expectedSize) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, o));
    assertEquals(expectedSize, this.function.apply(this.p.getScope()));
  }

  static Stream<Arguments> provideArgsForApply() {
    return Stream.of(
        Arguments.of("", 0),
        Arguments.of("ab", 2),
        Arguments.of(new MCList(), 0),
        Arguments.of(new MCList(Arrays.asList(1, 2)), 2),
        Arguments.of(new MCSet(), 0),
        Arguments.of(new MCSet(Arrays.asList(1, 2)), 2),
        Arguments.of(new MCMap(), 0),
        Arguments.of(new MCMap(Collections.singletonMap("a", 1)), 1)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyError")
  void applyError(Object o) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, o));
    assertThrows(UnsupportedOperatorException.class, () -> this.function.apply(this.p.getScope()));
  }

  static Stream<Arguments> provideArgsForApplyError() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(1),
        Arguments.of(1.0),
        Arguments.of((Object) null),
        Arguments.of(new Item()),
//        Arguments.of(Blocks.STONE), // TODO not initialized
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @Override
  String functionName() {
    return "len";
  }
}
