package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

public class ListType extends Type<MCList> {
  public static final String NAME = "list";

  private static final String VALUES_KEY = "Values";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<MCList> getWrappedType() {
    return MCList.class;
  }

  @Property(name = "length")
  public Integer getLength(final MCList self) {
    return self.size();
  }

  @Property(name = "clear")
  public Void clear(final MCList self) {
    self.clear();
    return null;
  }

  @Override
  protected Object __get_item__(final Scope scope, final MCList self, final Object key) {
    if (!(key instanceof Integer)) {
      throw new CastException(scope, scope.getInterpreter().getTypeInstance(IntType.class),
          scope.getInterpreter().getTypeForValue(key));
    }
    return self.get((Integer) key);
  }

  @Override
  protected void __set_item__(final Scope scope, MCList self, final Object key, final Object value) {
    if (!(key instanceof Integer)) {
      throw new CastException(scope, scope.getInterpreter().getTypeInstance(IntType.class),
          scope.getInterpreter().getTypeForValue(key));
    }
    self.set((Integer) key, value);
  }

  @Override
  protected void __del_item__(final Scope scope, MCList self, final Object key) {
    if (!(key instanceof Integer)) {
      throw new CastException(scope, scope.getInterpreter().getTypeInstance(IntType.class),
          scope.getInterpreter().getTypeForValue(key));
    }
    self.remove((int) (Integer) key);
  }

  @Override
  protected Object __add__(final Scope scope, MCList self, final Object o, final boolean inPlace) {
    MCList other = this.implicitCast(scope, o);
    if (inPlace) {
      return this.add(self, other);
    }
    return this.add(new MCList(self), other);
  }

  private Object add(MCList list1, final MCList list2) {
    list1.addAll(list2);
    return list1;
  }

  @Override
  protected Object __sub__(final Scope scope, MCList self, final Object o, final boolean inPlace) {
    MCList other = this.implicitCast(scope, o);
    if (inPlace) {
      return this.sub(self, other);
    }
    return this.sub(new MCList(self), other);
  }

  private Object sub(MCList list1, final MCList list2) {
    list1.removeAll(list2);
    return list1;
  }

  @Override
  protected Object __mul__(final Scope scope, MCList self, final Object o, final boolean inPlace) {
    int nb = scope.getInterpreter().getTypeInstance(IntType.class).implicitCast(scope, o);
    if (inPlace) {
      return this.mul(self, nb);
    }
    return this.mul(new MCList(self), nb);
  }

  private Object mul(MCList list, final int nb) {
    if (nb <= 0) {
      list.clear();
      return list;
    }
    for (int i = 0; i < nb - 1; i++) {
      //noinspection CollectionAddedToSelf
      list.addAll(list);
    }
    return list;
  }

  @Override
  protected Object __in__(final Scope scope, final MCList self, final Object o) {
    return self.contains(o);
  }

  @Override
  protected boolean __bool__(final MCList self) {
    return !self.isEmpty();
  }

  @Override
  protected Iterator<?> __iter__(final Scope scope, final MCList self) {
    return self.iterator();
  }

  @Override
  public MCList implicitCast(final Scope scope, final Object o) {
    if (o instanceof List || o instanceof Set) {
      return new MCList((Collection<?>) o);
    } else if (o instanceof Map) {
      return new MCList(((Map<?, ?>) o).keySet());
    }
    return super.implicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final MCList self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    NBTTagList list = new NBTTagList();
    self.forEach(v -> list.appendTag(scope.getInterpreter().getTypeForValue(v).writeToNBT(scope, v)));
    tag.setTag(VALUES_KEY, list);
    return tag;
  }

  @Override
  public MCList readFromNBT(final Scope scope, final NBTTagCompound tag) {
    MCList list = new MCList();
    NBTTagList tagsList = tag.getTagList(VALUES_KEY, new NBTTagCompound().getId());
    tagsList.forEach(t -> list.add(scope.getInterpreter().getTypeForName(((NBTTagCompound) t).getString(Type.NAME_KEY)).readFromNBT(scope, (NBTTagCompound) t)));
    return list;
  }
}
