package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.WorldProxy;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
  public Integer getWorldTick(final Scope scope, final WorldProxy self) {
    return self.getWorldTick();
  }

  // TODO add properties and methods

  @Method(name = "set_block_meta")
  @Doc("Sets the block at the given position in this world with the given block and metadata.")
  public Boolean setBlock(final Scope scope, WorldProxy self, final BlockPos pos, final Block block, final int meta, final String mode, final MCMap dataTags) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      //noinspection ConstantConditions
      String[] args = {
          "" + pos.getX(), "" + pos.getY(), "" + pos.getZ(),
          block.getRegistryName().toString(), "" + meta,
          getSetBlockMode(mode), mapToDataTag(dataTags),
      };
      ICommand setblockCommand = server.commandManager.getCommands().get("setblock");
      try {
        setblockCommand.execute(server, server, args);
      } catch (CommandException e) {
        return false;
      }
    }
    return true;
  }

  @Method(name = "set_block_state")
  @Doc("Sets the block at the given position in this world with the given block and block state.")
  public Boolean setBlock(final Scope scope, WorldProxy self, final BlockPos pos, final Block block, final MCMap state, final String mode, final MCMap dataTags) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      //noinspection ConstantConditions
      String[] args = {
          "" + pos.getX(), "" + pos.getY(), "" + pos.getZ(),
          block.getRegistryName().toString(), mapToBlockState(state),
          getSetBlockMode(mode), mapToDataTag(dataTags),
      };
      ICommand setblockCommand = server.commandManager.getCommands().get("setblock");
      try {
        setblockCommand.execute(server, server, args);
      } catch (CommandException e) {
        return false;
      }
    }
    return true;
  }

  @Method(name = "fill_meta")
  @Doc("Fills the region between the given positions in this world with the specified block and metadata.")
  public Boolean fill(final Scope scope, WorldProxy self, final BlockPos pos1, final BlockPos pos2, final Block block, final int meta, final String mode, final MCMap dataTags) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      //noinspection ConstantConditions
      String[] args = {
          "" + pos1.getX(), "" + pos1.getY(), "" + pos1.getZ(),
          "" + pos2.getX(), "" + pos2.getY(), "" + pos2.getZ(),
          block.getRegistryName().toString(), "" + meta,
          getFillMode(mode), mapToDataTag(dataTags),
      };
      ICommand setblockCommand = server.commandManager.getCommands().get("fill");
      try {
        setblockCommand.execute(server, server, args);
      } catch (CommandException e) {
        return false;
      }
    }
    return true;
  }

  @Method(name = "fill_block_state")
  @Doc("Fills the region between the given positions in this world with the specified block and block state.")
  public Boolean fill(final Scope scope, WorldProxy self, final BlockPos pos1, final BlockPos pos2, final Block block, final MCMap state, final String mode, final MCMap dataTags) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      //noinspection ConstantConditions
      String[] args = {
          "" + pos1.getX(), "" + pos1.getY(), "" + pos1.getZ(),
          "" + pos2.getX(), "" + pos2.getY(), "" + pos2.getZ(),
          block.getRegistryName().toString(), mapToBlockState(state),
          getFillMode(mode), mapToDataTag(dataTags),
      };
      ICommand setblockCommand = server.commandManager.getCommands().get("fill");
      try {
        setblockCommand.execute(server, server, args);
      } catch (CommandException e) {
        return false;
      }
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
