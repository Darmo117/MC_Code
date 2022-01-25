package net.darmo_creations.mccode.interpreter.types;

import net.minecraft.world.World;

public class WorldProxy {
  private final World world;
  private int worldTick;

  public WorldProxy(World world, int worldTick) {
    this.world = world;
    this.worldTick = worldTick;
  }

  public World getWorld() {
    return this.world;
  }

  public int getWorldTick() {
    return this.worldTick;
  }

  public void setWorldTick(int worldTick) {
    this.worldTick = worldTick;
  }
}
