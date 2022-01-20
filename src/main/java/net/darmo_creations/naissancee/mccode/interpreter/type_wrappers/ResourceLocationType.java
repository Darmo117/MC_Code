package net.darmo_creations.naissancee.mccode.interpreter.type_wrappers;

import net.darmo_creations.naissancee.mccode.interpreter.annotations.Property;
import net.darmo_creations.naissancee.mccode.interpreter.annotations.WrapperType;
import net.minecraft.util.ResourceLocation;

@WrapperType(name = ResourceLocationType.NAME, trueType = ResourceLocation.class)
public class ResourceLocationType extends Type<ResourceLocation> {
  public static final String NAME = "resource_location";

  @Property(name = "namespace")
  public String getNamespace(final ResourceLocation o) {
    return o.getNamespace();
  }

  @Property(name = "path")
  public String getPath(final ResourceLocation o) {
    return o.getPath();
  }

  @Override
  public ResourceLocation cast(final Object o) {
    if (o instanceof String) {
      return new ResourceLocation((String) o);
    }
    return super.cast(o);
  }
}
