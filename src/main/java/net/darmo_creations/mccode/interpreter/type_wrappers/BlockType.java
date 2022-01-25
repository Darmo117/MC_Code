package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockType extends Type<Block> {
  public static final String NAME = "block";

  private static final String ID_KEY = "ID";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Block> getWrappedType() {
    return Block.class;
  }

  // TODO add properties and methods

  @Property(name = "id")
  public ResourceLocation getID(final Block self) {
    return ForgeRegistries.BLOCKS.getKey(self);
  }

  @Override
  public Block explicitCast(final Scope scope, final Object o) throws MCCodeRuntimeException {
    if (o instanceof String) {
      return ForgeRegistries.BLOCKS.getValue(new ResourceLocation((String) o));
    }
    return super.explicitCast(scope, o);
  }

  @Override
  public NBTTagCompound _writeToNBT(final Scope scope, final Block self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    tag.setString(ID_KEY, this.getID(self).toString());
    return tag;
  }

  @Override
  public Block readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString(ID_KEY)));
  }
}
