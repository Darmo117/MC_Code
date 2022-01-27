package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.types.MCSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

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

  @Property(name = "clear")
  @Doc("Remove all values from this set.")
  public Void clear(final MCSet self) {
    self.clear();
    return null;
  }

  @Method(name = "add")
  @Doc("Add a value to this set. Modifies this set.")
  public MCSet add(final Scope scope, final MCSet self, final Object value) {
    self.add(scope.getProgramManager().getTypeForValue(value).copy(scope, value));
    return self;
  }

  @Method(name = "union")
  @Doc("Return the union of values from this set and the other. Alias of '+' operator. Does not modify this set.")
  public MCSet union(final Scope scope, final MCSet self, final MCSet other) {
    return (MCSet) this.__add__(scope, self, other, false);
  }

  @Method(name = "intersection")
  @Doc("Return the intersection of values from this set and the other. Does not modify this set.")
  public MCSet intersection(final Scope scope, final MCSet self, final MCSet other) {
    MCSet set = this.__copy__(scope, self);
    set.retainAll(other);
    return set;
  }

  /**
   * Add all elements of o in self.
   */
  @Override
  protected Object __add__(final Scope scope, MCSet self, final Object o, final boolean inPlace) {
    MCSet other = this.implicitCast(scope, o);
    if (inPlace) {
      return this.add(scope, self, other);
    }
    return this.add(scope, new MCSet(self), other);
  }

  private Object add(final Scope scope, MCSet set1, final MCSet set2) {
    // Deep copy all elements to add
    set1.addAll(this.__copy__(scope, set2));
    return set1;
  }

  /**
   * Remove all elements of o from self.
   */
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
  protected Object __eq__(final Scope scope, final MCSet self, final Object o) {
    if (!(o instanceof MCSet)) {
      return false;
    }
    return self.equals(o);
  }

  /**
   * Test whether self is a proper superset of o, that is, self >= o and self != o.
   *
   * @return True if self is a proper superset of o, false otherwise.
   */
  @Override
  protected Object __gt__(final Scope scope, final MCSet self, final Object o) {
    return this.toBoolean(this.__ge__(scope, self, o)) && !this.toBoolean(this.__eq__(scope, self, o));
  }

  /**
   * Test whether every element in o is in self.
   *
   * @return True if all elements in o are in self, false otherwise.
   */
  @Override
  protected Object __ge__(final Scope scope, final MCSet self, final Object o) {
    if (o instanceof MCSet) {
      return self.containsAll((MCSet) o);
    }
    return super.__ge__(scope, self, o);
  }

  /**
   * Test whether self is a proper subset of o, that is, self <= o and self != o.
   *
   * @return True if self is a proper subset of o, false otherwise.
   */
  @Override
  protected Object __lt__(final Scope scope, final MCSet self, final Object o) {
    return this.toBoolean(this.__le__(scope, self, o)) && !this.toBoolean(this.__eq__(scope, self, o));
  }

  /**
   * Test whether every element in self is in o.
   *
   * @return True if all elements in self are in o, false otherwise.
   */
  @Override
  protected Object __le__(final Scope scope, final MCSet self, final Object o) {
    if (o instanceof MCSet) {
      return ((MCSet) o).containsAll(self);
    }
    return super.__le__(scope, self, o);
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
  protected MCSet __copy__(final Scope scope, final MCSet self) {
    return new MCSet(self.stream().map(e -> scope.getProgramManager().getTypeForValue(e).copy(scope, e)).collect(Collectors.toSet()));
  }

  @Override
  protected int __len__(final Scope scope, final MCSet self) {
    return self.size();
  }

  @Override
  public MCSet explicitCast(final Scope scope, final Object o) {
    if (o instanceof Collection) {
      return this.__copy__(scope, new MCSet((Collection<?>) o));
    } else if (o instanceof Iterable) {
      MCSet list = new MCSet();
      for (Object e : (Iterable<?>) o) {
        list.add(scope.getProgramManager().getTypeForValue(e).copy(scope, e));
      }
      return list;
    } else if (o instanceof Map) {
      return this.__copy__(scope, new MCSet(((Map<?, ?>) o).keySet()));
    } else if (o instanceof String) {
      MCSet set = new MCSet();
      String s = (String) o;
      for (int i = 0; i < s.length(); i++) {
        set.add(String.valueOf(s.charAt(i)));
      }
      return set;
    }
    return super.explicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final MCSet self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    NBTTagList list = new NBTTagList();
    self.forEach(v -> list.appendTag(scope.getProgramManager().getTypeForValue(v).writeToNBT(scope, v)));
    tag.setTag(VALUES_KEY, list);
    return tag;
  }

  @Override
  public MCSet readFromNBT(final Scope scope, final NBTTagCompound tag) {
    MCSet set = new MCSet();
    NBTTagList tagsList = tag.getTagList(VALUES_KEY, new NBTTagCompound().getId());
    tagsList.forEach(t -> set.add(scope.getProgramManager().getTypeForName(((NBTTagCompound) t).getString(Type.NAME_KEY)).readFromNBT(scope, (NBTTagCompound) t)));
    return set;
  }
}
