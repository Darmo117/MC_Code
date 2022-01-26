package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapType extends Type<MCMap> {
  public static final String NAME = "map";

  private static final String ENTRIES_KEY = "Entries";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<MCMap> getWrappedType() {
    return MCMap.class;
  }

  @Property(name = "keys")
  public MCSet getKeys(final MCMap self) {
    return new MCSet(self.keySet());
  }

  @Property(name = "values")
  public MCSet getValues(final MCMap self) {
    return new MCSet(self.values());
  }

  @Property(name = "size")
  public Integer getSize(final MCMap self) {
    return self.size();
  }

  @Override
  protected Object __get_item__(final Scope scope, final MCMap self, final Object key) {
    if (!(key instanceof String)) {
      throw new CastException(scope, scope.getInterpreter().getTypeInstance(StringType.class),
          scope.getInterpreter().getTypeForValue(self));
    }
    return self.get((String) key);
  }

  @Override
  protected void __set_item__(final Scope scope, MCMap self, final Object key, final Object value) {
    if (!(key instanceof String)) {
      throw new CastException(scope, scope.getInterpreter().getTypeInstance(StringType.class),
          scope.getInterpreter().getTypeForValue(self));
    }
    self.put((String) key, value);
  }

  @Override
  protected void __del_item__(final Scope scope, MCMap self, final Object key) {
    if (!(key instanceof String)) {
      throw new CastException(scope, scope.getInterpreter().getTypeInstance(StringType.class),
          scope.getInterpreter().getTypeForValue(self));
    }
    self.remove(key);
  }

  @Override
  protected Object __add__(final Scope scope, MCMap self, final Object o, final boolean inPlace) {
    MCMap other = this.implicitCast(scope, o);
    if (inPlace) {
      return this.add(self, other);
    }
    return this.add(new MCMap(self), other);
  }

  private Object add(MCMap map1, final MCMap map2) {
    map1.putAll(map2);
    return map1;
  }

  @Override
  protected Object __sub__(final Scope scope, MCMap self, final Object o, final boolean inPlace) {
    MCList keys = scope.getInterpreter().getTypeInstance(ListType.class).implicitCast(scope, o);
    if (inPlace) {
      return this.sub(scope, self, keys);
    }
    return this.sub(scope, new MCMap(self), keys);
  }

  private Object sub(final Scope scope, MCMap map, final MCList keys) {
    StringType stringType = scope.getInterpreter().getTypeInstance(StringType.class);
    keys.stream().map(k -> stringType.implicitCast(scope, k)).forEach(map::remove);
    return map;
  }

  @Override
  protected Object __in__(final Scope scope, final MCMap self, final Object o) {
    if (!(o instanceof String)) {
      return false;
    }
    return self.containsKey(o);
  }

  @Override
  protected boolean __bool__(final MCMap self) {
    return !self.isEmpty();
  }

  @Override
  protected Iterator<?> __iter__(final Scope scope, final MCMap self) {
    return self.keySet().iterator();
  }

  @Override
  public MCMap implicitCast(final Scope scope, final Object o) {
    if (o instanceof Map) {
      try {
        //noinspection unchecked
        return new MCMap((Map<String, ?>) o);
      } catch (ClassCastException e) {
        ProgramManager programManager = scope.getInterpreter();
        throw new CastException(scope, programManager.getTypeInstance(MapType.class), programManager.getTypeForValue(o));
      }
    } else {
      Type<?> type = scope.getInterpreter().getTypeForValue(o);
      List<String> properties = type.getPropertiesNames();
      MCMap map = new MCMap();
      if (!properties.isEmpty()) {
        for (String s : properties) {
          map.put(s, type.getProperty(scope, o, s));
        }
      }
      return map;
    }
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final MCMap self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    NBTTagCompound entries = new NBTTagCompound();
    self.forEach((key, value) -> entries.setTag(key, scope.getInterpreter().getTypeForValue(value).writeToNBT(scope, value)));
    tag.setTag(ENTRIES_KEY, entries);
    return tag;
  }

  @Override
  public MCMap readFromNBT(final Scope scope, final NBTTagCompound tag) {
    MCMap map = new MCMap();
    NBTTagCompound entries = tag.getCompoundTag(ENTRIES_KEY);
    entries.getKeySet().stream()
        .map(k -> new ImmutablePair<>(k, entries.getCompoundTag(k)))
        .forEach(e -> map.put(e.getKey(), scope.getInterpreter().getTypeForName(e.getValue().getString(Type.NAME_KEY)).readFromNBT(scope, e.getValue())));
    return map;
  }
}
