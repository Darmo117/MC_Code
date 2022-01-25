package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.minecraft.nbt.NBTTagCompound;

public class StringType extends Type<String> {
  public static final String NAME = "string";

  private static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<String> getWrappedType() {
    return String.class;
  }

  @Property(name = "length")
  public Integer getLength(final String self) {
    return self.length();
  }

  @Override
  protected Object __add__(final Scope scope, final String self, final Object o, final boolean inPlace) {
    return self + this.implicitCast(scope, o);
  }

  @Override
  protected Object __mul__(final Scope scope, final String self, final Object o, final boolean inPlace) {
    int nb = scope.getInterpreter().getTypeInstance(IntType.class).implicitCast(scope, o);
    if (nb <= 0) {
      return "";
    }
    StringBuilder s = new StringBuilder(self);
    for (int i = 0; i < nb - 1; i++) {
      s.append(self);
    }
    return s.toString();
  }

  @Override
  protected Object __in__(final Scope scope, final String self, final Object o) {
    return self.contains(this.implicitCast(scope, o));
  }

  @Override
  protected boolean __bool__(final String self) {
    return self.length() != 0;
  }

  @Override
  public String implicitCast(final Scope scope, final Object o) {
    return String.valueOf(o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final String self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    tag.setString(VALUE_KEY, self);
    return tag;
  }

  @Override
  public String readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return tag.getString(VALUE_KEY);
  }
}
