package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

public class FloatType extends Type<Double> {
  public static final String NAME = "float";

  private static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Double> getWrappedType() {
    return Double.class;
  }

  @Override
  protected boolean __bool__(final Double self) {
    return self != 0.0;
  }

  @Override
  public Double implicitCast(final Scope scope, final Object o) {
    if (o instanceof Number) {
      return ((Number) o).doubleValue();
    } else if (o instanceof Boolean) {
      return ((Boolean) o) ? 1.0 : 0.0;
    }
    return super.implicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final Double self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    tag.setDouble(VALUE_KEY, self);
    return tag;
  }

  @Override
  public Double readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getDouble(VALUE_KEY);
  }
}
