package net.darmo_creations.mccode.interpreter.builtin_functions;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.TestBase;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class BuiltinFunctionTest<T extends BuiltinFunction> extends TestBase {
  T function;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    //noinspection unchecked
    this.function = (T) ProgramManager.getBuiltinFunction(this.functionName());
  }

  @Test
  void getDoc() {
    assertTrue(this.function.getDoc().isPresent());
  }

  abstract String functionName();
}
