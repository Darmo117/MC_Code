package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SetupProgramManager.class)
class ToDegreesFunctionTest extends BuiltinFunctionTest<ToDegreesFunction> {
  @Test
  void getName() {
    assertEquals("to_degrees", this.function.getName());
  }

  @Test
  void getReturnType() {
    assertSame(FloatType.class, this.function.getReturnType().getClass());
  }

  @Test
  void getParameters() {
    assertEquals(Collections.singletonList(new Parameter("_x0_", ProgramManager.getTypeInstance(FloatType.class))), this.function.getParameters());
  }

  @ParameterizedTest
  @ValueSource(doubles = {-Math.PI, -Math.PI / 2, 0.0, Math.PI / 2, Math.PI})
  void apply(double d) {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, d));
    assertEquals(Math.toDegrees(d), this.function.apply(this.p.getScope()));
  }

  @Override
  String functionName() {
    return "to_degrees";
  }
}
