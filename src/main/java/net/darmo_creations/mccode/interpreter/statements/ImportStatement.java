package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;

public class ImportStatement extends Statement {
  public static final int ID = 0;

  private static final String NAME_KEY = "ModuleName";
  public static final String ALIAS_KEY = "Alias";

  private final List<String> moduleNamePath;
  private final String alias;

  public ImportStatement(final List<String> moduleNamePath, final String alias) {
    this.moduleNamePath = new ArrayList<>(moduleNamePath);
    this.alias = alias;
  }

  public ImportStatement(final NBTTagCompound tag) {
    NBTTagList list = tag.getTagList(NAME_KEY, new NBTTagCompound().getId());
    this.moduleNamePath = new ArrayList<>();
    for (NBTBase t : list) {
      this.moduleNamePath.add(((NBTTagString) t).getString());
    }
    this.alias = tag.getString(ALIAS_KEY);
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    // TODO
    throw new UnsupportedOperationException("import statements are not yet available");
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    NBTTagList list = new NBTTagList();
    this.moduleNamePath.forEach(name -> list.appendTag(new NBTTagString(name)));
    tag.setTag(NAME_KEY, list);
    tag.setString(ALIAS_KEY, this.alias);
    return tag;
  }

  @Override
  public String toString() {
    return null;
  }
}
