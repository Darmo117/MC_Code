package net.darmo_creations.mccode.interpreter;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTSerializable {
  NBTTagCompound writeToNBT();
}
