package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.type_wrappers.FloatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SetupProgramManager.class)
class HypotFunctionTest extends BuiltinFunctionTest<HypotFunction> {
  @Test
  void getName() {
    assertEquals("hypot", this.function.getName());
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

  @Test
  void apply() {
    this.p.getScope().declareVariable(new Variable("_x0_", false, false, true, false, 3));
    this.p.getScope().declareVariable(new Variable("_x1_", false, false, true, false, 4));
    assertEquals(5.0, this.function.apply(this.p.getScope()));
  }

  @Override
  String functionName() {
    return "hypot";
  }
}
