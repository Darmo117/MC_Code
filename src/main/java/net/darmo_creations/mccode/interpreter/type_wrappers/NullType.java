package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.minecraft.nbt.NBTTagCompound;

public class NullType extends Type<Void> {
  public static final String NAME = "null";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Void> getWrappedType() {
    return Void.class;
  }

  @Override
  public boolean generateCastOperator() {
    return false;
  }

  @Override
  protected boolean __bool__(final Void self) {
    return false;
  }

  @Override
  public Void readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return null;
  }
}
