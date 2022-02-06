package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.WorldProxy;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.command.ICommand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper type for {@link WorldProxy} class.
 * <p>
 * It does not have a cast operator.
 */
public class WorldType extends Type<WorldProxy> {
  public static final String NAME = "world";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<WorldProxy> getWrappedType() {
    return WorldProxy.class;
  }

  @Override
  public boolean generateCastOperator() {
    return false;
  }

  @Property(name = "tick")
  @Doc("The current tick for this world.")
  public Integer getWorldTick(final WorldProxy self) {
    return self.getWorldTick();
  }

  @Method(name = "fill")
  @Doc("Fills the region between the given positions in this world with the specified block and metadata.")
  public Boolean fill(final Scope scope, WorldProxy self, final BlockPos pos1, final BlockPos pos2, final Block block, final Object metaOrState,
                      final String mode, final MCMap dataTags) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      String meta = metaOrState instanceof Integer
          ? "" + metaOrState
          : mapToBlockState(ProgramManager.getTypeInstance(MapType.class).implicitCast(scope, metaOrState));
      String trueMode = getFillMode(mode);
      //noinspection ConstantConditions
      List<String> args = Arrays.asList(
          "" + pos1.getX(), "" + pos1.getY(), "" + pos1.getZ(),
          "" + pos2.getX(), "" + pos2.getY(), "" + pos2.getZ(),
          block.getRegistryName().toString(), meta,
          trueMode
      );
      if (!"replace".equals(trueMode)) {
        args.add(mapToDataTag(dataTags));
      }
      return executeCommand(server, "fill", args.toArray(new String[0]));
    }
    return false;
  }

  @Method(name = "fill_replace")
  @Doc("Fills the region between the given positions in this world with the specified block and metadata.")
  public Boolean fill(final Scope scope, WorldProxy self, final BlockPos pos1, final BlockPos pos2, final Block block, final Object metaOrState,
                      final Block blockToReplace, final Object metaOrStateToReplace, final MCMap dataTags) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      String meta1 = metaOrState instanceof Integer
          ? "" + metaOrState
          : mapToBlockState(ProgramManager.getTypeInstance(MapType.class).implicitCast(scope, metaOrState));
      String meta2 = metaOrStateToReplace instanceof Integer
          ? "" + metaOrStateToReplace
          : mapToBlockState(ProgramManager.getTypeInstance(MapType.class).implicitCast(scope, metaOrStateToReplace));
      //noinspection ConstantConditions
      String[] args = {
          "" + pos1.getX(), "" + pos1.getY(), "" + pos1.getZ(),
          "" + pos2.getX(), "" + pos2.getY(), "" + pos2.getZ(),
          block.getRegistryName().toString(), meta1,
          "replace", blockToReplace.getRegistryName().toString(), meta2,
          mapToDataTag(dataTags),
      };
      return executeCommand(server, "fill", args);
    }
    return false;
  }

  @Method(name = "get_block")
  @Doc("Returns the block at the given position in this world.")
  public Block getBlock(final Scope scope, final WorldProxy self, final BlockPos pos) {
    return self.getWorld().getBlockState(pos).getBlock();
  }

  @Method(name = "get_block_state")
  @Doc("Returns the block state at the given position in this world.")
  public MCMap getBlockState(final Scope scope, final WorldProxy self, final BlockPos pos) {
    return new MCMap(self.getWorld().getBlockState(pos).getProperties().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getName(), e -> {
      Comparable<?> value = e.getValue();
      if (value instanceof Enum || value instanceof Character) {
        return value.toString();
      } else if (value instanceof Byte || value instanceof Short || value instanceof Long) {
        return ((Number) value).intValue();
      } else if (value instanceof Float) {
        return ((Float) value).doubleValue();
      }
      return value;
    })));
  }

  @Method(name = "is_block_loaded")
  @Doc("Returns whether the block at the given position is currently loaded.")
  public Boolean isBlockLoaded(final Scope scope, final WorldProxy self, final BlockPos pos) {
    return self.getWorld().isBlockLoaded(pos);
  }

  @Method(name = "set_block")
  @Doc("Sets the block at the given position in this world with the given block and metadata.")
  public Boolean setBlock(final Scope scope, WorldProxy self, final BlockPos pos, final Block block, final Object metaOrState, final String mode, final MCMap dataTags) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      String meta = metaOrState instanceof Integer
          ? "" + metaOrState
          : mapToBlockState(ProgramManager.getTypeInstance(MapType.class).implicitCast(scope, metaOrState));
      //noinspection ConstantConditions
      String[] args = {
          "" + pos.getX(), "" + pos.getY(), "" + pos.getZ(),
          block.getRegistryName().toString(), meta,
          getSetBlockMode(mode), mapToDataTag(dataTags),
      };
      return executeCommand(server, "setblock", args);
    }
    return false;
  }

  private static boolean executeCommand(MinecraftServer server, String commandName, String... args) {
    ICommand command = server.commandManager.getCommands().get(commandName);
    // Disable command feedback, set permission level to 2, set position vector to (0, 0, 0)
    CommandSenderWrapper sender = CommandSenderWrapper.create(server)
        .computePositionVector()
        .withPermissionLevel(2)
        .withSendCommandFeedback(false);
    try {
      command.execute(server, sender, args);
    } catch (CommandException e) {
      Utils.consoleLogTranslated(server, e.getMessage(), e.getErrorObjects());
      return false;
    }
    return true;
  }

  private static String mapToBlockState(final MCMap map) {
    return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(","));
  }

  private static String getSetBlockMode(final String mode) {
    return mode == null ? "replace" : mode;
  }

  private static String getFillMode(final String mode) {
    return mode == null ? "replace" : mode;
  }

  private static String mapToDataTag(final MCMap map) {
    if (map == null) {
      return "{}";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (Map.Entry<String, Object> e : map.entrySet()) {
      sb.append(e.getKey()).append(':').append(serializeJSON(e.getValue()));
    }
    sb.append("}");
    return sb.toString();
  }

  private static String serializeJSON(final Object o) {
    if (o instanceof MCMap) {
      return mapToDataTag((MCMap) o);
    } else if (o instanceof MCList) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      for (Object o1 : ((MCList) o)) {
        sb.append(serializeJSON(o1));
      }
      sb.append(']');
      return sb.toString();
    } else if (o instanceof String) {
      return '"' + ((String) o).replaceAll("[\"\\\\]", "\\\\$1") + '"';
    } else {
      return String.valueOf(o);
    }
  }

  @Override
  public WorldProxy readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return (WorldProxy) scope.getVariable(Program.WORLD_VAR_NAME, false);
  }
}
