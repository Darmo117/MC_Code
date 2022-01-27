package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.builtin_functions.*;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.type_wrappers.AnyType;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.darmo_creations.mccode.interpreter.types.BuiltinFunction;
import net.darmo_creations.mccode.interpreter.types.Function;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

/**
 * A scope is an object that represents the context of a program during execution.
 * It holds all declared variables and functions.
 */
public class Scope implements NBTDeserializable {
  /**
   * Name of the main scope.
   */
  public static final String MAIN_SCOPE_NAME = "$main";

  private static final String VARIABLES_KEY = "Variables";

  private final String name;
  private final Scope parentScope;
  private final Program program;
  private Map<String, Variable> variables = new HashMap<>();

  /**
   * Create a global scope for the given program.
   *
   * @param program The program this scope belongs to.
   */
  public Scope(Program program) {
    this.name = MAIN_SCOPE_NAME;
    this.parentScope = null;
    this.program = program;
    this.defineBuiltinConstants();
    this.defineBuiltinFunctions();
  }

  /**
   * Create a sub-scope of another scope.
   *
   * @param name        Sub-scope’s name.
   * @param parentScope Parent of this scope.
   */
  public Scope(final String name, Scope parentScope) {
    this.name = name;
    this.parentScope = parentScope;
    this.program = parentScope.program;
  }

  /**
   * Return this scope’s name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return this scope’s parent.
   */
  public Optional<Scope> getParentScope() {
    return Optional.ofNullable(this.parentScope);
  }

  /**
   * Return the program manager this scope’s program is loaded in.
   */
  public ProgramManager getProgramManager() {
    return this.program.getProgramManager();
  }

  /**
   * Return this scope’s program.
   */
  public Program getProgram() {
    return this.program;
  }

  /**
   * Return the value of each variable.
   */
  public Map<String, Object> getVariables() {
    return new HashMap<>(this.variables);
  }

  /**
   * Return whether a variable with the given name is declared in this scope.
   *
   * @param name Variable’s name.
   * @return True if a variable with this name exists, false otherwise.
   */
  public boolean isVariableDefined(final String name) {
    return this.variables.containsKey(name);
  }

  /**
   * Return the value of the given variable.
   *
   * @param name        Variable’s name.
   * @param fromCommand Whether this action is performed from a command.
   * @return The variable’s value.
   * @throws EvaluationException If the variable doesn’t exist or cannot be accessed from command but fromCommand is true.
   */
  public Object getVariable(final String name, final boolean fromCommand) throws EvaluationException {
    if (!this.variables.containsKey(name) || fromCommand && this.parentScope != null) {
      if (this.parentScope != null && !fromCommand) {
        return this.parentScope.getVariable(name, false);
      } else {
        throw new EvaluationException(this, "mccode.interpreter.error.undefined_variable", name);
      }
    }
    return this.variables.get(name).getValue();
  }

  /**
   * Sets the value of the given variable.
   *
   * @param name        Variable’s name.
   * @param value       Variable’s new value.
   * @param fromCommand Whether this action is performed from a command.
   * @throws EvaluationException If the variable doesn’t exist, is constant or cannot
   *                             be set from commands and fromCommand is true.
   */
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

  /**
   * Declare a variable.
   *
   * @param variable The variable.
   * @throws EvaluationException If a variable with the same name already exists.
   */
  public void declareVariable(Variable variable) throws EvaluationException {
    if (this.variables.containsKey(variable.getName())) {
      throw new EvaluationException(this, "mccode.interpreter.error.variable_already_declared", variable.getName());
    }
    this.variables.put(variable.getName(), variable);
  }

  /**
   * Delete the given variable.
   *
   * @param name        Variable’s name.
   * @param fromCommand Whether this action is performed from a command.
   * @throws EvaluationException If the variable doesn’t exist or is not deletable.
   */
  public void deleteVariable(final String name, final boolean fromCommand) throws EvaluationException {
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

  /**
   * Delete all declared variables of this scope.
   */
  public void reset() {
    this.variables.clear();
    this.defineBuiltinConstants();
    this.defineBuiltinFunctions();
  }

  /**
   * Return the stack trace of this scope.
   */
  public List<StackTraceElement> getStackTrace() {
    List<StackTraceElement> trace;
    if (this.parentScope != null) {
      trace = this.parentScope.getStackTrace();
    } else {
      trace = new ArrayList<>();
    }
    trace.add(0, new StackTraceElement(this.getName()));
    return trace;
  }

  /**
   * Declare builtin constants.
   */
  private void defineBuiltinConstants() {
    this.declareVariable(new Variable("INF", true, false, true, false, Double.POSITIVE_INFINITY));
    this.declareVariable(new Variable("PI", true, false, true, false, Math.PI));
    this.declareVariable(new Variable("E", true, false, true, false, Math.E));
  }

  /**
   * Declare builtin functions and cast operators of declared types.
   */
  private void defineBuiltinFunctions() {
    ProgramManager pm = this.getProgramManager();

    List<Function> functions = Arrays.asList(
        new FloorFunction(pm),
        new CeilFunction(pm),
        new LenFunction(pm),
        new HypotFunction(pm),
        new SqrtFunction(pm),
        new CbrtFunction(pm),
        new ExpFunction(pm),
        new CosFunction(pm),
        new SinFunction(pm),
        new TanFunction(pm),
        new AcosFunction(pm),
        new AsinFunction(pm),
        new AtanFunction(pm),
        new Atan2Function(pm),
        new LogFunction(pm),
        new Log10Function(pm),
        new AbsFunction(pm),
        new RoundFunction(pm),
        new ToDegreesFunction(pm),
        new ToRadiansFunction(pm),
        new RangeFunction(pm),
        new SortedFunction(pm),
        new ReversedFunction(pm),
        new MinFunction(pm),
        new MaxFunction(pm)
    );
    for (Type<?> type : pm.getTypes()) {
      if (type.generateCastOperator()) {
        functions.add(new BuiltinFunction("to_" + type.getName(), type, pm.getTypeInstance(AnyType.class)) {
          @Override
          public Object apply(final Scope scope) {
            return type.explicitCast(scope, this.getParameter(0));
          }
        });
      }
    }

    for (Function function : functions) {
      // TODO generate doc
      this.declareVariable(new Variable(function.getName(), true, false, true, false, function));
    }
  }

  @Override
  public NBTTagCompound writeToNBT() {
    if (this.parentScope != null) {
      throw new MCCodeException("cannot save non-global scope");
    }
    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList variablesList = new NBTTagList();
    this.variables.values().stream()
        .filter(Variable::isDeletable)
        .forEach(v -> variablesList.appendTag(v.writeToNBT(this)));
    tag.setTag(VARIABLES_KEY, variablesList);
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
}
