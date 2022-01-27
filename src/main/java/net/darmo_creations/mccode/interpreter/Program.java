package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.statements.Statement;
import net.darmo_creations.mccode.interpreter.statements.StatementAction;
import net.darmo_creations.mccode.interpreter.statements.StatementNBTHelper;
import net.darmo_creations.mccode.interpreter.types.WorldProxy;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A program is composed of a list of statements that can be executed.
 * <p>
 * The program’s state can be saved and restored and world loading and unloading.
 */
public class Program {
  public static final String WORLD_VAR_NAME = "WORLD";

  private static final String NAME_KEY = "Name";
  private static final String STATEMENTS_KEY = "Statements";
  private static final String SCOPE_KEY = "Scope";
  private static final String SCHEDULE_DELAY_KEY = "ScheduleDelay";
  private static final String REPEAT_AMOUNT_KEY = "RepeatAmount";
  private static final String WAIT_TIME_KEY = "WaitTime";
  private static final String IP_KEY = "IP";

  private final String name;
  private final List<Statement> statements;
  private final ProgramManager programManager;
  private final Scope scope;
  private final Integer scheduleDelay;
  private final Integer repeatAmount;
  private int timeToWait;
  /**
   * Instruction pointer.
   */
  private int ip;

  /**
   * Create a new program.
   *
   * @param name           Program’s name.
   * @param statements     Program’s statements.
   * @param scheduleDelay  Program’s schedule delay. May be null.
   * @param repeatAmount   Program’s repeat amount. May be null.
   * @param programManager Program’s manager.
   * @throws MCCodeRuntimeException If the schedule delay or repeat amount is negative,
   *                                or a repeat amount is defined without a schedule delay.
   */
  public Program(final String name, final List<Statement> statements, final Integer scheduleDelay, final Integer repeatAmount, ProgramManager programManager) {
    this.programManager = Objects.requireNonNull(programManager);
    this.name = Objects.requireNonNull(name);
    this.statements = Objects.requireNonNull(statements);
    this.scope = new Scope(this);
    if (scheduleDelay != null && scheduleDelay < 0) {
      throw new MCCodeRuntimeException(this.scope, "mccode.interpreter.error.invalid_schedule_delay", scheduleDelay);
    }
    if (repeatAmount != null && repeatAmount <= 0) {
      throw new MCCodeRuntimeException(this.scope, "mccode.interpreter.error.invalid_schedule_repeat", scheduleDelay);
    }
    if (scheduleDelay == null && repeatAmount != null) {
      throw new MCCodeRuntimeException(this.scope, "mccode.interpreter.error.missing_schedule_delay");
    }
    this.scheduleDelay = scheduleDelay;
    this.repeatAmount = repeatAmount;
    this.timeToWait = 0;
    this.ip = 0;
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
    this.scheduleDelay = tag.getInteger(SCHEDULE_DELAY_KEY);
    this.repeatAmount = tag.getInteger(REPEAT_AMOUNT_KEY);
    this.timeToWait = tag.getInteger(WAIT_TIME_KEY);
    this.ip = tag.getInteger(IP_KEY);
    this.setup();
  }

  /**
   * Declare global variables.
   */
  private void setup() {
    this.scope.declareVariable(new Variable(WORLD_VAR_NAME, false, false, true,
        false, new WorldProxy(this.programManager.getWorld(), -1)));
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
  public Optional<Integer> getScheduleDelay() {
    return Optional.of(this.scheduleDelay);
  }

  /**
   * Return this program’s repeat amount.
   */
  public Optional<Integer> getRepeatAmount() {
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
   * Execute this program.
   *
   * @param worldTick Current world tick.
   * @throws MCCodeRuntimeException If any error occurs.
   * @throws ArithmeticException    If any math error occurs.
   */
  public void execute(final int worldTick) throws MCCodeRuntimeException, ArithmeticException {
    if (this.timeToWait > 0) {
      this.timeToWait--;
    } else if (this.ip < this.statements.size()) {
      WorldProxy world = (WorldProxy) this.scope.getVariable(WORLD_VAR_NAME, false);
      world.setWorldTick(worldTick);
      while (this.ip < this.statements.size()) {
        StatementAction action = this.statements.get(this.ip).execute(this.scope);
        this.ip++;
        if (action == StatementAction.EXIT_FUNCTION) {
          throw new MCCodeRuntimeException(this.scope, "mccode.interpreter.error.return_outside_function");
        } else if (action == StatementAction.WAIT) {
          break;
        }
      }
    }
  }

  /**
   * Set the wait time of this program.
   *
   * @param scope Scope this instruction is called from.
   * @param ticks Number of ticks to wait for.
   * @throws EvaluationException If ticks amount is negative or 0.
   */
  public void wait(final Scope scope, int ticks) throws EvaluationException {
    if (ticks <= 0) {
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
    tag.setInteger(SCHEDULE_DELAY_KEY, this.scheduleDelay);
    tag.setInteger(REPEAT_AMOUNT_KEY, this.repeatAmount);
    tag.setInteger(WAIT_TIME_KEY, this.timeToWait);
    tag.setInteger(IP_KEY, this.ip);
    return tag;
  }

  @Override
  public String toString() {
    return this.statements.stream().map(Statement::toString).collect(Collectors.joining("\n"));
  }
}
