package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

public class SetType extends Type<MCSet> {
  public static final String NAME = "set";

  private static final String VALUES_KEY = "Values";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<MCSet> getWrappedType() {
    return MCSet.class;
  }

  @Property(name = "size")
  public Integer getSize(final MCMap self) {
    return self.size();
  }

  @Override
  protected Object __add__(final Scope scope, MCSet self, final Object o, final boolean inPlace) {
    MCSet other = this.implicitCast(scope, o);
    if (inPlace) {
      return this.add(self, other);
    }
    return this.add(new MCSet(self), other);
  }

  private Object add(MCSet set1, final MCSet set2) {
    set1.addAll(set2);
    return set1;
  }

  @Override
  protected Object __sub__(final Scope scope, MCSet self, final Object o, final boolean inPlace) {
    MCSet other = this.implicitCast(scope, o);
    if (inPlace) {
      return this.sub(self, other);
    }
    return this.sub(new MCSet(self), other);
  }

  private Object sub(MCSet set1, final MCSet set2) {
    set1.removeAll(set2);
    return set1;
  }

  @Override
  protected Object __in__(final Scope scope, final MCSet self, final Object o) {
    return self.contains(o);
  }

  @Override
  protected boolean __bool__(final MCSet self) {
    return !self.isEmpty();
  }

  @Override
  protected Iterator<?> __iter__(final Scope scope, final MCSet self) {
    return self.iterator();
  }

  @Override
  public MCSet implicitCast(Scope scope, final Object o) {
    if (o instanceof Set || o instanceof List) {
      return new MCSet((Collection<?>) o);
    } else if (o instanceof Map) {
      return new MCSet(((Map<?, ?>) o).keySet());
    }
    return super.implicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final MCSet self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    NBTTagList list = new NBTTagList();
    self.forEach(v -> list.appendTag(scope.getInterpreter().getTypeForValue(v).writeToNBT(scope, v)));
    tag.setTag(VALUES_KEY, list);
    return tag;
  }

  @Override
  public MCSet readFromNBT(final Scope scope, final NBTTagCompound tag) {
    MCSet set = new MCSet();
    NBTTagList tagsList = tag.getTagList(VALUES_KEY, new NBTTagCompound().getId());
    tagsList.forEach(t -> set.add(scope.getInterpreter().getTypeForName(((NBTTagCompound) t).getString(Type.NAME_KEY)).readFromNBT(scope, (NBTTagCompound) t)));
    return set;
  }
}
