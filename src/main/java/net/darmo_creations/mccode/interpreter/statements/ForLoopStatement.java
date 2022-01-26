package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Utils;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;
import java.util.List;

public class ForLoopStatement extends Statement {
  public static final int ID = 42;

  private static final String VARIABLE_NAME_KEY = "VariableName";
  private static final String VALUES_KEY = "Values";
  private static final String STATEMENTS_KEY = "Statements";
  private static final String IP_KEY = "IP";
  private static final String ITERATOR_INDEX_KEY = "IteratorIndex";
  private static final String PAUSED_KEY = "Paused";

  private final String variableName;
  private final Node values;
  private final List<Statement> statements;
  private int ip;
  private int iteratorIndex;
  private boolean resumeAfterLoad;
  private boolean paused;

  public ForLoopStatement(final String variableName, final Node values, final List<Statement> statements) {
    this.variableName = variableName;
    this.values = values;
    this.statements = statements;
    this.ip = 0;
    this.iteratorIndex = 0;
    this.paused = false;
  }

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
    Type<?> type = scope.getInterpreter().getTypeForValue(valuesObject);
    Iterator<?> iterator = (Iterator<?>) type.applyOperator(scope, Operator.ITERATE, valuesObject, null, false);
    boolean declareVariable = true;

    if (this.resumeAfterLoad) {
      // Skip elements already iterated over
      for (int i = 0; i <= this.iteratorIndex; i++) {
        iterator.next();
      }
    }

    exit:
    // Do not test again if loop was paused by a "wait" statement
    while (this.paused || this.resumeAfterLoad || iterator.hasNext()) {
      // Do not re-declare or re-set variable if loop was paused by a "wait" statement
      if (!this.paused && !this.resumeAfterLoad) {
        if (declareVariable) {
          scope.declareVariable(new Variable(this.variableName, false, false, false, true, iterator.next()));
          declareVariable = false;
        } else {
          scope.setVariable(this.variableName, iterator.next(), false);
        }
      } else {
        this.paused = false;
        this.resumeAfterLoad = false;
      }

      this.iteratorIndex++;
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
}
