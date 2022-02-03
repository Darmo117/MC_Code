package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Statement that exits the function it is in.
 */
public class ReturnStatement extends Statement {
  public static final int ID = 62;
  public static final String RETURN_SPECIAL_VAR_NAME = "$return";

  public static final String EXPR_KEY = "Expression";

  private final Node node;

  /**
   * Create a return statement.
   *
   * @param node Expression to return. May be null.
   */
  public ReturnStatement(final Node node) {
    this.node = Objects.requireNonNull(node);
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
    NBTTagCompound tag = super.writeToNBT();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ReturnStatement that = (ReturnStatement) o;
    return this.node.equals(that.node);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.node);
  }
}
