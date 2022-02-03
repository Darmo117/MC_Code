package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
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
class Atan2FunctionTest extends BuiltinFunctionTest<Atan2Function> {
  @Test
  void getName() {
    assertEquals("atan2", this.function.getName());
  }

  @Test
  void getReturnType() {
    assertSame(FloatType.class, this.function.getReturnType().getClass());
  }

  @Test
  void getParameters() {
    assertEquals(Arrays.asList(new Parameter("_x0_", ProgramManager.getTypeInstance(FloatType.class)),
            new Parameter("_x1_", ProgramManager.getTypeInstance(FloatType.class))),
        this.function.getParameters());
  }

  @ParameterizedTest
  @MethodSource("provideArgsForApply")
  void apply(double d1, double d2) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, d1));
    this.p.getScope().declareVariable(new Variable("_x1_", false, false, true, false, d2));
    assertEquals(Math.atan2(d1, d2), this.function.apply(this.p.getScope()));
  }

  public static Stream<Arguments> provideArgsForApply() {
    return Stream.of(
        Arguments.of(1.0, 1.0),
        Arguments.of(-1.0, 1.0),
        Arguments.of(1.0, -1.0),
        Arguments.of(-1.0, -1.0),
        Arguments.of(0.0, 0.0)
    );
  }

  @Override
  String functionName() {
    return "atan2";
  }
}
