package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.BooleanType;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * Statement that represents a while-loop.
 */
public class WhileLoopStatement extends Statement {
  public static final int ID = 41;

  private static final String CONDITION_KEY = "Condition";
  private static final String STATEMENTS_KEY = "Statements";
  private static final String IP_KEY = "IP";
  private static final String PAUSED_KEY = "Paused";

  private final Node condition;
  private final List<Statement> statements;
  /**
   * Instruction pointer.
   */
  private int ip;
  /**
   * Whether the loop encountered a "wait" statement.
   */
  private boolean paused;

  /**
   * Create a statement that represents a while-loop.
   *
   * @param condition  Expression that evaluates to a boolean.
   * @param statements Statements of the loop.
   */
  public WhileLoopStatement(final Node condition, final List<Statement> statements) {
    this.condition = condition;
    this.statements = statements;
    this.ip = 0;
    this.paused = false;
  }

  /**
   * Create a statement that represents a while-loop.
   *
   * @param tag The tag to deserialize.
   */
  public WhileLoopStatement(final NBTTagCompound tag) {
    this.condition = NodeNBTHelper.getNodeForTag(tag.getCompoundTag(CONDITION_KEY));
    this.statements = StatementNBTHelper.deserializeStatementsList(tag, STATEMENTS_KEY);
    this.ip = tag.getInteger(IP_KEY);
    this.paused = tag.getBoolean(PAUSED_KEY);
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    BooleanType booleanType = ProgramManager.getTypeInstance(BooleanType.class);
    exit:
    // Do not re-evaluate condition if loop was paused by "wait" a statement
    while (this.paused || booleanType.implicitCast(scope, this.condition.evaluate(scope))) {
      if (this.paused) {
        this.paused = false;
      }
      while (this.ip < this.statements.size()) {
        StatementAction action = this.statements.get(this.ip).execute(scope);
        if (action == StatementAction.EXIT_LOOP) {
          break exit;
        } else if (action == StatementAction.CONTINUE_LOOP) {
          this.ip = 0;
          break;
        } else if (action == StatementAction.EXIT_FUNCTION || action == StatementAction.WAIT) {
          if (action == StatementAction.WAIT) {
            this.paused = true;
            this.ip++;
          } else {
            this.ip = 0;
          }
          return action;
        } else {
          this.ip++;
        }
      }
    }
    this.ip = 0;

    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(CONDITION_KEY, this.condition.writeToNBT());
    tag.setTag(STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(this.statements));
    tag.setInteger(IP_KEY, this.ip);
    tag.setBoolean(PAUSED_KEY, this.paused);
    return tag;
  }

  @Override
  public String toString() {
    String s = " ";
    if (!this.statements.isEmpty()) {
      s = Utils.indentStatements(this.statements);
    }
    return String.format("while %s do%send", this.condition, s);
  }
}
