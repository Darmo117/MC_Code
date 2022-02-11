package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Wrapper type for {@link Boolean} class.
 */
@Doc("The type represents a truth value, either true or false.")
public class BooleanType extends Type<Boolean> {
  public static final String NAME = "boolean";

  public static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Boolean> getWrappedType() {
    return Boolean.class;
  }

  @Override
  protected Object __minus__(final Scope scope, final Boolean self) {
    return self ? -1L : 0L;
  }

  @Override
  protected Object __add__(final Scope scope, final Boolean self, final Object o, boolean inPlace) {
    if (o instanceof String) {
      return this.__str__(self) + o;
    }
    IntType intType = ProgramManager.getTypeInstance(IntType.class);
    return intType.__add__(scope, intType.implicitCast(scope, self), o, inPlace);
  }

  @Override
  protected Object __sub__(final Scope scope, final Boolean self, final Object o, boolean inPlace) {
    IntType intType = ProgramManager.getTypeInstance(IntType.class);
    return intType.__sub__(scope, intType.implicitCast(scope, self), o, inPlace);
  }

  @Override
  protected Object __mul__(final Scope scope, final Boolean self, final Object o, boolean inPlace) {
    IntType intType = ProgramManager.getTypeInstance(IntType.class);
    return intType.__mul__(scope, intType.implicitCast(scope, self), o, inPlace);
  }

  @Override
  protected Object __div__(final Scope scope, final Boolean self, final Object o, boolean inPlace) {
    IntType intType = ProgramManager.getTypeInstance(IntType.class);
    return intType.__div__(scope, intType.implicitCast(scope, self), o, inPlace);
  }

  @Override
  protected Object __mod__(final Scope scope, final Boolean self, final Object o, boolean inPlace) {
    IntType intType = ProgramManager.getTypeInstance(IntType.class);
    return intType.__mod__(scope, intType.implicitCast(scope, self), o, inPlace);
  }

  @Override
  protected Object __pow__(final Scope scope, final Boolean self, final Object o, boolean inPlace) {
    IntType intType = ProgramManager.getTypeInstance(IntType.class);
    return intType.__pow__(scope, intType.implicitCast(scope, self), o, inPlace);
  }

  @Override
  protected Object __eq__(final Scope scope, final Boolean self, final Object o) {
    FloatType floatType = ProgramManager.getTypeInstance(FloatType.class);
    double d = floatType.implicitCast(scope, self);
    return floatType.__eq__(scope, d, o);
  }

  @Override
  protected Object __gt__(final Scope scope, final Boolean self, final Object o) {
    FloatType floatType = ProgramManager.getTypeInstance(FloatType.class);
    double d = floatType.implicitCast(scope, self);
    return floatType.__gt__(scope, d, o);
  }

  @Override
  protected boolean __bool__(final Boolean self) {
    return self;
  }

  @Override
  public Boolean implicitCast(final Scope scope, final Object o) {
    return ProgramManager.getTypeForValue(o).toBoolean(o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Boolean self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setBoolean(VALUE_KEY, self);
    return tag;
  }

  @Override
  public Boolean readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getBoolean(VALUE_KEY);
  }
}
