package net.darmo_creations.mccode.interpreter.nodes;

import net.darmo_creations.mccode.interpreter.*;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A node that represents the call to an object’s method.
 */
public class MethodCallNode extends OperationNode {
  public static final int ID = 102;

  public static final String INSTANCE_KEY = "Instance";
  public static final String METHOD_NAME_KEY = "MethodName";

  protected final Node instance;
  private final String methodName;

  /**
   * Create a method call node.
   *
   * @param instance  Expression evaluating to an object the method will be applied on.
   * @param arguments Method’s arguments.
   */
  public MethodCallNode(final Node instance, final String methodName, final List<Node> arguments) {
    super(arguments);
    this.instance = Objects.requireNonNull(instance);
    this.methodName = Objects.requireNonNull(methodName);
  }

  /**
   * Create a method call node from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public MethodCallNode(final NBTTagCompound tag) {
    super(tag);
    this.instance = NodeNBTHelper.getNodeForTag(tag.getCompoundTag(INSTANCE_KEY));
    this.methodName = tag.getString(METHOD_NAME_KEY);
  }

  @Override
  public Object evaluate(final Scope scope) throws EvaluationException, ArithmeticException {
    Object self = this.instance.evaluate(scope);
    MemberFunction method = ProgramManager.getTypeForValue(self).getMethod(scope, this.methodName);
    Scope functionScope = new Scope(method.getName(), scope);

    if (this.arguments.size() != method.getParameters().size()) {
      throw new EvaluationException(scope, "mccode.interpreter.error.invalid_method_arguments_number",
          method.getHostType(), method.getName(), method.getParameters().size(), this.arguments.size());
    }

    functionScope.declareVariable(new Variable(MemberFunction.SELF_PARAM_NAME, false, false, true, false, self));
    for (int i = 0; i < this.arguments.size(); i++) {
      Parameter parameter = method.getParameter(i);
      functionScope.declareVariable(new Variable(parameter.getName(), false, false, false, true, this.arguments.get(i).evaluate(scope)));
    }

    return method.apply(functionScope);
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(INSTANCE_KEY, this.instance.writeToNBT());
    tag.setString(METHOD_NAME_KEY, this.methodName);
    return tag;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public String toString() {
    String parameters = this.arguments.stream().map(Node::toString).collect(Collectors.joining(", "));
    return String.format("%s.%s(%s)", this.instance, this.methodName, parameters);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    MethodCallNode that = (MethodCallNode) o;
    return this.instance.equals(that.instance) && this.methodName.equals(that.methodName) && this.arguments.equals(that.arguments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.instance, this.methodName, this.arguments);
  }
}
