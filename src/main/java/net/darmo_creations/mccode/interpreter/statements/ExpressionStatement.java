package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.minecraft.nbt.NBTTagCompound;

public class ExpressionStatement extends Statement {
  public static final int ID = 30;

  public static final String EXPRESSION_KEY = "Expression";

  private final Node expression;

  public ExpressionStatement(final Node expression) {
    this.expression = expression;
  }

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
}
