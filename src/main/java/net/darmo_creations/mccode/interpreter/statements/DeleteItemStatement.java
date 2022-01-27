package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class DeleteItemStatement extends Statement {
  public static final int ID = 21;

  private static final String TARGET_KEY = "Target";
  private static final String KEY_KEY = "Key";

  private final Node target;
  private final Node key;

  public DeleteItemStatement(final Node target, final Node key) {
    this.target = Objects.requireNonNull(target);
    this.key = Objects.requireNonNull(key);
  }

  public DeleteItemStatement(final NBTTagCompound tag) {
    this(
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(TARGET_KEY)),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(KEY_KEY))
    );
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    Object targetValue = this.target.evaluate(scope);
    Type<?> targetType = scope.getProgramManager().getTypeForValue(targetValue);
    targetType.deleteItem(scope, targetType, this.key.evaluate(scope));

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
}
