package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class SetItemStatement extends Statement {
  public static final int ID = 13;

  public static final String TARGET_KEY = "Target";
  public static final String KEY_KEY = "Key";
  public static final String OPERATOR_KEY = "Operator";
  public static final String VALUE_KEY = "Value";

  private final Node target;
  private final Node key;
  private final AssigmentOperator operator;
  private final Node value;

  public SetItemStatement(final Node target, final Node key, final AssigmentOperator operator, final Node value) {
    this.target = Objects.requireNonNull(target);
    this.key = Objects.requireNonNull(key);
    this.operator = Objects.requireNonNull(operator);
    this.value = Objects.requireNonNull(value);
  }

  public SetItemStatement(final NBTTagCompound tag) {
    this(
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(TARGET_KEY)),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(KEY_KEY)),
        AssigmentOperator.fromString(tag.getString(OPERATOR_KEY)),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(VALUE_KEY))
    );
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    Object targetObject = this.target.evaluate(scope);
    Type<?> targetObjectType = scope.getInterpreter().getTypeForValue(targetObject);
    Object keyValue = this.key.evaluate(scope);
    Object newValue = this.value.evaluate(scope);
    Object oldValue = targetObjectType.applyOperator(scope, Operator.GET_ITEM, targetObject, keyValue, false);
    Object resultValue = this.operator.getBaseOperator()
        .map(op -> targetObjectType.applyOperator(scope, op, oldValue, newValue, true))
        .orElse(targetObject);
    targetObjectType.setItem(scope, targetObject, keyValue, resultValue);

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
    tag.setString(OPERATOR_KEY, this.operator.getSymbol());
    tag.setTag(VALUE_KEY, this.value.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    return String.format("%s[%s] %s %s;", this.target, this.key, this.operator.getSymbol(), this.value);
  }
}
