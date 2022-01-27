package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.IndexOutOfBoundsException;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Wrapper type for {@link MCList} class.
 * <p>
 * Lists are iterable and support the __get_item__, __set_item__ and __del_item__ operators.
 */
public class ListType extends Type<MCList> {
  public static final String NAME = "list";

  private static final String VALUES_KEY = "Values";

  /**
   * Return a comparator to sort instances of this type.
   *
   * @param scope    A scope object to query types from.
   * @param reversed Whether to reverse sort the target list.
   * @return The comparator.
   */
  public static Comparator<Object> comparator(final Scope scope, final boolean reversed) {
    return (e1, e2) -> {
      Type<?> e1Type = ProgramManager.getTypeForValue(e1);
      Object gt = e1Type.applyOperator(scope, Operator.GT, e1, e2, null, false);
      if (ProgramManager.getTypeForValue(gt).toBoolean(gt)) {
        return reversed ? -1 : 1;
      }
      Object equal = e1Type.applyOperator(scope, Operator.EQUAL, e1, e2, null, false);
      return ProgramManager.getTypeForValue(equal).toBoolean(equal) ? 0 : (reversed ? 1 : -1);
    };
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<MCList> getWrappedType() {
    return MCList.class;
  }

  @Property(name = "clear")
  @Doc("Removes all values from this list. Modifies this list.")
  public Void clear(final MCList self) {
    self.clear();
    return null;
  }

  @Method(name = "add")
  @Doc("Adds a value at the end of this list. Modifies this list.")
  public Void add(final Scope scope, final MCList self, final Object value) {
    self.add(ProgramManager.getTypeForValue(value).copy(scope, value));
    return null;
  }

  @Method(name = "insert")
  @Doc("Adds a value at the specified index of this list. Modifies this list.")
  public Void insert(final Scope scope, final MCList self, final Integer index, final Object value) {
    if (index < 0 || index > self.size()) {
      throw new IndexOutOfBoundsException(scope, index);
    }
    self.add(index, ProgramManager.getTypeForValue(value).copy(scope, value));
    return null;
  }

  @Method(name = "count")
  @Doc("Counts the number of times the given value occurs in this list.")
  public Integer count(final Scope scope, final MCList self, final Object value) {
    return Math.toIntExact(self.stream().filter(e -> e.equals(value)).count());
  }

  @Method(name = "index")
  @Doc("Returns the index of the first occurence of the given value in this list." +
      " Returns -1 if the value is not present in this list.")
  public Integer indexOf(final Scope scope, final MCList self, final Object value) {
    return self.indexOf(value);
  }

  @Method(name = "sort")
  @Doc("Sorts this list using natural ordering of its elements. Modifies this list.")
  public Void sort(final Scope scope, final MCList self) {
    self.sort(comparator(scope, false));
    return null;
  }

  @Override
  protected Object __get_item__(final Scope scope, final MCList self, final Object key) {
    if (!(key instanceof Integer)) {
      throw new CastException(scope, ProgramManager.getTypeInstance(IntType.class),
          ProgramManager.getTypeForValue(key));
    }
    int index = (Integer) key;
    if (index < 0 || index > self.size()) {
      throw new IndexOutOfBoundsException(scope, index);
    }
    return self.get(index);
  }

  @Override
  protected void __set_item__(final Scope scope, MCList self, final Object key, final Object value) {
    if (!(key instanceof Integer)) {
      throw new CastException(scope, ProgramManager.getTypeInstance(IntType.class),
          ProgramManager.getTypeForValue(key));
    }
    int index = (Integer) key;
    if (index < 0 || index > self.size()) {
      throw new IndexOutOfBoundsException(scope, index);
    }
    // Deep copy value
    self.set(index, ProgramManager.getTypeForValue(value).copy(scope, value));
  }

  @Override
  protected void __del_item__(final Scope scope, MCList self, final Object key) {
    if (!(key instanceof Integer)) {
      throw new CastException(scope, ProgramManager.getTypeInstance(IntType.class),
          ProgramManager.getTypeForValue(key));
    }
    int index = (Integer) key;
    if (index < 0 || index > self.size()) {
      throw new IndexOutOfBoundsException(scope, index);
    }
    self.remove(index);
  }

  /**
   * Add all values of o to self.
   */
  @Override
  protected Object __add__(final Scope scope, MCList self, final Object o, final boolean inPlace) {
    MCList other = this.implicitCast(scope, o);
    if (inPlace) {
      return this.add(scope, self, other);
    }
    return this.add(scope, new MCList(self), other);
  }

  private Object add(final Scope scope, MCList list1, final MCList list2) {
    // Deep copy all elements to add
    list1.addAll(this.__copy__(scope, list2));
    return list1;
  }

  /**
   * Remove all values of o from self.
   */
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

  /**
   * Duplicates this list o times.
   */
  @Override
  protected Object __mul__(final Scope scope, MCList self, final Object o, final boolean inPlace) {
    int nb = ProgramManager.getTypeInstance(IntType.class).implicitCast(scope, o);
    if (inPlace) {
      return this.mul(scope, self, nb);
    }
    return this.mul(scope, new MCList(self), nb);
  }

  private Object mul(final Scope scope, MCList list, final int nb) {
    if (nb <= 0) {
      list.clear();
      return list;
    }
    for (int i = 0; i < nb - 1; i++) {
      // Deep copy all elements to add
      list.addAll(this.__copy__(scope, list));
    }
    return list;
  }

  @Override
  protected Object __eq__(final Scope scope, final MCList self, final Object o) {
    if (!(o instanceof MCList)) {
      return false;
    }
    return self.equals(o);
  }

  @Override
  protected Object __gt__(final Scope scope, final MCList self, final Object o) {
    if (o instanceof MCList) {
      MCList other = (MCList) o;
      Stream<?> s = IntStream.range(0, self.size())
          .mapToObj(i -> ProgramManager.getTypeForValue(self.get(i)).applyOperator(scope, Operator.GT, self.get(i), other.get(i), null, false));
      return self.size() > other.size() || self.size() == other.size() && s.allMatch(e -> ProgramManager.getTypeForValue(e).toBoolean(e));
    }
    return super.__gt__(scope, self, o);
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
  protected MCList __copy__(final Scope scope, final MCList self) {
    return new MCList(self.stream().map(e -> ProgramManager.getTypeForValue(e).copy(scope, e)).collect(Collectors.toList()));
  }

  @Override
  protected int __len__(final Scope scope, final MCList self) {
    return self.size();
  }

  @Override
  public MCList explicitCast(final Scope scope, final Object o) {
    if (o instanceof Collection) {
      return this.__copy__(scope, new MCList((Collection<?>) o));
    } else if (o instanceof Iterable) {
      MCList list = new MCList();
      for (Object e : (Iterable<?>) o) {
        list.add(ProgramManager.getTypeForValue(e).copy(scope, e));
      }
      return list;
    } else if (o instanceof Map) {
      return this.__copy__(scope, new MCList(((Map<?, ?>) o).keySet()));
    } else if (o instanceof String) {
      MCList list = new MCList();
      String s = (String) o;
      for (int i = 0; i < s.length(); i++) {
        list.add(String.valueOf(s.charAt(i)));
      }
      return list;
    }
    return super.explicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final MCList self) {
    NBTTagCompound tag = super._writeToNBT(self);
    NBTTagList list = new NBTTagList();
    self.forEach(v -> list.appendTag(ProgramManager.getTypeForValue(v).writeToNBT(v)));
    tag.setTag(VALUES_KEY, list);
    return tag;
  }

  @Override
  public MCList readFromNBT(final Scope scope, final NBTTagCompound tag) {
    MCList list = new MCList();
    NBTTagList tagsList = tag.getTagList(VALUES_KEY, new NBTTagCompound().getId());
    tagsList.forEach(t -> list.add(ProgramManager.getTypeForName(((NBTTagCompound) t).getString(Type.NAME_KEY)).readFromNBT(scope, (NBTTagCompound) t)));
    return list;
  }
}
