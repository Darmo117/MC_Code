package net.darmo_creations.naissancee.mccode.interpreter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WrapperType {
  String name();

  Class<?> trueType();
}
