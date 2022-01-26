package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.builtin_functions.CeilFunction;
import net.darmo_creations.mccode.interpreter.builtin_functions.FloorFunction;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.types.Function;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Map;

public class Scope implements NBTDeserializable {
  public static final String MAIN_SCOPE_NAME = "$main";

  private static final String VARIABLES_KEY = "Variables";

  private final String name;
  private final Scope parentScope;
  private final Program program;
  private Map<String, Variable> variables = new HashMap<>();

  public Scope(Program program) {
    this.name = MAIN_SCOPE_NAME;
    this.parentScope = null;
    this.program = program;
    this.defineBuiltinConstants();
    this.defineBuiltinFunctions();
  }

  public Scope(final String name, Scope parentScope) {
    this.name = name;
    this.parentScope = parentScope;
    this.program = parentScope.program;
  }

  public String getName() {
    return this.name;
  }

  public Scope getParentScope() {
    return this.parentScope;
  }

  public ProgramManager getInterpreter() {
    return this.program.getInterpreter();
  }

  public Program getProgram() {
    return this.program;
  }

  public Map<String, Object> getVariables() {
    return new HashMap<>(this.variables);
  }

  public void reset() {
    this.variables.clear();
    this.defineBuiltinConstants();
    this.defineBuiltinFunctions();
  }

  public boolean isVariableDefined(final String name) {
    return this.variables.containsKey(name);
  }

  public Object getVariable(final String name, boolean fromCommand) throws EvaluationException {
    if (!this.variables.containsKey(name) || fromCommand && this.parentScope != null) {
      if (this.parentScope != null && !fromCommand) {
        return this.parentScope.getVariable(name, false);
      } else {
        throw new EvaluationException(this, "mccode.interpreter.error.undefined_variable", name);
      }
    }
    return this.variables.get(name).getValue();
  }

  public void setVariable(final String name, Object value, final boolean fromCommand) throws EvaluationException {
    if (!this.variables.containsKey(name) || fromCommand && this.parentScope != null) {
      if (this.parentScope != null && !fromCommand) {
        this.parentScope.setVariable(name, value, false);
      } else {
        throw new EvaluationException(this, "mccode.interpreter.error.undefined_variable", name);
      }
    }
    this.variables.get(name).setValue(this, value, fromCommand);
  }

  public void declareVariable(Variable variable) throws EvaluationException {
    if (this.variables.containsKey(variable.getName())) {
      throw new EvaluationException(this, "mccode.interpreter.error.variable_already_declared", variable.getName());
    }
    this.variables.put(variable.getName(), variable);
  }

  public void deleteVariable(final String name, boolean fromCommand) throws EvaluationException {
    if (!this.variables.containsKey(name) || fromCommand && this.parentScope != null) {
      if (this.parentScope != null && !fromCommand) {
        this.parentScope.deleteVariable(name, false);
      } else {
        throw new EvaluationException(this, "mccode.interpreter.error.undefined_variable", name);
      }
    }
    if (!this.variables.get(name).isDeletable()) {
      throw new EvaluationException(this, "mccode.interpreter.error.cannot_delete_variable", name);
    }
    this.variables.remove(name);
  }

  private void defineBuiltinConstants() {
    this.declareVariable(new Variable("PI", true, false, true, false, Math.PI));
    this.declareVariable(new Variable("E", true, false, true, false, Math.E));
  }

  private void defineBuiltinFunctions() {
    Function[] functions = {
        new FloorFunction(this.getInterpreter()),
        new CeilFunction(this.getInterpreter()),
    };
    for (Function function : functions) {
      // TODO generate doc
      this.declareVariable(new Variable(function.getName(), true, false, true, false, function));
    }
  }

  // FIXME what about same object in different variables/collections?
  // TODO -> associate unique ID with each instance?
  @Override
  public NBTTagCompound writeToNBT() {
    if (this.parentScope != null) {
      throw new MCCodeException("cannot save non-global scope");
    }
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList statementsList = new NBTTagList();
    this.variables.values().stream()
        .filter(v -> !v.isDeletable())
        .forEach(v -> statementsList.appendTag(v.writeToNBT(this)));
    tag.setTag(VARIABLES_KEY, statementsList);
    return tag;
  }

  @Override
  public void readFromNBT(final NBTTagCompound tag) {
    if (this.parentScope != null) {
      throw new MCCodeException("cannot load non-global scope");
    }
    NBTTagList list = tag.getTagList(VARIABLES_KEY, new NBTTagCompound().getId());
    this.variables = new HashMap<>();
    for (NBTBase t : list) {
      Variable variable = new Variable((NBTTagCompound) t, this);
      this.variables.put(variable.getName(), variable);
    }
  }

  public StackTraceElement[] getStackTrace() {
    return null; // TODO
  }
}
