package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCList;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCMap;
import net.darmo_creations.naissancee.mccode.interpreter.types.MCSet;

@WrapperType(name = SetType.NAME, trueType = MCSet.class)
public class SetType extends Type<MCSet> {
  public static final String NAME = "set";

  @Property(name = "length")
  public Integer getLength(final MCMap o) {
    return o.size();
  }

  @Override
  public MCSet getDefault() {
    return new MCSet();
  }

  @Override
  protected boolean _toBoolean(final MCSet o) {
    return !o.isEmpty();
  }

  @Override
  public MCSet cast(final Object o) {
    if (o instanceof MCSet) {
      return (MCSet) o;
    } else if (o instanceof MCList) {
      return new MCSet((MCList) o);
    } else if (o instanceof MCMap) {
      return new MCSet(((MCMap) o).keySet());
    }
    return super.cast(o);
  }
}
