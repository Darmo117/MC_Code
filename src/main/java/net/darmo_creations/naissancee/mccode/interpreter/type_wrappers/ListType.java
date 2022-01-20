package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.Interpreter;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.darmo_creations.naissancee.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCList;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCMap;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCSet;

@WrapperType(name = ListType.NAME, trueType = MCList.class)
public class ListType extends Type<MCList> {
  public static final String NAME = "list";

  @Property(name = "length")
  public Integer getLength(final MCList o) {
    return o.size();
  }

  @Override
  public MCList getDefault() {
    return new MCList();
  }

  @Override
  protected Object _getItem(final Object index, final MCList o) {
    if (!(index instanceof Integer)) {
      throw new CastException(Interpreter.getTypeForClass(Integer.class), Interpreter.getTypeForValue(o));
    }
    return o.get((Integer) index);
  }

  @Override
  protected boolean _toBoolean(final MCList o) {
    return !o.isEmpty();
  }

  @Override
  public MCList cast(final Object o) {
    if (o instanceof MCList) {
      return (MCList) o;
    } else if (o instanceof MCSet) {
      return new MCList((MCSet) o);
    } else if (o instanceof MCMap) {
      return new MCList(((MCMap) o).keySet());
    }
    return super.cast(o);
  }
}
