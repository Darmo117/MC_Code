package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@WrapperType(name = ItemType.NAME, trueType = Item.class)
public class ItemType extends Type<Item> {
  public static final String NAME = "item";

  // TODO add properties

  @Property(name = "id")
  public ResourceLocation getID(final Item item) {
    return ForgeRegistries.ITEMS.getKey(item);
  }

  @Override
  public Item cast(Object o) {
    return super.cast(o);
  }
}
