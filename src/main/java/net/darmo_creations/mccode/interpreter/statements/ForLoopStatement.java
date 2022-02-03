package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.darmo_creations.mccode.interpreter.type_wrappers.UnaryOperator;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Statement that represents a for-loop.
 */
public class ForLoopStatement extends Statement {
  public static final int ID = 42;

  public static final String VARIABLE_NAME_KEY = "VariableName";
  public static final String VALUES_KEY = "Values";
  public static final String STATEMENTS_KEY = "Statements";
  public static final String IP_KEY = "IP";
  public static final String ITERATOR_INDEX_KEY = "IteratorIndex";
  public static final String PAUSED_KEY = "Paused";

  private final String variableName;
  private final Node values;
  private final List<Statement> statements;
  /**
   * Instruction pointer.
   */
  private int ip;
  /**
   * Index of the current value from the iterator.
   */
  private int iteratorIndex;
  /**
   * Whether the for-loop was just reloaded from an NBT tag.
   */
  private boolean resumeAfterLoad;
  /**
   * Whether the loop encountered a "wait" statement.
   */
  private boolean paused;

  /**
   * Create a statement that represents a for-loop.
   *
   * @param variableName Name of the loop variable.
   * @param values       Expression that returns an iterator.
   * @param statements   Statements of the loop.
   */
  public ForLoopStatement(final String variableName, final Node values, final List<Statement> statements) {
    this.variableName = variableName;
    this.values = values;
    this.statements = statements;
    this.ip = 0;
    this.iteratorIndex = 0;
    this.paused = false;
  }

  /**
   * Create a statement that represents a for-loop from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public ForLoopStatement(final NBTTagCompound tag) {
    this.variableName = tag.getString(VARIABLE_NAME_KEY);
    this.values = NodeNBTHelper.getNodeForTag(tag.getCompoundTag(VALUES_KEY));
    this.statements = StatementNBTHelper.deserializeStatementsList(tag, STATEMENTS_KEY);
    this.ip = tag.getInteger(IP_KEY);
    this.iteratorIndex = tag.getInteger(ITERATOR_INDEX_KEY);
    this.paused = tag.getBoolean(PAUSED_KEY);
    this.resumeAfterLoad = true;
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    Object valuesObject = this.values.evaluate(scope);
    Type<?> type = ProgramManager.getTypeForValue(valuesObject);
    Iterator<?> iterator = (Iterator<?>) type.applyOperator(scope, UnaryOperator.ITERATE, valuesObject, null, null, false);
    boolean declareVariable = !this.paused && !this.resumeAfterLoad;

    // Skip elements already iterated over
    for (int i = 0; i < this.iteratorIndex; i++) {
      iterator.next();
    }

    exit:
    // Do not test again if loop was paused by a "wait" statement
    while (this.paused || this.resumeAfterLoad || iterator.hasNext()) {
      // Do not re-declare or re-set variable if loop was paused by a "wait" statement
      if (this.ip == 0) {
        if (declareVariable) {
          scope.declareVariable(new Variable(this.variableName, false, false, false, true, iterator.next()));
          declareVariable = false;
        } else {
          Object next = iterator.next();
          scope.setVariable(this.variableName, next, false);
        }
      } else {
        this.paused = false;
        this.resumeAfterLoad = false;
      }

      this.iteratorIndex++;
      while (this.ip < this.statements.size()) {
        StatementAction action = this.statements.get(this.ip).execute(scope);
        if (action == StatementAction.EXIT_LOOP) {
          this.ip = 0;
          break exit;
        } else if (action == StatementAction.CONTINUE_LOOP) {
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
      this.ip = 0;
    }
    scope.deleteVariable(this.variableName, false);

    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setString(VARIABLE_NAME_KEY, this.variableName);
    tag.setTag(VALUES_KEY, this.values.writeToNBT());
    tag.setTag(STATEMENTS_KEY, StatementNBTHelper.serializeStatementsList(this.statements));
    tag.setInteger(IP_KEY, this.ip);
    tag.setInteger(ITERATOR_INDEX_KEY, this.iteratorIndex);
    tag.setBoolean(PAUSED_KEY, this.paused);
    return tag;
  }

  @Override
  public String toString() {
    String s = " ";
    if (!this.statements.isEmpty()) {
      s = Utils.indentStatements(this.statements);
    }
    return String.format("for %s in %s do%send", this.variableName, this.values, s);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ForLoopStatement that = (ForLoopStatement) o;
    return this.ip == that.ip && this.iteratorIndex == that.iteratorIndex && this.resumeAfterLoad == that.resumeAfterLoad
        && this.paused == that.paused && this.variableName.equals(that.variableName) && this.values.equals(that.values)
        && this.statements.equals(that.statements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.variableName, this.values, this.statements, this.ip, this.iteratorIndex, this.resumeAfterLoad, this.paused);
  }
}
