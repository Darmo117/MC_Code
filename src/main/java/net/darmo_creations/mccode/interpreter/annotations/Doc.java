package net.darmo_creations.mccode.interpreter.annotations;

import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;

import java.lang.annotation.*;

/**
 * Annotation to use on methods annotated with {@link Property}, {@link PropertySetter} or {@link Method},
 * on classes extending {@link Type} or {@link BuiltinFunction} to attach documentation accessible from within the game.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Doc {
  /**
   * Documentation string. Will be accessible from within game through commands.
   */
  String value();
}
