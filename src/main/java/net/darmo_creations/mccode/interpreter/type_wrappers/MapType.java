package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.NoSuchKeyException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper type for {@link MCMap} class.
 * <p>
 * Maps are iterable and support the __get_item__, __set_item__ and __del_item__ operators.
 * Maps iterate over their keys.
 */
@Doc("Maps are datastructures that associate a unique key to some arbitrary data.")
public class MapType extends Type<MCMap> {
  public static final String NAME = "map";

  public static final String ENTRIES_KEY = "Entries";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<MCMap> getWrappedType() {
    return MCMap.class;
  }

  @Property(name = "keys")
  @Doc("Returns the set of all keys of this map.")
  public MCSet getKeys(final MCMap self) {
    return new MCSet(self.keySet());
  }

  @Property(name = "values")
  @Doc("Returns a list of all values of this map. Order of values in the returned list is not guaranteed.")
  public MCList getValues(final MCMap self) {
    return new MCList(self.values());
  }

  @Method(name = "clear")
  @Doc("Removes all entries from this map. Modifies this map.")
  public Void clear(final Scope scope, final MCMap self) {
    self.clear();
    return null;
  }

  @Override
  protected Object __get_item__(final Scope scope, final MCMap self, final Object key) {
    if (key instanceof String) {
      String k = (String) key;
      if (!self.containsKey(k)) {
        throw new NoSuchKeyException(scope, k);
      }
      return self.get(k);
    }
    return super.__get_item__(scope, self, key);
  }

  @Override
  protected void __set_item__(final Scope scope, MCMap self, final Object key, final Object value) {
    if (key instanceof String) {
      // Deep copy value
      self.put((String) key, ProgramManager.getTypeForValue(value).copy(scope, value));
    } else {
      super.__set_item__(scope, self, key, value);
    }
  }

  @Override
  protected void __del_item__(final Scope scope, MCMap self, final Object key) {
    if (key instanceof String) {
      String k = (String) key;
      if (!self.containsKey(k)) {
        throw new NoSuchKeyException(scope, k);
      }
      self.remove(k);
    } else {
      super.__del_item__(scope, self, key);
    }
  }

  /**
   * Add all entries of o to self.
   */
  @Override
  protected Object __add__(final Scope scope, MCMap self, final Object o, final boolean inPlace) {
    if (o instanceof MCMap) {
      MCMap other = this.implicitCast(scope, o);
      if (inPlace) {
        return this.add(scope, self, other, true);
      }
      return this.add(scope, new MCMap(self), other, false);
    } else if (o instanceof String) {
      return self.toString() + o;
    }
    return super.__add__(scope, self, o, inPlace);
  }

  private MCMap add(final Scope scope, MCMap map1, final MCMap map2, final boolean inPlace) {
    // Deep copy all elements to add
    if (!inPlace) {
      MCMap temp = this.__copy__(scope, map1);
      map1.clear();
      map1.putAll(temp);
    }
    map1.putAll(map2.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> ProgramManager.getTypeForValue(e.getValue()).copy(scope, e.getValue()))));
    return map1;
  }

  /**
   * Remove all entries from self whose key matches a key of o.
   */
  @Override
  protected Object __sub__(final Scope scope, MCMap self, final Object o, final boolean inPlace) {
    if (o instanceof MCMap) {
      MCMap other = ProgramManager.getTypeInstance(MapType.class).implicitCast(scope, o);
      if (inPlace) {
        return this.sub(self, other);
      }
      return this.sub(new MCMap(self), other);
    }
    return super.__sub__(scope, self, o, inPlace);
  }

  private MCMap sub(MCMap map, final MCMap other) {
    other.keySet().forEach(map::remove);
    return map;
  }

  @Override
  protected Object __eq__(final Scope scope, final MCMap self, final Object o) {
    if (!(o instanceof MCMap)) {
      return false;
    }
    return self.equals(o);
  }

  @Override
  protected Object __in__(final Scope scope, final MCMap self, final Object o) {
    if (o instanceof String) {
      return self.containsKey(o);
    }
    return super.__in__(scope, self, o);
  }

  @Override
  protected boolean __bool__(final MCMap self) {
    return !self.isEmpty();
  }

  /**
   * Return an iterator of this map’s keys.
   */
  @Override
  protected Iterator<?> __iter__(final Scope scope, final MCMap self) {
    return self.keySet().iterator();
  }

  @Override
  protected int __len__(final Scope scope, final MCMap self) {
    return self.size();
  }

  @Override
  protected MCMap __copy__(final Scope scope, final MCMap self) {
    return new MCMap(self.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> ProgramManager.getTypeForValue(e.getValue()).copy(scope, e.getValue()))));
  }

  @Override
  public MCMap explicitCast(final Scope scope, final Object o) {
    if (o instanceof Map) {
      try {
        //noinspection unchecked
        return new MCMap((Map<String, ?>) o);
      } catch (ClassCastException e) {
        throw new CastException(scope, ProgramManager.getTypeInstance(MapType.class), ProgramManager.getTypeForValue(o));
      }
    } else {
      Type<?> type = ProgramManager.getTypeForValue(o);
      List<String> properties = type.getPropertiesNames();
      MCMap map = new MCMap();
      if (!properties.isEmpty()) {
        for (String s : properties) {
          Object value = type.getProperty(scope, o, s);
          map.put(s, ProgramManager.getTypeForValue(value).copy(scope, value));
        }
      }
      return map;
    }
  }

  @Override
  protected NBTTagCompound _writeToNBT(final MCMap self) {
    NBTTagCompound tag = super._writeToNBT(self);
    NBTTagCompound entries = new NBTTagCompound();
    self.forEach((key, value) -> entries.setTag(key, ProgramManager.getTypeForValue(value).writeToNBT(value)));
    tag.setTag(ENTRIES_KEY, entries);
    return tag;
  }

  @Override
  public MCMap readFromNBT(final Scope scope, final NBTTagCompound tag) {
    MCMap map = new MCMap();
    NBTTagCompound entries = tag.getCompoundTag(ENTRIES_KEY);
    entries.getKeySet().stream()
        .map(k -> new ImmutablePair<>(k, entries.getCompoundTag(k)))
        .forEach(e -> map.put(e.getKey(), ProgramManager.getTypeForName(e.getValue().getString(Type.NAME_KEY)).readFromNBT(scope, e.getValue())));
    return map;
  }
}
