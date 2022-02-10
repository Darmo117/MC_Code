package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SetupProgramManager.class)
class ImportStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ImportStatement.ID_KEY, ImportStatement.ID);
    NBTTagList list = new NBTTagList();
    list.appendTag(new NBTTagString("a"));
    list.appendTag(new NBTTagString("b"));
    tag.setTag(ImportStatement.NAME_KEY, list);
    tag.setString(ImportStatement.ALIAS_KEY, "c");
    assertEquals(tag, new ImportStatement(Arrays.asList("a", "b"), "c", 0, 0).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ImportStatement.ID_KEY, ImportStatement.ID);
    NBTTagList list = new NBTTagList();
    list.appendTag(new NBTTagString("a"));
    list.appendTag(new NBTTagString("b"));
    tag.setTag(ImportStatement.NAME_KEY, list);
    tag.setString(ImportStatement.ALIAS_KEY, "c");
    assertEquals(new ImportStatement(Arrays.asList("a", "b"), "c", 0, 0), new ImportStatement(tag));
  }

  @Test
  void testEquals() {
    assertEquals(new ImportStatement(Collections.singletonList("a"), null, 0, 0), new ImportStatement(Collections.singletonList("a"), null, 0, 0));
    assertEquals(new ImportStatement(Collections.singletonList("a"), "b", 0, 0), new ImportStatement(Collections.singletonList("a"), "b", 0, 0));
  }
}
