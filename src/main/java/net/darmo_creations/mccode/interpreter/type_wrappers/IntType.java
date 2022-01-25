package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

public class IntType extends Type<Integer> {
  public static final String NAME = "int";

  private static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Integer> getWrappedType() {
    return Integer.class;
  }

  @Override
  protected boolean __bool__(final Integer self) {
    return self != 0;
  }

  @Override
  public Integer implicitCast(final Scope scope, final Object o) {
    if (o instanceof Number) {
      return ((Number) o).intValue();
    } else if (o instanceof Boolean) {
      return ((Boolean) o) ? 1 : 0;
    }
    return super.implicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final Integer self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    tag.setInteger(VALUE_KEY, self);
    return tag;
  }

  @Override
  public Integer readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getInteger(VALUE_KEY);
  }
}
