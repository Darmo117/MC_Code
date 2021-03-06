package net.darmo_creations.mccode.interpreter.type_wrappers;

import net.darmo_creations.mccode.interpreter.Program;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.annotations.Doc;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.stream.Collectors;

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
  protected List<String> getAdditionalPropertiesNames(final Program self) {
    return self.getScope().getVariables().values().stream()
        .filter(Variable::isPubliclyVisible)
        .map(Variable::getName)
        .collect(Collectors.toList());
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
      self.getScope().setVariable(propertyName, ProgramManager.getTypeForValue(value).copy(scope, value), true);
    } else {
      super.__set_property__(scope, self, propertyName, value);
    }
  }

  @Override
  protected Object __add__(final Scope scope, final Program self, final Object o, final boolean inPlace) {
    if (o instanceof String) {
      return this.__str__(self) + o;
    }
    return super.__add__(scope, self, o, inPlace);
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
