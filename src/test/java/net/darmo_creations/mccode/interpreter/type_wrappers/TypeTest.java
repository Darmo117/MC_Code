package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TypeTest<T extends Type<?>> {
  Program p;
  T typeInstance;

  @BeforeEach
  void setUp() {
    ProgramManager pm = new ProgramManager("testPM");
    this.p = new Program("test", Collections.emptyList(), null, null, pm);
    this.typeInstance = ProgramManager.getTypeInstance(this.getTypeClass());
  }

  @Test
  void getDoc() {
    assertTrue(this.typeInstance.getDoc().isPresent());
  }

  abstract Class<T> getTypeClass();
}
