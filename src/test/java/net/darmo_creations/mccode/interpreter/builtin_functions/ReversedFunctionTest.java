package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.ListType;
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
class ReversedFunctionTest extends BuiltinFunctionTest<ReversedFunction> {
  @Test
  void getName() {
    assertEquals("reversed", this.function.getName());
  }

  @Test
  void getReturnType() {
    assertSame(AnyType.class, this.function.getReturnType().getClass());
  }

  @Test
  void getParameters() {
    assertEquals(Collections.singletonList(new Parameter("_x0_", ProgramManager.getTypeInstance(AnyType.class))),
        this.function.getParameters());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApply")
  void apply(Object o, Object expected, boolean testSame) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, o));
    Object res = this.function.apply(this.p.getScope());
    assertEquals(expected, res);
    if (testSame) {
      assertNotSame(res, o);
    }
  }

  public static Stream<Arguments> provideArgsForApply() {
    return Stream.of(
        Arguments.of(new MCList(), new MCList(), true),
        Arguments.of(new MCList(Collections.singletonList(1)), new MCList(Collections.singletonList(1)), true),
        Arguments.of(new MCList(Arrays.asList(1, 3, 2)), new MCList(Arrays.asList(2, 3, 1)), true),
        Arguments.of(new MCList(Arrays.asList(1, 3, 2)), new MCList(Arrays.asList(2, 3, 1)), true),
        Arguments.of("", "", false),
        Arguments.of("a", "a", false),
        Arguments.of("aψb", "bψa", false)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyError")
  void applyError(Object o) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, o));
    assertThrows(CastException.class, () -> this.function.apply(this.p.getScope()));
  }

  public static Stream<Arguments> provideArgsForApplyError() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(1),
        Arguments.of(1.0),
        Arguments.of((Object) null),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap()),
        Arguments.of(new Item()),
//        Arguments.of(Blocks.STONE), // TODO not initialized
        Arguments.of(new BlockPos(0, 0, 0)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @Override
  String functionName() {
    return "reversed";
  }
}
