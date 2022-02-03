package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Statement that contains a single expression.
 */
public class ExpressionStatement extends Statement {
  public static final int ID = 30;

  public static final String EXPRESSION_KEY = "Expression";

  private final Node expression;

  /**
   * Create a statement that contains a single expression.
   *
   * @param expression The expression to evaluate.
   */
  public ExpressionStatement(final Node expression) {
    this.expression = expression;
  }

  /**
   * Create a statement that contains a single expression.
   *
   * @param tag The tag to deserialize.
   */
  public ExpressionStatement(final NBTTagCompound tag) {
    this(NodeNBTHelper.getNodeForTag(tag.getCompoundTag(EXPRESSION_KEY)));
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    this.expression.evaluate(scope);
    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(EXPRESSION_KEY, this.expression.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    return this.expression + ";";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ExpressionStatement that = (ExpressionStatement) o;
    return this.expression.equals(that.expression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.expression);
  }
}
