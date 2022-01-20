package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.minecraft.entity.Entity;

@WrapperType(name = EntityType.NAME, trueType = Entity.class)
public class EntityType extends Type<Entity> {
  public static final String NAME = "entity";

  // TODO add properties
}
