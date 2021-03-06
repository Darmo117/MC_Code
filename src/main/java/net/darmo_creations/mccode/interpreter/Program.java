package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.darmo_creations.mccode.interpreter.statements.Statement;
import net.darmo_creations.mccode.interpreter.statements.StatementAction;
import net.darmo_creations.mccode.interpreter.statements.StatementNBTHelper;
import net.darmo_creations.mccode.interpreter.statements.WaitStatement;
import net.darmo_creations.mccode.interpreter.types.WorldProxy;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A program is composed of a list of statements that can be executed.
 * <p>
 * The program’s state can be saved and restored and world loading and unloading.
 */
public class Program {
  public static final String WORLD_VAR_NAME = "WORLD";
  public static final String NAME_SPECIAL_VARIABLE = "__name__";

  public static final String NAME_KEY = "Name";
  public static final String STATEMENTS_KEY = "Statements";
  public static final String SCOPE_KEY = "Scope";
  public static final String SCHEDULE_DELAY_KEY = "ScheduleDelay";
  public static final String REPEAT_AMOUNT_KEY = "RepeatAmount";
  public static final String WAIT_TIME_KEY = "WaitTime";
  public static final String IP_KEY = "IP";
  public static final String IS_MODULE_KEY = "IsModule";
  public static final String ARGS_KEY = "CommandArgs";
  public static final String ARG_TYPE_KEY = "Type";
  public static final String ARG_VALUE_KEY = "Value";

  private final String name;
  private final List<Statement> statements;
  private final ProgramManager programManager;
  private final boolean isModule;
  private final Scope scope;
  private final Long scheduleDelay;
  private final Long repeatAmount;
  private long timeToWait;
  /**
   * Instruction pointer.
   */
  private int ip;
  private final Random rng = new Random();

  private final List<Object> args;

  /**
   * Create a new program.
   *
   * @param name           Program’s name.
   * @param statements     Program’s statements.
   * @param scheduleDelay  Program’s schedule delay. May be null.
   * @param repeatAmount   Program’s repeat amount. May be null.
   * @param programManager Program’s manager.
   * @param args           Optional command arguments for the program.
   * @throws MCCodeRuntimeException If the schedule delay or repeat amount is negative,
   *                                or a repeat amount is defined without a schedule delay.
   */
  public Program(final String name, final List<Statement> statements, final Long scheduleDelay, final Long repeatAmount, ProgramManager programManager, Object... args) {
    this.programManager = Objects.requireNonNull(programManager);
    this.name = Objects.requireNonNull(name);
    this.statements = Objects.requireNonNull(statements);
    this.scope = new Scope(this);
    if (scheduleDelay != null && scheduleDelay < 0) {
      throw new MCCodeRuntimeException(this.scope, null, "mccode.interpreter.error.invalid_schedule_delay", scheduleDelay);
    }
    if (repeatAmount != null && repeatAmount <= 0) {
      throw new MCCodeRuntimeException(this.scope, null, "mccode.interpreter.error.invalid_schedule_repeat", scheduleDelay);
    }
    if (scheduleDelay == null && repeatAmount != null) {
      throw new MCCodeRuntimeException(this.scope, null, "mccode.interpreter.error.missing_schedule_delay");
    }
    this.scheduleDelay = scheduleDelay;
    this.repeatAmount = repeatAmount;
    this.timeToWait = 0;
    this.ip = 0;
    this.isModule = false;
    this.args = Arrays.asList(args);
    this.setup();
  }

  /**
   * Create a program as a sub-module.
   * Wait statements are not allowed in sub-modules.
   *
   * @param name           Module’s name.
   * @param statements     Module’s statements.
   * @param programManager Module’s manager.
   */
  public Program(final String name, final List<Statement> statements, ProgramManager programManager) {
    this.programManager = Objects.requireNonNull(programManager);
    this.name = Objects.requireNonNull(name);
    this.statements = Objects.requireNonNull(statements);
    this.scope = new Scope(this);
    this.scheduleDelay = null;
    this.repeatAmount = null;
    this.timeToWait = 0;
    this.ip = 0;
    this.isModule = true;
    this.args = Collections.emptyList();
    this.setup();
  }

  /**
   * Create a program from the given NBT tag.
   *
   * @param tag            Tag to deserialize.
   * @param programManager Program’s manager.
   */
  public Program(final NBTTagCompound tag, ProgramManager programManager) {
    this.programManager = Objects.requireNonNull(programManager);
    this.name = tag.getString(NAME_KEY);
    this.statements = StatementNBTHelper.deserializeStatementsList(tag, STATEMENTS_KEY);
    this.scope = new Scope(this);
    this.scope.readFromNBT(tag.getCompoundTag(SCOPE_KEY));
    this.isModule = tag.getBoolean(IS_MODULE_KEY);
    if (!this.isModule) {
      this.scheduleDelay = tag.getLong(SCHEDULE_DELAY_KEY);
      this.repeatAmount = tag.getLong(REPEAT_AMOUNT_KEY);
      this.timeToWait = tag.getLong(WAIT_TIME_KEY);
    } else {
      this.scheduleDelay = null;
      this.repeatAmount = null;
      this.timeToWait = 0;
    }
    this.ip = tag.getInteger(IP_KEY);
    this.args = new ArrayList<>();
    for (NBTBase nbtBase : tag.getTagList(ARGS_KEY, new NBTTagCompound().getId())) {
      NBTTagCompound t = (NBTTagCompound) nbtBase;
      String argType = t.getString(ARG_TYPE_KEY);
      switch (argType) {
        case "int":
          this.args.add(t.getLong(ARG_VALUE_KEY));
          break;
        case "float":
          this.args.add(t.getDouble(ARG_VALUE_KEY));
          break;
        case "boolean":
          this.args.add(t.getBoolean(ARG_VALUE_KEY));
          break;
        default:
          throw new MCCodeException("invalid arg type " + argType);
      }
    }
    this.setup();
  }

  /**
   * Declare global variables.
   */
  private void setup() {
    this.scope.declareVariable(new Variable(WORLD_VAR_NAME, false, false, true,
        false, new WorldProxy(this.programManager.getWorld())));
    this.scope.declareVariable(new Variable("$$", false, false, true, false, (long) this.args.size()));
    for (int i = 0; i < this.args.size(); i++) {
      this.scope.declareVariable(new Variable("$" + i, false, false, true, false, this.args.get(i)));
    }
    if (!this.scope.isVariableDefined(NAME_SPECIAL_VARIABLE)) { // May have been overriden by program
      this.scope.declareVariable(new Variable(NAME_SPECIAL_VARIABLE, true, false, false,
          false, this.getName()));
    }
  }

  /**
   * Reset this program: delete all variables, reset instruction pointer and wait time.
   */
  public void reset() {
    this.scope.reset();
    this.timeToWait = 0;
    this.ip = 0;
    this.setup();
  }

  /**
   * Return this program’s name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return this program’s schedule delay.
   */
  public Optional<Long> getScheduleDelay() {
    return Optional.ofNullable(this.scheduleDelay);
  }

  /**
   * Return this program’s repeat amount.
   */
  public Optional<Long> getRepeatAmount() {
    return Optional.ofNullable(this.repeatAmount);
  }

  /**
   * Return this program’s global scope.
   */
  public Scope getScope() {
    return this.scope;
  }

  /**
   * Return whether this program has terminated, i.e executed its last statement and wait time is 0.
   */
  public boolean hasTerminated() {
    return this.ip == this.statements.size() && this.timeToWait == 0;
  }

  /**
   * Return the manager this program is loaded in.
   */
  public ProgramManager getProgramManager() {
    return this.programManager;
  }

  /**
   * Return the random number generator for this program.
   */
  public Random getRNG() {
    return this.rng;
  }

  /**
   * Set the seed for the random number generator of this program.
   * <p>
   * <strong>Notice</strong>: The seed is not saved when this program is serialized as NBT
   * and is thus lost when deserializing. Consequently, programs should not rely on fixed
   * seeds outside of debugging.
   *
   * @param seed The seed.
   */
  public void setRNGSeed(long seed) {
    this.rng.setSeed(seed);
  }

  /**
   * Execute this program.
   *
   * @throws MCCodeRuntimeException If any error occurs.
   * @throws ArithmeticException    If any math error occurs.
   * @throws SyntaxErrorException   If any break/continue statement is found outside a loop,
   *                                or a return statement is found outside a function.
   */
  public void execute() throws MCCodeRuntimeException, SyntaxErrorException {
    if (this.timeToWait > 0) {
      this.timeToWait--;
    } else if (this.ip < this.statements.size()) {
      while (this.ip < this.statements.size()) {
        Statement statement = this.statements.get(this.ip);
        StatementAction action = statement.execute(this.scope);
        if (action == StatementAction.EXIT_FUNCTION) {
          throw new SyntaxErrorException(statement.getLine(), statement.getColumn(),
              "mccode.interpreter.error.return_outside_function");
        } else if (action == StatementAction.EXIT_LOOP) {
          throw new SyntaxErrorException(statement.getLine(), statement.getColumn(),
              "mccode.interpreter.error.break_outside_loop");
        } else if (action == StatementAction.CONTINUE_LOOP) {
          throw new SyntaxErrorException(statement.getLine(), statement.getColumn(),
              "mccode.interpreter.error.continue_outside_loop");
        } else if (action == StatementAction.WAIT) {
          if (this.isModule) {
            throw new MCCodeRuntimeException(this.scope, null, statement.getLine(), statement.getColumn(),
                "mccode.interpreter.error.wait_in_module", this.getName());
          }
          if (statement instanceof WaitStatement) {
            this.ip++;
          }
          break;
        }
        this.ip++;
      }
    }
  }

  /**
   * Return the remaining time to wait for this program.
   */
  public long getWaitTime() {
    return this.timeToWait;
  }

  /**
   * Set the wait time of this program.
   *
   * @param scope Scope this instruction is called from.
   * @param ticks Number of ticks to wait for.
   * @throws EvaluationException If ticks amount is negative.
   */
  public void wait(final Scope scope, long ticks) throws EvaluationException {
    if (ticks < 0) {
      throw new EvaluationException(scope, "mccode.interpreter.error.negative_wait_time");
    }
    this.timeToWait = ticks;
  }

  /**
   * Export the state of this program to NBT.
   */
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(NAME_KEY, this.name);
    tag.setTag(STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(this.statements));
    tag.setTag(SCOPE_KEY, this.scope.writeToNBT());
    if (!this.isModule) {
      if (this.scheduleDelay != null) {
        tag.setLong(SCHEDULE_DELAY_KEY, this.scheduleDelay);
        if (this.repeatAmount != null) {
          tag.setLong(REPEAT_AMOUNT_KEY, this.repeatAmount);
        }
      }
      tag.setLong(WAIT_TIME_KEY, this.timeToWait);
    }
    tag.setInteger(IP_KEY, this.ip);
    tag.setBoolean(IS_MODULE_KEY, this.isModule);
    NBTTagList argsList = new NBTTagList();
    for (Object arg : this.args) {
      NBTTagCompound t = new NBTTagCompound();
      if (arg instanceof Long) {
        t.setString(ARG_TYPE_KEY, "int");
        t.setLong(ARG_VALUE_KEY, (Long) arg);
      } else if (arg instanceof Double) {
        t.setString(ARG_TYPE_KEY, "float");
        t.setDouble(ARG_VALUE_KEY, (Double) arg);
      } else if (arg instanceof Boolean) {
        t.setString(ARG_TYPE_KEY, "boolean");
        t.setBoolean(ARG_VALUE_KEY, (Boolean) arg);
      }
      argsList.appendTag(t);
    }
    tag.setTag(ARGS_KEY, argsList);
    return tag;
  }

  @Override
  public String toString() {
    return this.statements.stream().map(Statement::toString).collect(Collectors.joining("\n"));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Program that = (Program) o;
    return this.timeToWait == that.timeToWait
        && this.name.equals(that.name)
        && this.statements.equals(that.statements)
        && Objects.equals(this.scheduleDelay, that.scheduleDelay)
        && Objects.equals(this.repeatAmount, that.repeatAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.statements, this.scheduleDelay, this.repeatAmount, this.timeToWait);
  }
}
