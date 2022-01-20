package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;

@WrapperType(name = FloatType.NAME, trueType = Double.class)
public class FloatType extends Type<Double> {
  public static final String NAME = "float";

  @Override
  protected boolean _toBoolean(final Double o) {
    return o != 0.0;
  }

  @Override
  public Double cast(final Object o) {
    if (o instanceof Double) {
      return (Double) o;
    } else if (o instanceof Integer) {
      return ((Integer) o).doubleValue();
    } else if (o instanceof Boolean) {
      return ((Boolean) o) ? 1.0 : 0.0;
    }
    return super.cast(o);
  }
}
