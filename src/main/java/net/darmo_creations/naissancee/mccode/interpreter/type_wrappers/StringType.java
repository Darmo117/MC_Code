package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;

@WrapperType(name = StringType.NAME, trueType = String.class)
public class StringType extends Type<String> {
  public static final String NAME = "string";

  @Property(name = "length")
  public Integer getLength(final String o) {
    return o.length();
  }

  @Override
  protected boolean _toBoolean(final String o) {
    return o.length() != 0;
  }

  @Override
  public String cast(final Object o) {
    return String.valueOf(o);
  }
}
