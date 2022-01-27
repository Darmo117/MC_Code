package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Wrapper type for {@link ResourceLocation} class.
 * <p>
 * New instances can be created by casting {@link String}s or {@link Map}s.
 */
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
  @Doc("Returns the namespace of this resource.")
  public String getNamespace(final ResourceLocation self) {
    return self.getNamespace();
  }

  @Property(name = "path")
  @Doc("Returns the path of this resource.")
  public String getPath(final ResourceLocation self) {
    return self.getPath();
  }

  @Override
  protected Object __eq__(final Scope scope, final ResourceLocation self, final Object o) {
    if (!(o instanceof ResourceLocation)) {
      return false;
    }
    return self.equals(o);
  }

  @Override
  protected Object __gt__(final Scope scope, final ResourceLocation self, final Object o) {
    if (o instanceof ResourceLocation) {
      return self.compareTo((ResourceLocation) o) > 0;
    }
    return super.__gt__(scope, self, o);
  }

  @Override
  public ResourceLocation explicitCast(Scope scope, final Object o) {
    if (o instanceof String) {
      return new ResourceLocation((String) o);
    } else if (o instanceof Map) {
      StringType stringType = scope.getProgramManager().getTypeInstance(StringType.class);
      Map<?, ?> m = (Map<?, ?>) o;
      Object namespace = m.get("namespace");
      Object path = m.get("path");
      if (!(path instanceof String)) {
        throw new CastException(scope, stringType, scope.getProgramManager().getTypeForValue(path));
      }
      if (namespace == null) {
        return new ResourceLocation((String) path);
      } else if (!(namespace instanceof String)) {
        throw new CastException(scope, stringType, scope.getProgramManager().getTypeForValue(path));
      }
      return new ResourceLocation((String) namespace, (String) path);
    }
    return super.explicitCast(scope, o);
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
