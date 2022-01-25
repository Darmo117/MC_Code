package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemType extends Type<Item> {
  public static final String NAME = "item";

  private static final String ID_KEY = "ID";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Item> getWrappedType() {
    return Item.class;
  }

  // TODO add properties

  @Property(name = "id")
  public ResourceLocation getID(final Item instance) {
    return ForgeRegistries.ITEMS.getKey(instance);
  }

  @Override
  public Item explicitCast(final Scope scope, final Object o) throws MCCodeRuntimeException {
    if (o instanceof String) {
      return ForgeRegistries.ITEMS.getValue(new ResourceLocation((String) o));
    } else if (o instanceof Block) {
      Item itemBlock = Item.getItemFromBlock((Block) o);
      if (itemBlock == Items.AIR) {
        return null;
      }
      return itemBlock;
    }
    return super.explicitCast(scope, o);
  }

  @Override
  public NBTTagCompound _writeToNBT(final Scope scope, final Item self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    tag.setString(ID_KEY, this.getID(self).toString());
    return tag;
  }

  @Override
  public Item readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString(ID_KEY)));
  }
}
