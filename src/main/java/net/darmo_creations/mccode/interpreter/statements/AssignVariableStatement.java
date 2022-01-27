package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class AssignVariableStatement extends Statement {
  public static final int ID = 12;

  private static final String VAR_NAME_KEY = "VariableName";
  private static final String OPERATOR_KEY = "Operator";
  private static final String VALUE_KEY = "Value";

  private final String variableName;
  private final AssigmentOperator operator;
  private final Node value;

  public AssignVariableStatement(final String variableName, final AssigmentOperator operator, final Node value) {
    this.variableName = Objects.requireNonNull(variableName);
    this.operator = Objects.requireNonNull(operator);
    this.value = Objects.requireNonNull(value);
  }

  public AssignVariableStatement(final NBTTagCompound tag) {
    this(
        tag.getString(VAR_NAME_KEY),
        AssigmentOperator.fromString(tag.getString(OPERATOR_KEY)),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(VALUE_KEY))
    );
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    ProgramManager pm = scope.getProgramManager();
    Object targetObject = scope.getVariable(this.variableName, false);
    Type<?> targetType = pm.getTypeForValue(targetObject);
    Object valueObject = this.value.evaluate(scope);
    Object result = this.operator.getBaseOperator()
        .map(op -> targetType.applyOperator(scope, op, targetObject, valueObject, true))
        .orElse(pm.getTypeForValue(valueObject).copy(scope, valueObject));
    scope.setVariable(this.variableName, result, false);

    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setString(VAR_NAME_KEY, this.variableName);
    tag.setString(OPERATOR_KEY, this.operator.getSymbol());
    tag.setTag(VALUE_KEY, this.value.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    return String.format("%s %s %s;", this.variableName, this.operator.getSymbol(), this.value);
  }
}
