package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;

abstract class NodeTest {
  Program p;

  @BeforeEach
  void setUp() {
    ProgramManager pm = new ProgramManager("testPM");
    this.p = new Program("test", Collections.emptyList(), null, null, pm);
  }
}
