package net.darmo_creations.mccode.interpreter.types;

import java.util.Collection;
import java.util.HashSet;

/**
 * A set of objects.
 */
public class MCSet extends HashSet<Object> {
  /**
   * Create an empty set.
   */
  public MCSet() {
  }

  /**
   * Create a set from the given collection.
   *
   * @param collection The collection to copy values from.
   */
  public MCSet(Collection<?> collection) {
    super(collection);
  }
}
