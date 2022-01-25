package net.darmo_creations.mccode.interpreter.annotations;

import net.darmo_creations.mccode.interpreter.type_wrappers.Type;

import java.lang.annotation.*;

/**
 * Methods annoted with this annotation, from classes extending {@link Type},
 * will be accessible property setters from instances of the type within programs.
 * <p>
 * Annotated methods must feature a two parameters:
 * the instance object to set the property on and the new property value.
 * They must also reference an existing property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface PropertySetter {
  /**
   * MCCode name of the property it is the setter for.
   */
  String forProperty();
}
