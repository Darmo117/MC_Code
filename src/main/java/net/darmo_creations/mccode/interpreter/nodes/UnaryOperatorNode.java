package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

/**
 * A node that represents an operator with a single operand.
 */
public class UnaryOperatorNode extends OperatorNode {
  public static final int ID = 200;

  private final Operator operator;

  /**
   * Create a unary operator node.
   *
   * @param operator Operator’s symbol.
   * @param operand  Operator’s operand.
   */
  public UnaryOperatorNode(final Operator operator, final Node operand) {
    super(operator.getSymbol(), 1, Collections.singletonList(operand));
    this.operator = operator;
  }

  /**
   * Create a unary operator node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public UnaryOperatorNode(final NBTTagCompound tag) {
    super(tag);
    this.operator = Operator.fromString(this.getSymbol());
  }

  @Override
  protected final Object evaluateImpl(Scope scope, final List<Object> values) {
    Object arg1 = values.get(0);
    Type<?> arg1Type = ProgramManager.getTypeForValue(arg1);
    return arg1Type.applyOperator(scope, this.operator, arg1, null, null, false);
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    Node operand = this.arguments.get(0);
    if (operand instanceof OperatorNode) {
      return String.format("%s(%s)", this.getSymbol(), operand);
    } else {
      return this.getSymbol() + this.arguments.get(0);
    }
  }
}
