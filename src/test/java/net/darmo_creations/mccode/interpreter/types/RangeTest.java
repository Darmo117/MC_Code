package net.darmo_creations.mccode.interpreter.types;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@ExtendWith(SetupProgramManager.class)
class RangeTest extends TestBase {
  Range r;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    this.r = new Range(1, 3, 1);
  }

  @Test
  void getStart() {
    assertEquals(1, this.r.getStart());
  }

  @Test
  void getEnd() {
    assertEquals(3, this.r.getEnd());
  }

  @Test
  void getStep() {
    assertEquals(1, this.r.getStep());
  }

  @Test
  void iterator() {
    int[] values = {1, 2};
    Iterator<Long> it = this.r.iterator();
    int i = 0;
    while (it.hasNext()) {
      assertEquals(values[i], it.next());
      i++;
    }
  }

  @Test
  void iteratorStep2() {
    int[] values = {1, 3};
    Iterator<Long> it = new Range(1, 4, 2).iterator();
    int i = 0;
    while (it.hasNext()) {
      assertEquals(values[i], it.next());
      i++;
    }
  }

  @Test
  void iteratorNegativeStep() {
    int[] values = {2, 1};
    Iterator<Long> it = new Range(2, 0, -1).iterator();
    int i = 0;
    while (it.hasNext()) {
      assertEquals(values[i], it.next());
      i++;
    }
  }

  @Test
  void iteratorNegativeStep2() {
    int[] values = {4, 2};
    Iterator<Long> it = new Range(4, 0, -2).iterator();
    int i = 0;
    while (it.hasNext()) {
      assertEquals(values[i], it.next());
      i++;
    }
  }

  @Test
  void testEquals() {
    assertEquals(new Range(1, 3, 1), this.r);
  }

  @Test
  void testClone() {
    Range r = this.r.clone();
    assertEquals(this.r, r);
    assertNotSame(this.r, r);
  }
}
