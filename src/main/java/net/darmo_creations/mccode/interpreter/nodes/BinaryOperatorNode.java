package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.type_wrappers.Operator;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.List;

/**
 * A node that represents an operator with two operands.
 */
public class BinaryOperatorNode extends OperatorNode {
  public static final int ID = 201;

  private final Operator operator;

  /**
   * Create a binary operator node with two operands.
   *
   * @param operator Operator to apply.
   * @param left     Left operand.
   * @param right    Right operand.
   */
  public BinaryOperatorNode(final Operator operator, final Node left, final Node right) {
    super(operator.getSymbol(), 2, Arrays.asList(left, right));
    this.operator = operator;
  }

  /**
   * Create a binary operator node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public BinaryOperatorNode(final NBTTagCompound tag) {
    super(tag);
    this.operator = Operator.fromString(this.getSymbol());
  }

  @Override
  protected final Object evaluateImpl(Scope scope, final List<Object> values) {
    boolean flipped = this.operator.isFlipped();
    Object arg1 = values.get(0);
    Object arg2 = values.get(1);
    Type<?> argType = scope.getProgramManager().getTypeForValue(flipped ? arg2 : arg1);
    return argType.applyOperator(scope, this.operator, flipped ? arg2 : arg1, flipped ? arg1 : arg2, null, false);
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return String.format("%s %s %s", this.arguments.get(0), this.getSymbol(), this.arguments.get(1));
  }
}
