package net.darmo_creations.mccode;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MCCode.MOD_ID, name = MCCode.MOD_NAME, version = MCCode.VERSION)
public class MCCode {
  public static final String MOD_ID = "mccode";
  public static final String MOD_NAME = "MC_Code";
  public static final String VERSION = "1.0";

  /**
   * This is the instance of your mod as created by Forge. It will never be null.
   */
  @Mod.Instance(MOD_ID)
  public static MCCode INSTANCE;

  /**
   * This is the first initialization event. Register tile entities here.
   * The registry events below will have fired prior to entry to this method.
   */
  @Mod.EventHandler
  public void preinit(FMLPreInitializationEvent event) {

  }

  /**
   * This is the second initialization event. Register custom recipes
   */
  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {

  }
}
