package net.darmo_creations.mccode;

import net.darmo_creations.mccode.commands.CommandProgram;
import net.darmo_creations.mccode.interpreter.ProgramErrorReport;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mod’s main class. MCCode adds the possibility to write scripts to load and interact with Minecraft worlds.
 */
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
   * Map associating worlds to their program managers.
   */
  public final Map<World, ProgramManager> PROGRAM_MANAGERS = new HashMap<>();

  /**
   * This is the first initialization event. Register tile entities here.
   * The registry events below will have fired prior to entry to this method.
   */
  @Mod.EventHandler
  public void preinit(FMLPreInitializationEvent event) {
    ProgramManager.declareDefaultBuiltinTypes();
    ProgramManager.declareDefaultBuiltinFunctions();
  }

  /**
   * This is the second initialization event. Register custom recipes
   */
  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    ProgramManager.initialize();
  }

  @Mod.EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandProgram());
  }

  @Mod.EventBusSubscriber
  static class EventsHandler {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
      if (!event.getWorld().isRemote) {
        INSTANCE.PROGRAM_MANAGERS.put(event.getWorld(), ProgramManager.attachToGlobalStorage(event.getWorld()));
      }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
      if (!event.getWorld().isRemote) {
        INSTANCE.PROGRAM_MANAGERS.remove(event.getWorld());
      }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
      if (!event.world.isRemote && event.phase == TickEvent.Phase.START) {
        // TODO log errors to chat/console
        for (ProgramManager programManager : INSTANCE.PROGRAM_MANAGERS.values()) {
          List<ProgramErrorReport> errorReports = programManager.executePrograms();
          // TEMP
          errorReports.forEach(e -> System.out.println(e.getTranslationKey() + " " + Arrays.toString(e.getArgs())));
        }
      }
    }
  }
}
