package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.annotations.Property;
import net.darmo_creations.mccode.interpreter.annotations.PropertySetter;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Dummy type used to test property setters.
 */
public class DummyType extends Type<DummyObject> {
  @Override
  public String getName() {
    return "dummy";
  }

  @Override
  public Class<DummyObject> getWrappedType() {
    return DummyObject.class;
  }

  @Property(name = "property")
  public MCList get(final DummyObject self) {
    return self.value;
  }

  @PropertySetter(forProperty = "property")
  public void set(DummyObject self, final MCList value) {
    self.value = value;
  }

  @Override
  public DummyObject readFromNBT(Scope scope, NBTTagCompound tag) {
    return new DummyObject();
  }
}
