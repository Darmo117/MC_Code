package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Wrapper type for {@link Item} class.
 * <p>
 * Implements __eq__ operator. Can explicitly cast {@link String}s, {@link ResourceLocation}s and {@link Block}s.
 */
@Doc("Type that represents an item.")
public class ItemType extends Type<Item> {
  public static final String NAME = "item";

  public static final String ID_KEY = "ID";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Item> getWrappedType() {
    return Item.class;
  }

  @Property(name = "id")
  @Doc("The ID of an item.")
  public ResourceLocation getID(final Item self) {
    return ForgeRegistries.ITEMS.getKey(self);
  }

  @Property(name = "max_stack_size")
  @Doc("The max stack size of an item.")
  public Long getMaxStackSize(final Item self) {
    //noinspection deprecation
    return (long) self.getItemStackLimit();
  }

  @Override
  protected Object __add__(final Scope scope, final Item self, final Object o, final boolean inPlace) {
    if (o instanceof String) {
      return this.__str__(self) + o;
    }
    return super.__add__(scope, self, o, inPlace);
  }

  @Override
  protected Object __eq__(final Scope scope, final Item self, final Object o) {
    if (!(o instanceof Item)) {
      return false;
    }
    return this.getID(self).equals(this.getID((Item) o));
  }

  @Override
  protected String __str__(final Item self) {
    //noinspection ConstantConditions
    return self.getRegistryName().toString();
  }

  @Override
  public Item explicitCast(final Scope scope, final Object o) throws MCCodeRuntimeException {
    if (o instanceof String) {
      return ForgeRegistries.ITEMS.getValue(new ResourceLocation((String) o));
    } else if (o instanceof ResourceLocation) {
      return ForgeRegistries.ITEMS.getValue((ResourceLocation) o);
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
  public NBTTagCompound _writeToNBT(final Item self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setString(ID_KEY, this.getID(self).toString());
    return tag;
  }

  @Override
  public Item readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString(ID_KEY)));
  }
}
