package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class SetPropertyStatement extends Statement {
  public static final int ID = 14;

  private static final String TARGET_KEY = "Target";
  private static final String PROPERTY_NAME_KEY = "PropertyName";
  private static final String OPERATOR_KEY = "Operator";
  private static final String VALUE_KEY = "Value";

  private final Node target;
  private final String propertyName;
  private final AssigmentOperator operator;
  private final Node value;

  public SetPropertyStatement(final Node target, final String propertyName, final AssigmentOperator operator, final Node value) {
    this.target = Objects.requireNonNull(target);
    this.propertyName = Objects.requireNonNull(propertyName);
    this.operator = Objects.requireNonNull(operator);
    this.value = Objects.requireNonNull(value);
  }

  public SetPropertyStatement(final NBTTagCompound tag) {
    this(
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(TARGET_KEY)),
        tag.getString(PROPERTY_NAME_KEY),
        AssigmentOperator.fromString(OPERATOR_KEY),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(VALUE_KEY))
    );
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    Object targetObject = this.target.evaluate(scope);
    Type<?> targetType = ProgramManager.getTypeForValue(targetObject);
    Object valueObject = this.value.evaluate(scope);
    Object result = this.operator.getBaseOperator()
        .map(op -> targetType.applyOperator(scope, op, targetObject, valueObject, null, true))
        .orElse(valueObject);
    targetType.setProperty(scope, targetObject, this.propertyName, result);

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
    tag.setString(PROPERTY_NAME_KEY, this.propertyName);
    tag.setString(OPERATOR_KEY, this.operator.getSymbol());
    tag.setTag(VALUE_KEY, this.value.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    return String.format("%s.%s %s %s;", this.target, this.propertyName, this.operator, this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    SetPropertyStatement that = (SetPropertyStatement) o;
    return this.target.equals(that.target) && this.propertyName.equals(that.propertyName)
        && this.operator == that.operator && this.value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.target, this.propertyName, this.operator, this.value);
  }
}
