package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.BinaryOperator;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Statement that applies the "delete item" operator.
 */
public class DeleteItemStatement extends Statement {
  public static final int ID = 21;

  public static final String TARGET_KEY = "Target";
  public static final String KEY_KEY = "Key";

  private final Node target;
  private final Node key;

  /**
   * Create a statement that applies the "delete item" operator.
   *
   * @param target Expression that returns the object to apply the operator on.
   * @param key    Expression that returns the key to delete.
   */
  public DeleteItemStatement(final Node target, final Node key) {
    this.target = Objects.requireNonNull(target);
    this.key = Objects.requireNonNull(key);
  }

  /**
   * Create a statement that applies the "delete item" operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public DeleteItemStatement(final NBTTagCompound tag) {
    this(
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(TARGET_KEY)),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(KEY_KEY))
    );
  }

  @Override
  public StatementAction execute(final Scope scope) throws EvaluationException, ArithmeticException {
    Object targetValue = this.target.evaluate(scope);
    Type<?> targetType = ProgramManager.getTypeForValue(targetValue);
    targetType.applyOperator(scope, BinaryOperator.DEL_ITEM, targetValue, this.key.evaluate(scope), null, true);
    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(TARGET_KEY, this.target.writeToNBT());
    tag.setTag(KEY_KEY, this.key.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    return String.format("del %s[%s];", this.target, this.key);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    DeleteItemStatement that = (DeleteItemStatement) o;
    return this.target.equals(that.target) && this.key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.target, this.key);
  }
}
