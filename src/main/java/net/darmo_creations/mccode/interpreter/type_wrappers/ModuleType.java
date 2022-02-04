package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

@Doc("Modules are programs that have been imported through the 'import' statement.")
public class ModuleType extends Type<Program> {
  public static final String NAME = "module";

  public static final String MODULE_KEY = "Module";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Class<Program> getWrappedType() {
    return Program.class;
  }

  @Override
  public boolean generateCastOperator() {
    return false;
  }

  @Override
  public List<String> getPropertiesNames(Object self) {
    List<String> propertiesNames = super.getPropertiesNames(self);
    propertiesNames.add(Program.NAME_SPECIAL_VARIABLE);
    return propertiesNames;
  }

  @Override
  protected Object __get_property__(final Scope scope, final Program self, final String propertyName) {
    if (self.getScope().isVariableDefined(propertyName)) {
      return self.getScope().getVariable(propertyName, true);
    } else {
      return super.__get_property__(scope, self, propertyName);
    }
  }

  @Override
  protected void __set_property__(final Scope scope, Program self, final String propertyName, final Object value) {
    if (propertyName.equals("__name__")) {
      throw new EvaluationException(scope, "mccode.interpreter.error.cannot_set_module_name");
    }
    if (self.getScope().isVariableDefined(propertyName)) {
      self.getScope().setVariable(propertyName, value, true);
    } else {
      super.__set_property__(scope, self, propertyName, value);
    }
  }

  @Override
  protected NBTTagCompound _writeToNBT(final Program self) {
    NBTTagCompound tag = super._writeToNBT(self);
    tag.setTag(MODULE_KEY, self.writeToNBT());
    return tag;
  }

  @Override
  public Program readFromNBT(final Scope scope, final NBTTagCompound tag) {
    return new Program(tag.getCompoundTag(MODULE_KEY), scope.getProgram().getProgramManager());
  }
}
