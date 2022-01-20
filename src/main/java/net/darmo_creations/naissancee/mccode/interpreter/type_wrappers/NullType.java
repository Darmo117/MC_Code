package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;

@WrapperType(name = NullType.NAME, trueType = Void.class)
public class NullType extends Type<Void> {
  public static final String NAME = "null";

  @Override
  protected boolean _toBoolean(final Void o) {
    return false;
  }
}
