package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Method;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.darmo_creations.mccode.interpreter.types.WorldProxy;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

  @Method(name = "set_block_meta")
  public Boolean setBlock(final Scope scope, WorldProxy self, final BlockPos pos, final Block block, final int meta) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      //noinspection ConstantConditions
      String[] args = {"" + pos.getX(), "" + pos.getY(), "" + pos.getZ(), block.getRegistryName().toString(), "" + meta};
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
  public Boolean setBlock(final Scope scope, WorldProxy self, final BlockPos pos, final Block block, final MCMap state) {
    World world = self.getWorld();
    MinecraftServer server = world.getMinecraftServer();
    if (server != null) {
      //noinspection ConstantConditions
      String[] args = {"" + pos.getX(), "" + pos.getY(), "" + pos.getZ(), block.getRegistryName().toString(), "" + state};
      ICommand setblockCommand = server.commandManager.getCommands().get("setblock");
      try {
        setblockCommand.execute(server, server, args);
      } catch (CommandException e) {
        return false;
      }
    }
    return true;
  }

  // TODO add properties and methods

  @Override
  public WorldProxy readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return (WorldProxy) scope.getVariable(Program.WORLD_VAR_NAME, false);
  }
}
