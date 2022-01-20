package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;

@WrapperType(name = IntType.NAME, trueType = Integer.class)
public class IntType extends Type<Integer> {
  public static final String NAME = "int";

  @Override
  protected boolean _toBoolean(final Integer o) {
    return o != 0;
  }

  @Override
  public Integer cast(final Object o) {
    if (o instanceof Integer) {
      return (Integer) o;
    } else if (o instanceof Double) {
      return ((Double) o).intValue();
    } else if (o instanceof Boolean) {
      return ((Boolean) o) ? 1 : 0;
    }
    return super.cast(o);
  }
}
