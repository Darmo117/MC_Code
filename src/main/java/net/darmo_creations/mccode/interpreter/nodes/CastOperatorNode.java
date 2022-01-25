package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class CastOperatorNode extends Node {
  public static final int ID = 200;

  public static final String TARGET_TYPE_NAME_KEY = "TargetTypeName";
  public static final String VALUE_KEY = "Value";

  private final String targetTypeName;
  private final Node value;

  // TODO check for cast operator for type during parsing
  public CastOperatorNode(final String targetTypeName, final Node value) {
    this.targetTypeName = Objects.requireNonNull(targetTypeName);
    this.value = Objects.requireNonNull(value);
  }

  public CastOperatorNode(final NBTTagCompound tag) {
    this(
        tag.getString(TARGET_TYPE_NAME_KEY),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(VALUE_KEY))
    );
  }

  @Override
  public Object evaluate(final Scope scope) throws EvaluationException, ArithmeticException {
    Type<?> type = scope.getInterpreter().getTypeForName(this.targetTypeName);
    if (type == null) {
      throw new EvaluationException(scope, "mccode.interpreter.error.no_type_for_name", this.targetTypeName);
    }
    return type.explicitCast(scope, this.value.evaluate(scope));
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return String.format("(%s) %s", this.targetTypeName, this.value);
  }
}
