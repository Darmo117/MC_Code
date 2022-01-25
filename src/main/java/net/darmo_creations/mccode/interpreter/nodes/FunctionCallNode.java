package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.type_wrappers.Type;
import net.darmo_creations.mccode.interpreter.types.Function;
import net.darmo_creations.mccode.interpreter.types.UserFunction;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link Node} representing the call to a function.
 */
public class FunctionCallNode extends OperationNode {
  public static final int ID = 104;

  private static final String FUNCTION_OBJ_KEY = "FunctionObject";

  protected final Node functionObject;

  /**
   * Create a function call.
   *
   * @param functionObject A node that evaluates to a {@link Function} object.
   * @param arguments      Function’s arguments.
   */
  public FunctionCallNode(final Node functionObject, final List<Node> arguments) {
    super(arguments);
    this.functionObject = Objects.requireNonNull(functionObject);
  }

  /**
   * Create a function call {@link Node} from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public FunctionCallNode(final NBTTagCompound tag) {
    super(tag);
    this.functionObject = NodeNBTHelper.getNodeForTag(tag.getCompoundTag(FUNCTION_OBJ_KEY));
  }

  @Override
  public Object evaluate(final Scope scope) throws EvaluationException, ArithmeticException {
    Object o = this.functionObject.evaluate(scope);

    Function function;
    try {
      function = (Function) o;
    } catch (ClassCastException e) {
      throw new EvaluationException(scope, "mccode.interpreter.error.calling_non_callable",
          scope.getInterpreter().getTypeForValue(o));
    }

    Scope functionScope;
    if (function instanceof UserFunction) {
      functionScope = new Scope(function.getInternalName(), ((UserFunction) function).getClosure());
    } else {
      functionScope = new Scope(function.getInternalName(), scope);
    }

    for (int i = 0; i < this.operands.size(); i++) {
      Pair<String, ? extends Type<?>> parameter = function.getParameter(i);
      functionScope.declareVariable(new Variable(parameter.getKey(), false, false, false, false, this.operands.get(i).evaluate(scope)));
    }

    return function.apply(functionScope);
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(FUNCTION_OBJ_KEY, this.functionObject.writeToNBT());
    return tag;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    return String.format("%s(%s)", this.functionObject, this.operands.stream().map(Node::toString).collect(Collectors.joining(", ")));
  }
}
