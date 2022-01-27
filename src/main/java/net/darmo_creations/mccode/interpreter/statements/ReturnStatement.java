package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Statement that exits the function it is in.
 */
public class ReturnStatement extends Statement {
  public static final int ID = 62;
  public static final String RETURN_SPECIAL_VAR_NAME = "$return";

  private static final String EXPR_KEY = "Expression";

  private final Node node;

  /**
   * Create a return statement.
   *
   * @param node Expression to return. May be null.
   */
  public ReturnStatement(final Node node) {
    this.node = node;
  }

  /**
   * Create a return statement from an NBT statement.
   *
   * @param tag The tag to deserialize.
   */
  public ReturnStatement(final NBTTagCompound tag) {
    this(NodeNBTHelper.getNodeForTag(tag.getCompoundTag(EXPR_KEY)));
  }

  @Override
  public StatementAction execute(Scope scope) {
    Object value = this.node != null ? this.node.evaluate(scope) : null;
    scope.declareVariable(new Variable(RETURN_SPECIAL_VAR_NAME, false, false, true, false, value));
    return StatementAction.EXIT_FUNCTION;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setTag(EXPR_KEY, this.node.writeToNBT());
    return tag;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return String.format("return %s;", this.node);
  }
}
