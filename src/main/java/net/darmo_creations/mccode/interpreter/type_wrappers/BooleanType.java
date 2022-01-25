package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

public class BooleanType extends Type<Boolean> {
  public static final String NAME = "boolean";

  private static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Boolean> getWrappedType() {
    return Boolean.class;
  }

  @Override
  protected boolean __bool__(final Boolean self) {
    return self;
  }

  @Override
  public Boolean implicitCast(final Scope scope, final Object o) {
    return scope.getInterpreter().getTypeForValue(o).toBoolean(o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final Boolean self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    tag.setBoolean(VALUE_KEY, self);
    return tag;
  }

  @Override
  public Boolean readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getBoolean(VALUE_KEY);
  }
}
