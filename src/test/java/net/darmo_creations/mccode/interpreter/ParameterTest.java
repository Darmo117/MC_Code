package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.type_wrappers.IntType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SetupProgramManager.class)
class ParameterTest extends TestBase {
  Parameter p;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.p = new Parameter("p", ProgramManager.getTypeInstance(IntType.class));
  }

  @Test
  void getName() {
    assertEquals("p", this.p.getName());
  }

  @Test
  void getType() {
    assertEquals(ProgramManager.getTypeInstance(IntType.class), this.p.getType());
  }

  @Test
  void testEquals() {
    assertEquals(new Parameter("p", ProgramManager.getTypeInstance(IntType.class)), this.p);
  }
}
