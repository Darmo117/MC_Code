package net.darmo_creations.mccode.interpreter.type_wrappers;

import com.google.common.collect.ImmutableMap;
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
import net.minecraft.block.properties.IProperty;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.*;
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

  @Property(name = "seed")
  @Doc("The seed of this world.")
  public Long getSeed(final WorldProxy self) {
    return self.getWorld().getSeed();
  }

  @Property(name = "day")
  @Doc("The current day for this world.")
  public Long getWorldDay(final WorldProxy self) {
    return self.getWorld().getWorldTime() / 24000L % 2147483647L;
  }

  @Property(name = "day_time")
  @Doc("The current time of day for this world.")
  public Long getWorldDayTime(final WorldProxy self) {
    return self.getWorld().getWorldTime() % 24000L;
  }

  @Property(name = "game_time")
  @Doc("The current game time for this world.")
  public Long getWorldTick(final WorldProxy self) {
    return self.getWorld().getTotalWorldTime() % 2147483647L;
  }

  @Override
  protected Object __get_property__(final Scope scope, final WorldProxy self, final String propertyName) {
    GameRules gameRules = self.getWorld().getGameRules();

    for (String rule : gameRules.getRules()) {
      if (("gr_" + rule).equals(propertyName)) {
        if (gameRules.areSameType(rule, GameRules.ValueType.NUMERICAL_VALUE)) {
          return (long) gameRules.getInt(rule);
        } else if (gameRules.areSameType(rule, GameRules.ValueType.BOOLEAN_VALUE)) {
          return gameRules.getBoolean(rule);
        } else {
          return gameRules.getString(rule);
        }
      }
    }

    return super.__get_property__(scope, self, propertyName);
  }

  @Override
  protected void __set_property__(final Scope scope, WorldProxy self, final String propertyName, final Object value) {
    GameRules gameRules = self.getWorld().getGameRules();
    boolean found = false;

    for (String rule : gameRules.getRules()) {
      if (("gr_" + rule).equals(propertyName)) {
        gameRules.setOrCreateGameRule(rule, String.valueOf(value));
        found = true;
        break;
      }
    }

    if (!found) {
      super.__set_property__(scope, self, propertyName, value);
    }
  }

  /*
   * /advancement command
   */

  @Method(name = "grant_all_advancements")
  @Doc("Grants the given advancement to the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long grantAllAdvancements(final Scope scope, WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "advancement",
        "grant", targetSelector, "everything"
    ).orElse(-1L);
  }

  @Method(name = "grant_advancement")
  @Doc("Grants the given advancement to the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long grantAdvancement(final Scope scope, WorldProxy self, final String targetSelector, final String mode, final String advancement) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "advancement",
        "grant", targetSelector, mode, advancement
    ).orElse(-1L);
  }

  @Method(name = "revoke_all_advancements")
  @Doc("Grants the given advancement to the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long revokeAllAdvancements(final Scope scope, WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "advancement",
        "revoke", targetSelector, "everything"
    ).orElse(-1L);
  }

  @Method(name = "revoke_advancement")
  @Doc("Grants the given advancement to the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long revokeAdvancement(final Scope scope, WorldProxy self, final String targetSelector, final String mode, final String advancement) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "advancement",
        "revoke", targetSelector, mode, advancement
    ).orElse(-1L);
  }

  @Method(name = "has_advancement")
  @Doc("Returns whether the selected players have the given advancement.")
  public Boolean hasAdvancement(final Scope scope, WorldProxy self, final String targetSelector, final String advancement, final String criterion) {
    List<String> args = new ArrayList<>(Arrays.asList("test", targetSelector, advancement));
    if (criterion != null) {
      args.add(criterion);
    }
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "advancement",
        args.toArray(new String[0])
    ).orElse(-1L) > 0;
  }

  /*
   * /clear command
   */

  @Method(name = "clear_inventory")
  @Doc("Removes all items from the inventory of selected players. " +
      "Returns the number of affected items or -1 if the action failed.")
  public Long clearInventory(final Scope scope, WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ITEMS,
        "clear",
        targetSelector
    ).orElse(-1L);
  }

  @Method(name = "clear_item")
  @Doc("Removes matching items from the inventory of selected players. " +
      "Returns the number of affected items or -1 if the action failed.")
  public Long clearItem(final Scope scope, WorldProxy self, final String targetSelector,
                        final String item, final Long data, final Long maxCount, final MCMap dataTag) {
    List<String> args = new ArrayList<>(Arrays.asList(targetSelector, item));
    if (data != null) {
      args.add(data.toString());
      if (maxCount != null) {
        args.add(maxCount.toString());
        args.add(mapToJSON(dataTag));
      }
    }
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ITEMS,
        "clear",
        args.toArray(new String[0])
    ).orElse(-1L);
  }

  /*
   * /clone command
   */

  @Method(name = "clone")
  @Doc("Clones blocks from one region to another. " +
      "Returns the number of affected blocks or -1 if the action failed.")
  public Long clone(final Scope scope, WorldProxy self, final BlockPos pos1, final BlockPos pos2, final BlockPos destination,
                    final String maskMode, final String cloneMode) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_BLOCKS,
        "clone",
        "" + pos1.getX(), "" + pos1.getY(), "" + pos1.getZ(),
        "" + pos2.getX(), "" + pos2.getY(), "" + pos2.getZ(),
        "" + destination.getX(), "" + destination.getY(), "" + destination.getZ(),
        maskMode, cloneMode
    ).orElse(-1L);
  }

  @Method(name = "clone_filtered")
  @Doc("Clones blocks from one region to another with the \"filter\" mask. " +
      "Returns the number of affected blocks or -1 if the action failed.")
  public Long clone(final Scope scope, WorldProxy self, final BlockPos pos1, final BlockPos pos2, final BlockPos destination,
                    final Block blockToClone, final Object metaOrStateToClone, final String cloneMode) {
    //noinspection ConstantConditions
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_BLOCKS,
        "clone",
        "" + pos1.getX(), "" + pos1.getY(), "" + pos1.getZ(),
        "" + pos2.getX(), "" + pos2.getY(), "" + pos2.getZ(),
        "" + destination.getX(), "" + destination.getY(), "" + destination.getZ(),
        blockToClone.getRegistryName().toString(), metaOrStateToString(scope, metaOrStateToClone), cloneMode
    ).orElse(-1L);
  }

  /*
   * /defaultgamemode command
   */

  @Method(name = "set_default_gamemode")
  @Doc("Sets the default game mode for new players. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setDefaultGameMode(final Scope scope, WorldProxy self, final String gamemode) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "defaultgamemode",
        gamemode
    ).orElse(-1L) > 0;
  }

  /*
   * /difficulty command
   */

  @Method(name = "set_difficulty")
  @Doc("Sets the difficulty level. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setDifficulty(final Scope scope, WorldProxy self, final String difficulty) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "difficulty",
        difficulty
    ).orElse(-1L) > 0;
  }

  /*
   * /effect command
   */

  @Method(name = "clear_all_effects")
  @Doc("Removes all effects from the selected entities. " +
      "Returns the number of affected entities or -1 if the action failed.")
  public Long clearAllEffects(final Scope scope, WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "effect",
        targetSelector, "clear"
    ).orElse(-1L);
  }

  @Method(name = "give_effect")
  @Doc("Gives an effect to the selected entities. " +
      "Returns the number of affected entities or -1 if the action failed.")
  public Long giveEffect(final Scope scope, WorldProxy self, final String targetSelector, final String effect,
                         final Long seconds, final Long amplifier, final Boolean hideParticles) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "effect",
        targetSelector, effect, seconds.toString(), amplifier.toString(), hideParticles.toString()
    ).orElse(-1L);
  }

  /*
   * /enchant command
   */

  @Method(name = "enchant_selected_item")
  @Doc("Enchants the active item of all selected entities with the given enchantment. " +
      "Returns the number of affected items or -1 if the action failed.")
  public Long enchantSelectedItem(final Scope scope, WorldProxy self, final String targetSelector, final String enchantment, final Long level) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ITEMS,
        "enchant",
        targetSelector, enchantment, level.toString()
    ).orElse(-1L);
  }

  /*
   * /experience /xp commands
   */

  @Method(name = "give_xp")
  @Doc("Gives the indicated amount of XP points or levels to the selected players. " +
      "Returns the number of XP levels/points before the action was executed or -1 if the action failed.")
  public Long giveXP(final Scope scope, WorldProxy self, final String targetSelector, final Long amount, final Boolean levels) {
    return executeCommand(
        self, CommandResultStats.Type.QUERY_RESULT,
        "xp",
        amount.toString() + (levels ? "L" : ""), targetSelector
    ).orElse(-1L);
  }

  @Method(name = "remove_xp_levels")
  @Doc("Removes the indicated amount of XP levels from the selected players. " +
      "Returns the number of XP levels before the action was executed or -1 if the action failed.")
  public Long removeXPLevels(final Scope scope, WorldProxy self, final String targetSelector, final Long amount) {
    return executeCommand(
        self, CommandResultStats.Type.QUERY_RESULT,
        "xp",
        "-" + amount.toString() + "L", targetSelector
    ).orElse(-1L);
  }

  /*
   * /function command
   */

  @Method(name = "run_mcfunction")
  @Doc("Runs the given mcfunction. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean runMCFunction(final Scope scope, WorldProxy self, final String function) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "function",
        function
    ).orElse(-1L) > 0;
  }

  @Method(name = "run_mcfunction_if_entities_match")
  @Doc("Runs the given mcfunction only if entities match the given selector. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean runMCFunctionIfEntitiesMatch(final Scope scope, WorldProxy self, final String function, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "function",
        function, "if", targetSelector
    ).orElse(-1L) > 0;
  }

  @Method(name = "run_mcfunction_unless_entities_match")
  @Doc("Runs the given mcfunction only if no entities match the given selector. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean runMCFunctionUnlessEntitiesMatch(final Scope scope, WorldProxy self, final String function, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "function",
        function, "unless", targetSelector
    ).orElse(-1L) > 0;
  }

  /*
   * /gamemode command
   */

  @Method(name = "set_gamemode")
  @Doc("Sets the game mode of the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long setGameMode(final Scope scope, WorldProxy self, final String targetSelector, final String gameMode) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "gamemode",
        gameMode, targetSelector
    ).orElse(-1L);
  }

  /*
   * /fill command
   */

  @Method(name = "fill")
  @Doc("Fills the region between the given positions in this world with the specified block and metadata. " +
      "Returns the number of affected blocks or -1 if the action failed.")
  public Long fill(final Scope scope, WorldProxy self, final BlockPos pos1, final BlockPos pos2, final Block block, final Object metaOrState,
                   final String mode, final MCMap dataTags) {
    //noinspection ConstantConditions
    List<String> args = new ArrayList<>(Arrays.asList(
        "" + pos1.getX(), "" + pos1.getY(), "" + pos1.getZ(),
        "" + pos2.getX(), "" + pos2.getY(), "" + pos2.getZ(),
        block.getRegistryName().toString(), metaOrStateToString(scope, metaOrState), mode
    ));
    if (!"replace".equals(mode)) {
      args.add(mapToJSON(dataTags));
    }
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_BLOCKS,
        "fill",
        args.toArray(new String[0])
    ).orElse(-1L);
  }

  @Method(name = "fill_replace")
  @Doc("Fills the region between the given positions in this world with the specified block and metadata. " +
      "Returns the number of affected blocks or -1 if the action failed.")
  public Long fill(final Scope scope, WorldProxy self, final BlockPos pos1, final BlockPos pos2, final Block block, final Object metaOrState,
                   final Block blockToReplace, final Object metaOrStateToReplace, final MCMap dataTags) {
    //noinspection ConstantConditions
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_BLOCKS,
        "fill",
        "" + pos1.getX(), "" + pos1.getY(), "" + pos1.getZ(),
        "" + pos2.getX(), "" + pos2.getY(), "" + pos2.getZ(),
        block.getRegistryName().toString(), metaOrStateToString(scope, metaOrState),
        "replace", blockToReplace.getRegistryName().toString(), metaOrStateToString(scope, metaOrStateToReplace),
        mapToJSON(dataTags)
    ).orElse(-1L);
  }

  /*
   * Get block info
   */

  @Method(name = "get_block")
  @Doc("Returns the block at the given position in this world.")
  public Block getBlock(final Scope scope, final WorldProxy self, final BlockPos pos) {
    return self.getWorld().getBlockState(pos).getBlock();
  }

  @Method(name = "get_block_state")
  @Doc("Returns the block state at the given position in this world.")
  public MCMap getBlockState(final Scope scope, final WorldProxy self, final BlockPos pos) {
    ImmutableMap<IProperty<?>, Comparable<?>> properties = self.getWorld().getBlockState(pos).getActualState(self.getWorld(), pos).getProperties();
    return new MCMap(properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getName(), e -> {
      Comparable<?> value = e.getValue();
      if (value instanceof Enum || value instanceof Character) {
        return value.toString();
      } else if (value instanceof Byte || value instanceof Short || value instanceof Integer) {
        return ((Number) value).longValue();
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

  /*
   * /kill command
   */

  @Method(name = "kill_entities")
  @Doc("Kills all selected entities. " +
      "Returns the number of affected entities or -1 if the action failed.")
  public Long killEntities(final Scope scope, final WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "kill",
        targetSelector
    ).orElse(-1L);
  }

  /*
   * /locate command
   */

  @Method(name = "locate_structure")
  @Doc("Returns the coordinates of the closest structure around the given point. " +
      "If the last parameter is true, unexplored structures will also be scanned. " +
      "Result may be null.")
  public BlockPos locateStructure(final Scope scope, final WorldProxy self, final String structureName,
                                  final BlockPos around, final Boolean findUnexplored) {
    return self.getWorld().findNearestStructure(structureName, around, findUnexplored);
  }

  /*
   * /msg /tell /w /tellraw commands
   */

  @Method(name = "send_message")
  @Doc("Sends a private message to the selected players. The message can be either a string or a map." +
      "Returns the number of affected players or -1 if the action failed.")
  public Long sendMessage(final Scope scope, WorldProxy self, final String targetSelector, final Object message) {
    String msg;
    if (message instanceof String) {
      msg = (String) message;
    } else {
      msg = mapToJSON(ProgramManager.getTypeInstance(MapType.class).implicitCast(scope, message));
    }
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "msg",
        targetSelector, msg
    ).orElse(-1L);
  }

  /*
   * /particle command
   */

  // TODO check behavior
  @Method(name = "spawn_particles")
  @Doc("Spawns particles at the given position. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean spawnParticles(final Scope scope, WorldProxy self, final String name, final BlockPos pos,
                                final Double deltaX, final Double deltaY, final Double deltaZ,
                                final Double speed, final Long count,
                                final Boolean force, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "particle",
        name, "" + pos.getX(), "" + pos.getY(), "" + pos.getZ(),
        deltaX.toString(), deltaY.toString(), deltaZ.toString(),
        speed.toString(), count.toString(),
        force ? "force" : "normal", targetSelector
    ).orElse(-1L) > 0;
  }

  /*
   * /playsound command
   */

  @Method(name = "play_sound")
  @Doc("Plays the specified sound. Position may be null. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long playSound(final Scope scope, WorldProxy self, final String sound, final String source,
                        final String targetSelector, final Double posX, final Double posY, final Double posZ) {
    List<String> args = new ArrayList<>(Arrays.asList(sound, source, targetSelector));
    if (posX != null && posY != null && posZ != null) {
      args.add(posX.toString());
      args.add(posY.toString());
      args.add(posZ.toString());
    }
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "playsound",
        args.toArray(new String[0])
    ).orElse(-1L);
  }

  @Method(name = "play_sound_with_volume")
  @Doc("Plays the specified sound. Pitch and volume may be null. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long playSoundWithVolume(final Scope scope, WorldProxy self, final String sound, final String source,
                                  final String targetSelector, final Double posX, final Double posY, final Double posZ,
                                  final Double volume, final Double pitch, final Double minVolume) {
    List<String> args = new ArrayList<>(Arrays.asList(
        sound, source, targetSelector,
        posX.toString(), posY.toString(), posZ.toString(),
        volume.toString()
    ));
    if (pitch != null) {
      args.add(pitch.toString());
      if (minVolume != null) {
        args.add(minVolume.toString());
      }
    }
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "playsound",
        args.toArray(new String[0])
    ).orElse(-1L);
  }

  /*
   * /recipe command
   */

  @Method(name = "unlock_all_recipes")
  @Doc("Unlocks all recipes for the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long unlockAllRecipes(final Scope scope, WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "recipe",
        "give", targetSelector, "*"
    ).orElse(-1L);
  }

  @Method(name = "unlock_recipe")
  @Doc("Unlocks the given recipe for the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long unlockRecipe(final Scope scope, WorldProxy self, final String targetSelector, final String recipe) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "recipe",
        "give", targetSelector, recipe
    ).orElse(-1L);
  }

  @Method(name = "lock_all_recipes")
  @Doc("Locks all recipes for the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long lockAllRecipes(final Scope scope, WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "recipe",
        "take", targetSelector, "*"
    ).orElse(-1L);
  }

  @Method(name = "lock_recipe")
  @Doc("Locks the given recipe for the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long lockRecipe(final Scope scope, WorldProxy self, final String targetSelector, final String recipe) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "recipe",
        "take", targetSelector, recipe
    ).orElse(-1L);
  }

  /*
   * /setblock command
   */

  @Method(name = "set_block")
  @Doc("Sets the block at the given position in this world with the given block and metadata. " +
      "Returns the number of affected blocks or -1 if the action failed.")
  public Long setBlock(final Scope scope, WorldProxy self, final BlockPos pos, final Block block, final Object metaOrState,
                       final String mode, final MCMap dataTags) {
    //noinspection ConstantConditions
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_BLOCKS,
        "setblock",
        "" + pos.getX(), "" + pos.getY(), "" + pos.getZ(),
        block.getRegistryName().toString(), metaOrStateToString(scope, metaOrState),
        mode, mapToJSON(dataTags)
    ).orElse(-1L);
  }

  /*
   * /setworldspawn command
   */

  @Method(name = "set_world_spawn")
  @Doc("Sets the world’s spawn. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setWorldSpawn(final Scope scope, WorldProxy self, final BlockPos pos) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "setworldspawn",
        "" + pos.getX(), "" + pos.getY(), "" + pos.getZ()
    ).orElse(-1L) > 0;
  }

  /*
   * /spawnpoint command
   */

  @Method(name = "set_spawn")
  @Doc("Sets the spawn point of the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long setSpawn(final Scope scope, WorldProxy self, final String targetSelector, final BlockPos pos) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "spawnpoint",
        targetSelector, "" + pos.getX(), "" + pos.getY(), "" + pos.getZ()
    ).orElse(-1L);
  }

  /*
   * /spreadplayers command
   */

  @Method(name = "spread_players")
  @Doc("Teleports players to a random location in the given area. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long spreadPlayers(final Scope scope, WorldProxy self, final Double centerX, final Double centerZ,
                            final Double spreadDistance, final Double maxRange, final Boolean respectTeams,
                            final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "spreadplayers",
        centerX.toString(), centerZ.toString(), spreadDistance.toString(), maxRange.toString(),
        respectTeams.toString(), targetSelector
    ).orElse(-1L);
  }

  /*
   * /stopsound command
   */

  @Method(name = "stop_sound")
  @Doc("Stops the selected or all sounds. If source or sound are null, all sounds will be stopped." +
      "Returns the number of affected players or -1 if the action failed.")
  public Long spreadPlayers(final Scope scope, WorldProxy self, final String targetSelector, final String source, final String sound) {
    List<String> args = new ArrayList<>();
    args.add(targetSelector);
    if (source != null && sound != null) {
      args.add(source);
      args.add(sound);
    }
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "spreadplayers",
        args.toArray(args.toArray(new String[0]))
    ).orElse(-1L);
  }

  /*
   * /summon command
   */

  @Method(name = "summon")
  @Doc("Summons an entity. Returns true if the action was successful, false otherwise.")
  public Boolean summonEntity(final Scope scope, WorldProxy self, final String entityType,
                              final Double posX, final Double posY, final Double posZ, final MCMap nbtData) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "summon",
        entityType, posX.toString(), posY.toString(), posZ.toString(), mapToJSON(nbtData)
    ).orElse(-1L) > 0;
  }

  /*
   * /teleport /tp commands
   */

  @Method(name = "tp_entities_to_player")
  @Doc("Teleports the selected entities to the given player. " +
      "Returns the number of affected entities or -1 if the action failed")
  public Long teleportEntitiesToPlayer(final Scope scope, WorldProxy self, final String targetSelector, final String playerName) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "tp",
        targetSelector, playerName
    ).orElse(-1L);
  }

  @Method(name = "tp_entities_to_pos")
  @Doc("Teleports the selected entities to the provided position. " +
      "Returns the number of affected entities or -1 if the action failed")
  public Long teleportEntitiesToPos(final Scope scope, WorldProxy self, final String targetSelector, final BlockPos destination,
                                    final Double yawAngle, final Double pitchAngle) {
    List<String> args = new ArrayList<>(Arrays.asList(
        targetSelector,
        "" + destination.getX(), "" + destination.getY(), "" + destination.getZ())
    );
    if (yawAngle != null) {
      args.add(yawAngle.toString());
      if (pitchAngle != null) {
        args.add(pitchAngle.toString());
      }
    }
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "tp",
        args.toArray(args.toArray(new String[0]))
    ).orElse(-1L);
  }

  /*
   * /time command
   */

  @Method(name = "set_time")
  @Doc("Sets the time for all worlds. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setTime(final Scope scope, WorldProxy self, final Long value) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "time",
        "set", value.toString()
    ).orElse(-1L) > 0;
  }

  @Method(name = "add_time")
  @Doc("Adds the given amount to the time for all worlds. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean addTime(final Scope scope, WorldProxy self, final Long amount) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "time",
        "add", amount.toString()
    ).orElse(-1L) > 0;
  }

  /*
   * /title command
   */

  @Method(name = "clear_title")
  @Doc("Clears the screen title from the screens of the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long clearTitle(final Scope scope, WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "title",
        targetSelector, "clear"
    ).orElse(-1L);
  }

  @Method(name = "reset_title")
  @Doc("Resets the subtitle text for the selected players to blank text, " +
      "and the fade-in, stay and fade-out times to their default values. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long resetTitle(final Scope scope, WorldProxy self, final String targetSelector) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "title",
        targetSelector, "reset"
    ).orElse(-1L);
  }

  @Method(name = "display_title")
  @Doc("Displays a screen title to the selected players, or changes the current screen title to the specified text. " +
      "After fading out, resets the subtitle back to blank text, but does not reset fade-in, stay, and fade-out times. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long displayTitle(final Scope scope, WorldProxy self, final String targetSelector, final MCMap title) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "title",
        targetSelector, "title", mapToJSON(title)
    ).orElse(-1L);
  }

  @Method(name = "set_subtitle")
  @Doc("Sets the subtitle for the current title or the next one if none is currently displayed. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long setSubtitle(final Scope scope, WorldProxy self, final String targetSelector, final MCMap subtitle) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "title",
        targetSelector, "subtitle", mapToJSON(subtitle)
    ).orElse(-1L);
  }

  @Method(name = "display_action_bar_text")
  @Doc("Displays text in the action bar slot of the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long displayActionBarText(final Scope scope, WorldProxy self, final String targetSelector, final MCMap text) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "title",
        targetSelector, "actionbar", mapToJSON(text)
    ).orElse(-1L);
  }

  @Method(name = "set_title_times")
  @Doc("Sets the fade-in, stay, and fade-out times (measured in game ticks) " +
      "of all current and future screen titles for the selected players. " +
      "Returns the number of affected players or -1 if the action failed.")
  public Long setTitleTimes(final Scope scope, WorldProxy self, final String targetSelector,
                            final Long fadeIn, final Long stay, final Long fadeOut) {
    return executeCommand(
        self, CommandResultStats.Type.AFFECTED_ENTITIES,
        "title",
        targetSelector, "times", fadeIn.toString(), stay.toString(), fadeOut.toString()
    ).orElse(-1L);
  }

  /*
   * /weather command
   */

  @Method(name = "set_weather")
  @Doc("Sets the weather for this world. Returns true if the action was successful, false otherwise.")
  public Boolean setWeather(final Scope scope, WorldProxy self, final String weather, final Long duration) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "weather",
        weather, duration.toString()
    ).orElse(-1L) > 0;
  }

  /*
   * /worldborder command
   */

  @Method(name = "get_world_border_diameter")
  @Doc("Returns the world border’s diameter.")
  public Long getWorldBorderDiameter(final Scope scope, WorldProxy self) {
    return executeCommand(
        self, CommandResultStats.Type.QUERY_RESULT,
        "worldborder",
        "get"
    ).orElse(-1L);
  }

  @Method(name = "set_world_border_center")
  @Doc("Sets the center coordinate of the world border. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setWorldBorderCenter(final Scope scope, WorldProxy self, final Long centerX, final Long centerZ) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "worldborder",
        "center", centerX.toString(), centerZ.toString()
    ).orElse(-1L) > 0;
  }

  @Method(name = "set_world_border_diameter")
  @Doc("Sets the world border’s diameter in the specified number of seconds. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setWorldBorderDiameter(final Scope scope, WorldProxy self, final Long diameter, final Long time) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "worldborder",
        "set", diameter.toString(), time.toString()
    ).orElse(-1L) > 0;
  }

  @Method(name = "update_world_border_diameter")
  @Doc("Add the given distance to the world border’s diameter in the specified number of seconds. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean updateWorldBorderDiameter(final Scope scope, WorldProxy self, final Long amount, final Long time) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "worldborder",
        "add", amount.toString(), time.toString()
    ).orElse(-1L) > 0;
  }

  @Method(name = "set_world_border_damage")
  @Doc("Sets the amount of damage per block to deal to players outside the border. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setWorldBorderDamage(final Scope scope, WorldProxy self, final Double damagePerBlock) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "worldborder",
        "damage", "amount", damagePerBlock.toString()
    ).orElse(-1L) > 0;
  }

  @Method(name = "set_world_border_damage_buffer")
  @Doc("Sets distance outside the border over which the players will take damage. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setWorldBorderDamageBuffer(final Scope scope, WorldProxy self, final Long bufferDistance) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "worldborder",
        "damage", "buffer", bufferDistance.toString()
    ).orElse(-1L) > 0;
  }

  @Method(name = "set_world_border_warn_distance")
  @Doc("Sets the world border’s warning distance.")
  public Boolean setWorldBorderWarnDistance(final Scope scope, WorldProxy self, final Long distance) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "worldborder",
        "warning", "distance", distance.toString()
    ).orElse(-1L) > 0;
  }

  @Method(name = "set_world_border_warn_time")
  @Doc("Sets the world border’s warning time. " +
      "Returns true if the action was successful, false otherwise.")
  public Boolean setWorldBorderWarnTime(final Scope scope, WorldProxy self, final Long time) {
    return executeCommand(
        self, CommandResultStats.Type.SUCCESS_COUNT,
        "worldborder",
        "warning", "time", time.toString()
    ).orElse(-1L) > 0;
  }

  /*
   * Utility methods
   */

  private static Optional<Long> executeCommand(WorldProxy self, final CommandResultStats.Type returnStatType,
                                               final String commandName, final String... args) {
    MinecraftServer server = self.getWorld().getMinecraftServer();
    StatsCommandSender sender = new StatsCommandSender(server);
    String command = commandName + " " + String.join(" ", args);
    //noinspection ConstantConditions
    server.commandManager.executeCommand(sender, command);
    System.out.println(sender.getStats()); // DEBUG
    if (sender.getStats().get(CommandResultStats.Type.SUCCESS_COUNT) == 0) {
      return Optional.empty();
    }
    if (returnStatType != null) {
      return Optional.of((long) sender.getStats().getOrDefault(returnStatType, 0));
    }
    return Optional.of(1L);
  }

  /**
   * Alias for EnumMap of CommandResultStats.Type.
   */
  private static class CommandStats extends EnumMap<CommandResultStats.Type, Integer> {
    public CommandStats() {
      super(CommandResultStats.Type.class);
    }
  }

  /**
   * Custom command sender class that gathers command result statistics.
   */
  private static class StatsCommandSender implements ICommandSender {
    private final MinecraftServer server;
    private final CommandStats stats;

    public StatsCommandSender(MinecraftServer server) {
      this.server = server;
      this.stats = new CommandStats();
    }

    public CommandStats getStats() {
      return this.stats;
    }

    @Override
    public String getName() {
      return this.server.getName();
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName) {
      return this.server.canUseCommand(permLevel, commandName);
    }

    @Override
    public World getEntityWorld() {
      return this.server.getEntityWorld();
    }

    @Override
    public MinecraftServer getServer() {
      return this.server;
    }

    @Override
    public void sendMessage(ITextComponent component) {
      this.server.sendMessage(component);
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount) {
      this.stats.put(type, amount);
    }
  }

  private static String metaOrStateToString(final Scope scope, final Object metaOrState) {
    if (metaOrState instanceof Long) {
      return "" + metaOrState;
    }
    return mapToBlockState(ProgramManager.getTypeInstance(MapType.class).implicitCast(scope, metaOrState));
  }

  private static String mapToBlockState(final MCMap map) {
    return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(","));
  }

  private static String mapToJSON(final MCMap map) {
    if (map == null) {
      return "{}";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    int i = 0;
    for (Map.Entry<String, Object> e : map.entrySet()) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(Utils.escapeString(e.getKey())).append(':').append(serializeJSON(e.getValue()));
      i++;
    }
    sb.append("}");
    System.out.println(sb);
    return sb.toString();
  }

  private static String serializeJSON(final Object o) {
    if (o instanceof MCMap) {
      return mapToJSON((MCMap) o);
    } else if (o instanceof MCList) {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      MCList list = (MCList) o;
      for (int i = 0; i < list.size(); i++) {
        if (i > 0) {
          sb.append(',');
        }
        sb.append(serializeJSON(list.get(i)));
      }
      sb.append(']');
      return sb.toString();
    } else if (o instanceof String) {
      return Utils.escapeString((String) o);
    } else {
      return String.valueOf(o);
    }
  }

  @Override
  public WorldProxy readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return (WorldProxy) scope.getVariable(Program.WORLD_VAR_NAME, false);
  }
}
