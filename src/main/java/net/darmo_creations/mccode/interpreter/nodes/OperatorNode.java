package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.SyntaxErrorException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link Node} representing an operator.
 */
public abstract class OperatorNode extends OperationNode {
  public static final String SYMBOL_KEY = "Symbol";

  private final String symbol;

  /**
   * Create an operator.
   *
   * @param symbol   Operator’s symbol.
   * @param arity    Operator’s arity, i.e. its number of operands.
   * @param operands Operator’s operands.
   * @throws SyntaxErrorException If the number of operands does not match the arity.
   */
  public OperatorNode(final String symbol, final int arity, final List<Node> operands) throws SyntaxErrorException {
    super(operands);
    this.symbol = Objects.requireNonNull(symbol);
    if (this.operands.size() != arity) {
      throw new SyntaxErrorException(String.format("operator %s expected %d arguments, got %d", symbol, arity, operands.size()));
    }
  }

  /**
   * Create an operator {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public OperatorNode(final NBTTagCompound tag) {
    super(tag);
    this.symbol = tag.getString(SYMBOL_KEY);
  }

  /**
   * Return this operator’s symbol.
   */
  public String getSymbol() {
    return this.symbol;
  }

  /**
   * Evaluate the operator then return its value.
   * Different implementations of the same operator may be called depending on the types of the operands.
   *
   * @param scope Scope this operator is called from.
   * @return Operator’s result.
   * @throws EvaluationException If an error occured during {@link Node} evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  @Override
  public Object evaluate(Scope scope) throws EvaluationException, ArithmeticException {
    return this.evaluateImpl(scope, this.operands.stream().map(node -> node.evaluate(scope)).collect(Collectors.toList()));
  }

  /**
   * Delegate method that returns the result of the operator.
   *
   * @param scope  Scope this operator is called from.
   * @param values Values of the operands.
   * @return Operator’s result.
   */
  protected abstract Object evaluateImpl(Scope scope, final List<Object> values);

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setString(SYMBOL_KEY, this.symbol);
    return tag;
  }
}
