package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.type_wrappers.IntType;
import net.darmo_creations.mccode.interpreter.type_wrappers.RangeType;
import net.darmo_creations.mccode.interpreter.types.Range;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SetupProgramManager.class)
class RangeFunctionTest extends BuiltinFunctionTest<RangeFunction> {
  @Test
  void getName() {
    assertEquals("range", this.function.getName());
  }

  @Test
  void getReturnType() {
    assertSame(RangeType.class, this.function.getReturnType().getClass());
  }

  @Test
  void getParameters() {
    assertEquals(Arrays.asList(new Parameter("_x0_", ProgramManager.getTypeInstance(IntType.class)),
            new Parameter("_x1_", ProgramManager.getTypeInstance(IntType.class)),
            new Parameter("_x2_", ProgramManager.getTypeInstance(IntType.class))),
        this.function.getParameters());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApply")
  void apply(long start, long end, long step) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, start));
    this.p.getScope().declareVariable(new Variable("_x1_", false, false, true, false, end));
    this.p.getScope().declareVariable(new Variable("_x2_", false, false, true, false, step));
    assertEquals(new Range(start, end, step), this.function.apply(this.p.getScope()));
  }

  public static Stream<Arguments> provideArgsForApply() {
    return Stream.of(
        Arguments.of(1, 1, 1),
        Arguments.of(1, 2, 1),
        Arguments.of(2, 1, 1)
    );
  }

  @Override
  String functionName() {
    return "range";
  }
}
