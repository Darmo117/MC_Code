package net.darmo_creations.mccode.interpreter;

import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;

public abstract class TestBase {
  protected Program p;

  @BeforeEach
  public void setUp() {
    ProgramManager pm = new ProgramManager(null);
    this.p = new Program("test", Collections.emptyList(), null, null, pm);
  }
}
