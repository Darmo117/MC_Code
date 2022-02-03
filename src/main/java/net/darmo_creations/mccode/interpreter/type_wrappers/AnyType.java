package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Wrapper type for {@link Object} class.
 * <p>
 * It does not have a cast operator.
 */
@Doc("Base type. Placeholder for functions parameters.")
public class AnyType extends Type<Object> {
  public static final String NAME = "any";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Object> getWrappedType() {
    return Object.class;
  }

  @Override
  public boolean generateCastOperator() {
    return false;
  }

  @Override
  public Object implicitCast(final Scope scope, final Object o) {
    return o;
  }

  @Override
  public NBTTagCompound _writeToNBT(final Object self) {
    throw new MCCodeException("cannot serialize objects of type any");
  }

  @Override
  public Object readFromNBT(final Scope scope, final NBTTagCompound tag) {
    throw new MCCodeException("cannot deserialize objects of type any");
  }
}
