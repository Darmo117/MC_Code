package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Wrapper type for {@link Void} class (null value).
 * <p>
 * It does not have a cast operator.
 */
@Doc("Placeholder type for the null value.")
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
  protected Object __add__(Scope scope, Void self, Object o, boolean inPlace) {
    if (o instanceof String) {
      return "null" + o;
    }
    return super.__add__(scope, self, o, inPlace);
  }

  @Override
  protected boolean __bool__(final Void self) {
    return false;
  }

  @Override
  public Void implicitCast(final Scope scope, final Object o) throws CastException {
    if (o == null) {
      return null;
    }
    return super.implicitCast(scope, o);
  }

  @Override
  public Void readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return null;
  }
}
