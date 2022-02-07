package net.darmo_creations.mccode.interpreter.types;

import net.minecraft.world.World;

/**
 * A wrapper type for Minecraft’s {@link World} class.
 */
public class WorldProxy {
  private final World world;

  /**
   * Create a world wrapper.
   *
   * @param world The world object to wrap.
   */
  public WorldProxy(World world) {
    this.world = world;
  }

  /**
   * Return the wrapped world object.
   */
  public World getWorld() {
    return this.world;
  }
}
