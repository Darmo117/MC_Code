package net.darmo_creations.mccode.interpreter;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTDeserializable extends NBTSerializable {
  void readFromNBT(final NBTTagCompound tag);
}
