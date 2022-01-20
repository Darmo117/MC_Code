package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@WrapperType(name = BlockType.NAME, trueType = Block.class)
public class BlockType extends Type<Block> {
  public static final String NAME = "block";

  // TODO add properties

  @Property(name = "id")
  public ResourceLocation getID(final Block block) {
    return ForgeRegistries.BLOCKS.getKey(block);
  }
}
