package net.darmo_creations.naissancee.mccode.interpreter.types;

import java.util.Collection;
import java.util.HashSet;

public class MCSet extends HashSet<Object> {
  public MCSet() {
  }

  public MCSet(Collection<?> collection) {
    super(collection);
  }
}
