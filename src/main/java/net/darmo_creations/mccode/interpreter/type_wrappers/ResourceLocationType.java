package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ResourceLocationType extends Type<ResourceLocation> {
  public static final String NAME = "resource_location";

  private static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<ResourceLocation> getWrappedType() {
    return ResourceLocation.class;
  }

  @Property(name = "namespace")
  public String getNamespace(final ResourceLocation self) {
    return self.getNamespace();
  }

  @Property(name = "path")
  public String getPath(final ResourceLocation self) {
    return self.getPath();
  }

  @Override
  public ResourceLocation implicitCast(Scope scope, final Object o) {
    if (o instanceof String) {
      return new ResourceLocation((String) o);
    } else if (o instanceof Map) {
      StringType stringType = scope.getInterpreter().getTypeInstance(StringType.class);
      Map<?, ?> m = (Map<?, ?>) o;
      return new ResourceLocation(stringType.implicitCast(scope, m.get("namespace")), stringType.implicitCast(scope, m.get("path")));
    }
    return super.implicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Scope scope, final ResourceLocation self) {
    NBTTagCompound tag = super._writeToNBT(scope, self);
    tag.setString(VALUE_KEY, self.toString());
    return tag;
  }

  @Override
  public ResourceLocation readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return new ResourceLocation(tag.getString(VALUE_KEY));
  }
}
