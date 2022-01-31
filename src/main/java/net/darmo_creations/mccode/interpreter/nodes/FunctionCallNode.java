package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.Parameter;
import net.darmo_creations.mccode.interpreter.ProgramManager;
import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.types.Function;
import net.darmo_creations.mccode.interpreter.types.UserFunction;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A node that represents the call to a function.
 */
public class FunctionCallNode extends OperationNode {
  public static final int ID = 103;

  public static final String FUNCTION_OBJ_KEY = "FunctionObject";

  protected final Node functionObject;

  /**
   * Create a function call node.
   *
   * @param functionObject Expression that evaluates to a {@link Function} object.
   * @param arguments      Function’s arguments.
   */
  public FunctionCallNode(final Node functionObject, final List<Node> arguments) {
    super(arguments);
    this.functionObject = Objects.requireNonNull(functionObject);
  }

  /**
   * Create a function call node from an NBT tag.
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
          ProgramManager.getTypeForValue(o));
    }

    Scope functionScope;
    if (function instanceof UserFunction) {
      // User-defined functions use global scope as closure as they can only be declared in the global scope
      functionScope = new Scope(function.getName(), scope.getProgram().getScope());
    } else {
      functionScope = new Scope(function.getName(), scope);
    }

    if (this.arguments.size() != function.getParameters().size()) {
      throw new EvaluationException(scope, "mccode.interpreter.error.invalid_function_arguments_number",
          function.getName(), function.getParameters().size(), this.arguments.size());
    }

    for (int i = 0; i < this.arguments.size(); i++) {
      Parameter parameter = function.getParameter(i);
      functionScope.declareVariable(new Variable(parameter.getName(), false, false, false, true, this.arguments.get(i).evaluate(scope)));
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
    return String.format("%s(%s)", this.functionObject, this.arguments.stream().map(Node::toString).collect(Collectors.joining(", ")));
  }
}
