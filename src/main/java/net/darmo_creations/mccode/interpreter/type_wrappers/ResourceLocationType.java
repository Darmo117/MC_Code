package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Wrapper type for {@link ResourceLocation} class.
 * <p>
 * New instances can be created by casting {@link String}s or {@link Map}s.
 */
@Doc("Resource locations are objects that point to a resource in the game (block, item, etc.).")
public class ResourceLocationType extends Type<ResourceLocation> {
  public static final String NAME = "resource_location";

  public static final String VALUE_KEY = "Value";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<ResourceLocation> getWrappedType() {
    return ResourceLocation.class;
  }

  @Property(name = "namespace")
  @Doc("The namespace of a resource.")
  public String getNamespace(final ResourceLocation self) {
    return self.getResourceDomain();
  }

  @Property(name = "path")
  @Doc("The path of a resource.")
  public String getPath(final ResourceLocation self) {
    return self.getResourcePath();
  }

  @Override
  protected Object __eq__(final Scope scope, final ResourceLocation self, final Object o) {
    if (o instanceof ResourceLocation) {
      return self.equals(o);
    } else if (o instanceof String) {
      return self.equals(this.explicitCast(scope, o));
    }
    return false;
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
      StringType stringType = ProgramManager.getTypeInstance(StringType.class);
      Map<?, ?> m = (Map<?, ?>) o;
      if (m.size() != 2 || !m.containsKey("namespace") || !m.containsKey("path")) {
        throw new EvaluationException(scope, "mccode.interpreter.error.resource_location_map_format", m);
      }
      Object namespace = m.get("namespace");
      Object path = m.get("path");
      if (namespace == null) {
        return new ResourceLocation((String) path);
      } else if (!(namespace instanceof String)) {
        throw new CastException(scope, stringType, ProgramManager.getTypeForValue(namespace));
      } else if (!(path instanceof String)) {
        throw new CastException(scope, stringType, ProgramManager.getTypeForValue(path));
      }
      return new ResourceLocation((String) namespace, (String) path);
    }
    return super.explicitCast(scope, o);
  }

  @Override
  protected NBTTagCompound _writeToNBT(final ResourceLocation self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setString(VALUE_KEY, self.toString());
    return tag;
  }

  @Override
  public ResourceLocation readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return new ResourceLocation(tag.getString(VALUE_KEY));
  }
}
