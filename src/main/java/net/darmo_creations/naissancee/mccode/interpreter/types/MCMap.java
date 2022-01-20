package net.darmo_creations.naissancee.mccode.interpreter.types;

import java.util.HashMap;
import java.util.Map;

public class MCMap extends HashMap<String, Object> {
  public MCMap() {
  }

  public MCMap(Map<? extends String, ?> map) {
    super(map);
  }
}
