package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TypeTest<T extends Type<?>> extends TestBase {
  T typeInstance;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.typeInstance = ProgramManager.getTypeInstance(this.getTypeClass());
  }

  @Test
  void getDoc() {
    assertTrue(this.typeInstance.getDoc().isPresent());
  }

  abstract Class<T> getTypeClass();
}
