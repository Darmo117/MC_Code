package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.Interpreter;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;

@WrapperType(name = BooleanType.NAME, trueType = Boolean.class)
public class BooleanType extends Type<Boolean> {
  public static final String NAME = "boolean";

  @Override
  protected boolean _toBoolean(final Boolean o) {
    return o;
  }

  @Override
  public Boolean cast(final Object o) {
    return Interpreter.getTypeForValue(o).toBoolean(o);
  }
}
