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

  private void setup() {
    this.scope.declareVariable(new Variable(WORLD_VAR_NAME, false, false, true,
        false, new WorldProxy(this.programManager.getWorld(), -1)));
  }

  public void reset() {
    this.scope.reset();
    this.timeToWait = 0;
    this.ip = 0;
    this.setup();
  }

  public String getName() {
    return this.name;
  }

  public Optional<Integer> getScheduleDelay() {
    return Optional.of(this.scheduleDelay);
  }

  public Optional<Integer> getRepeatAmount() {
    return Optional.ofNullable(this.repeatAmount);
  }

  public ProgramManager getProgramManager() {
    return this.programManager;
  }

  public Scope getScope() {
    return this.scope;
  }

  public boolean hasTerminated() {
    return this.ip == this.statements.size() && this.timeToWait == 0;
  }

  public void execute(final int worldTick) throws MCCodeRuntimeException {
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

  public void wait(final Scope scope, int ticks) throws EvaluationException {
    if (ticks < 0) {
      throw new EvaluationException(scope, "mccode.interpreter.error.negative_wait_time");
    }
    this.timeToWait = ticks;
  }

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
