package net.darmo_creations.mccode.interpreter.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A map that associates string keys to any object.
 */
public class MCMap extends HashMap<String, Object> {
  /**
   * Create an empty map.
   */
  public MCMap() {
  }

  /**
   * Create a new map from the another.
   *
   * @param map The map to copy items from.
   */
  public MCMap(Map<? extends String, ?> map) {
    super(map);
  }

  @Override
  public String toString() {
    Iterator<Entry<String, Object>> i = this.entrySet().iterator();
    if (!i.hasNext()) {
      return "{}";
    }
    StringBuilder sb = new StringBuilder();
    sb.append('{');
    while (true) {
      Entry<String, Object> e = i.next();
      String key = e.getKey();
      Object value = e.getValue();
      sb.append(key);
      sb.append(':').append(' ');
      sb.append(value == this ? "(this map)" : value);
      if (!i.hasNext()) {
        return sb.append('}').toString();
      }
      sb.append(',').append(' ');
    }
  }
}
