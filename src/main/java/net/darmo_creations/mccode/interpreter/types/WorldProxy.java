package net.darmo_creations.mccode.interpreter.types;

import net.minecraft.world.World;

/**
 * A wrapper type for Minecraft’s {@link World} class.
 */
public class WorldProxy {
  private final World world;
  private int worldTick;

  /**
   * Create a world wrapper.
   *
   * @param world     The world object to wrap.
   * @param worldTick The world’s current tick.
   */
  public WorldProxy(World world, int worldTick) {
    this.world = world;
    this.worldTick = worldTick;
  }

  /**
   * Return the wrapped world object.
   */
  public World getWorld() {
    return this.world;
  }

  /**
   * Return the wrapped world’s current tick.
   */
  public int getWorldTick() {
    return this.worldTick;
  }

  /**
   * Set wrapped world’s current tick.
   *
   * @param worldTick Current world tick.
   */
  public void setWorldTick(int worldTick) {
    this.worldTick = worldTick;
  }
}
