package net.darmo_creations.mccode.interpreter;

import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeException;
import net.darmo_creations.mccode.interpreter.exceptions.MCCodeRuntimeException;
import net.darmo_creations.mccode.interpreter.exceptions.ProgramException;
import net.darmo_creations.mccode.interpreter.statements.Statement;
import net.darmo_creations.mccode.interpreter.statements.StatementAction;
import net.darmo_creations.mccode.interpreter.statements.StatementNBTHelper;
import net.darmo_creations.mccode.interpreter.types.WorldProxy;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A program is composed of a list of statements that can be executed.
 * <p>
 * The program’s state can be saved and restored and world loading and unloading.
 */
public class Program {
  public static final String WORLD_VAR_NAME = "world";

  private static final String NAME_KEY = "Name";
  private static final String STATEMENTS_KEY = "Statements";
  private static final String SCOPE_KEY = "Scope";
  private static final String IP_KEY = "IP";
  private static final String WAIT_TIME_KEY = "WaitTime";

  private final String name;
  private final List<Statement> statements;
  private final Interpreter interpreter;
  private final Scope scope;
  /**
   * Instruction pointer.
   */
  private int ip;
  private int timeToWait;

  public Program(final String name, final List<Statement> statements, Interpreter interpreter) {
    this.interpreter = Objects.requireNonNull(interpreter);
    this.name = Objects.requireNonNull(name);
    this.statements = Objects.requireNonNull(statements);
    this.scope = new Scope(this);
    this.ip = 0;
    this.timeToWait = 0;
    this.setup();
  }

  public Program(final NBTTagCompound tag, Interpreter interpreter) {
    this.interpreter = Objects.requireNonNull(interpreter);
    this.name = tag.getString(NAME_KEY);
    NBTTagList statementsTag = tag.getTagList(STATEMENTS_KEY, new NBTTagCompound().getId());
    this.statements = new ArrayList<>();
    for (NBTBase t : statementsTag) {
      this.statements.add(StatementNBTHelper.getStatementForTag((NBTTagCompound) t));
    }
    this.scope = new Scope(this);
    this.scope.readFromNBT(tag.getCompoundTag(SCOPE_KEY));
    this.ip = tag.getInteger(IP_KEY);
    this.timeToWait = tag.getInteger(WAIT_TIME_KEY);
    this.setup();
  }

  private void setup() {
    this.scope.declareVariable(new Variable(WORLD_VAR_NAME, false, false, true,
        false, new WorldProxy(this.interpreter.getWorld(), -1)));
  }

  public String getName() {
    return this.name;
  }

  public Interpreter getInterpreter() {
    return this.interpreter;
  }

  public boolean hasTerminated() {
    return this.ip == this.statements.size();
  }

  // TODO reload state after world reload or "wait" statement
  public void execute(final int worldTick) throws ProgramException {
    if (this.ip == this.statements.size()) {
      return;
    }
    if (this.timeToWait > 0) {
      this.timeToWait--;
    } else {
      WorldProxy world = (WorldProxy) this.scope.getVariable(WORLD_VAR_NAME, false);
      world.setWorldTick(worldTick);
      for (int i = this.ip; i < this.statements.size(); this.ip = ++i) {
        StatementAction action;
        try {
          action = this.statements.get(i).execute(this.scope);
        } catch (MCCodeException e) {
          throw new ProgramException(i + 1, e);
        }
        if (action == StatementAction.WAIT) {
          break;
        }
      }
    }
  }

  public void wait(final Scope scope, int ticks) throws MCCodeRuntimeException {
    if (ticks < 0) {
      throw new EvaluationException(scope, "mccode.interpreter.error.negative_wait_time");
    }
    this.timeToWait = ticks;
  }

  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString(NAME_KEY, this.name);
    NBTTagList statementsList = new NBTTagList();
    this.statements.forEach(s -> statementsList.appendTag(s.writeToNBT()));
    tag.setTag(STATEMENTS_KEY, statementsList);
    tag.setTag(SCOPE_KEY, this.scope.writeToNBT());
    tag.setInteger(IP_KEY, this.ip);
    tag.setInteger(WAIT_TIME_KEY, this.timeToWait);
    return tag;
  }

  @Override
  public String toString() {
    return this.statements.stream().map(Statement::toString).collect(Collectors.joining("\n"));
  }
}
