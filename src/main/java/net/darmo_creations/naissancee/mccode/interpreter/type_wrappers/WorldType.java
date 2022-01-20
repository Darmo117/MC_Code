package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.minecraft.world.World;

@WrapperType(name = WorldType.NAME, trueType = World.class)
public class WorldType extends Type<World> {
  public static final String NAME = "world";

  // TODO add properties
}
