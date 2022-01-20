package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.Interpreter;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.darmo_creations.naissancee.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCMap;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCSet;

import java.util.List;

@WrapperType(name = MapType.NAME, trueType = MCMap.class)
public class MapType extends Type<MCMap> {
  public static final String NAME = "map";

  @Property(name = "keys")
  public MCSet getKeys(final MCMap o) {
    return new MCSet(o.keySet());
  }

  @Property(name = "values")
  public MCSet getValues(final MCMap o) {
    return new MCSet(o.values());
  }

  @Property(name = "length")
  public Integer getLength(final MCMap o) {
    return o.size();
  }

  @Override
  public MCMap getDefault() {
    return new MCMap();
  }

  @Override
  protected Object _getItem(final Object index, final MCMap o) {
    if (!(index instanceof String)) {
      throw new CastException(Interpreter.getTypeForClass(String.class), Interpreter.getTypeForValue(o));
    }
    return o.get((String) index);
  }

  @Override
  protected boolean _toBoolean(final MCMap o) {
    return !o.isEmpty();
  }

  @Override
  public MCMap cast(final Object o) {
    if (o instanceof MCMap) {
      return (MCMap) o;
    } else {
      Type<?> type = Interpreter.getTypeForValue(o);
      List<String> properties = type.getPropertieNames();
      if (!properties.isEmpty()) {
        MCMap map = new MCMap();
        for (String s : properties) {
          map.put(s, type.getProperty(s, o));
        }
        return map;
      }
    }
    return super.cast(o);
  }
}
