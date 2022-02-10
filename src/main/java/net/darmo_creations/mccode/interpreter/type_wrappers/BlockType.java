package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Wrapper type for {@link Block} class.
 * <p>
 * New instances can be created by casting {@link String}s or {@link ResourceLocation}s.
 */
@Doc("Type that represents a block.")
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

  @Property(name = "id")
  @Doc("The ID of a block.")
  public ResourceLocation getID(final Block self) {
    return ForgeRegistries.BLOCKS.getKey(self);
  }

  @Override
  protected Object __eq__(final Scope scope, final Block self, final Object o) {
    if (!(o instanceof Block)) {
      return false;
    }
    return this.getID(self).equals(this.getID((Block) o));
  }

  @Override
  public Block explicitCast(final Scope scope, final Object o) throws MCCodeRuntimeException {
    if (o instanceof String) {
      return ForgeRegistries.BLOCKS.getValue(new ResourceLocation((String) o));
    } else if (o instanceof ResourceLocation) {
      return ForgeRegistries.BLOCKS.getValue((ResourceLocation) o);
    }
    return super.explicitCast(scope, o);
  }

  @Override
  public NBTTagCompound _writeToNBT(final Block self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setString(ID_KEY, this.getID(self).toString());
    return tag;
  }

  @Override
  public Block readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString(ID_KEY)));
  }
}
