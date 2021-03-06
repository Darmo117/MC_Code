package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.BooleanType;
import net.darmo_creations.mccode.interpreter.types.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
class SortedFunctionTest extends BuiltinFunctionTest<SortedFunction> {
  @Test
  void getName() {
    assertEquals("sorted", this.function.getName());
  }

  @Test
  void getReturnType() {
    assertSame(AnyType.class, this.function.getReturnType().getClass());
  }

  @Test
  void getParameters() {
    assertEquals(Arrays.asList(new Parameter("_x0_", ProgramManager.getTypeInstance(AnyType.class)),
            new Parameter("_x1_", ProgramManager.getTypeInstance(BooleanType.class))),
        this.function.getParameters());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApply")
  void apply(Object o, boolean reversed, Object expected, boolean testSame) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, o));
    this.p.getScope().declareVariable(new Variable("_x1_", false, false, true, false, reversed));
    Object res = this.function.apply(this.p.getScope());
    assertEquals(expected, res);
    if (testSame) {
      assertNotSame(res, o);
    }
  }

  public static Stream<Arguments> provideArgsForApply() {
    return Stream.of(
        Arguments.of(new MCList(), false, new MCList(), true),
        Arguments.of(new MCList(Collections.singletonList(1L)), false, new MCList(Collections.singletonList(1L)), true),
        Arguments.of(new MCList(Arrays.asList(1L, 3L, 2L)), false, new MCList(Arrays.asList(1L, 2L, 3L)), true),
        Arguments.of("", false, "", false),
        Arguments.of("a", false, "a", false),
        Arguments.of("a??b", false, "ab??", false),

        Arguments.of(new MCList(), true, new MCList(), true),
        Arguments.of(new MCList(Collections.singletonList(1L)), true, new MCList(Collections.singletonList(1L)), true),
        Arguments.of(new MCList(Arrays.asList(1L, 3L, 2L)), true, new MCList(Arrays.asList(3L, 2L, 1L)), true),
        Arguments.of("", true, "", false),
        Arguments.of("a", true, "a", false),
        Arguments.of("a??b", true, "??ba", false)
    );
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApplyError")
  void applyError(Object o) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, o));
    this.p.getScope().declareVariable(new Variable("_x1_", false, false, true, false, false));
    assertThrows(CastException.class, () -> this.function.apply(this.p.getScope()));
  }

  public static Stream<Arguments> provideArgsForApplyError() {
    return Stream.of(
        Arguments.of(true),
        Arguments.of(1L),
        Arguments.of(1.0),
        Arguments.of((Object) null),
        Arguments.of(new MCSet()),
        Arguments.of(new MCMap()),
        Arguments.of(new Item()),
//        Arguments.of(Blocks.STONE), // TODO not initialized
        Arguments.of(new Position(0, 0, 0)),
        Arguments.of(new Range(1, 1, 1)),
        Arguments.of(new ResourceLocation("minecraft:stone"))
    );
  }

  @Override
  String functionName() {
    return "sorted";
  }
}
