package net.darmo_creations.mccode.interpreter.annotations;

import java.lang.annotation.*;

/**
 * Annotation to use on methods annotated with {@link Property} or {@link Method}, on classes extending
 * {@link net.darmo_creations.mccode.interpreter.type_wrappers.Type} or
 * {@link net.darmo_creations.mccode.interpreter.types.BuiltinFunction}
 * to attach documentation accessible from within the game.
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
