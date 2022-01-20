package net.darmo_creations.naissancee.mccode.interpreter.types;

import java.util.ArrayList;
import java.util.Collection;

public class MCList extends ArrayList<Object> {
  public MCList() {
  }

  public MCList(Collection<?> collection) {
    super(collection);
  }
}
