package net.darmo_creations.mccode.interpreter.types;

import java.util.Iterator;
import java.util.Objects;

/**
 * A range is an iterable object that generates integers between two bounds.
 */
public class Range implements Iterable<Integer>, Cloneable {
  private final int start; // Included
  private final int end; // Excluded
  private final int step;

  /**
   * Create a range generator.
   *
   * @param start Range’s start value (included).
   * @param end   Range’s end value (excluded).
   * @param step  Range’s step.
   */
  public Range(final int start, final int end, final int step) {
    this.start = start;
    this.end = end;
    this.step = step;
  }

  /**
   * Return range’s start value.
   */
  public int getStart() {
    return this.start;
  }

  /**
   * Return range’s end value.
   */
  public int getEnd() {
    return this.end;
  }

  /**
   * Return range’s step.
   */
  public int getStep() {
    return this.step;
  }

  @Override
  public Iterator<Integer> iterator() {
    return new Iterator<Integer>() {
      private int i = Range.this.start;

      @Override
      public boolean hasNext() {
        return this.i < Range.this.end - Range.this.step + 1;
      }

      @Override
      public Integer next() {
        int i = this.i;
        this.i += Range.this.step;
        return i;
      }
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Range integers = (Range) o;
    return this.start == integers.start && this.end == integers.end && this.step == integers.step;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.start, this.end, this.step);
  }

  @Override
  public Range clone() {
    try {
      return (Range) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError(e);
    }
  }
}
