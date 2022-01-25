package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.exceptions.TypeException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.types.Function;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class DefineFunctionStatement extends Statement {
  public static final int ID = 11;

  private static final String NAME_KEY = "Name";
  private static final String FUNCTION_OBJECT_KEY = "FunctionObject";

  private final String name;
  private final Node functionObject;

  public DefineFunctionStatement(final String name, final Node functionObject) {
    this.name = Objects.requireNonNull(name);
    this.functionObject = Objects.requireNonNull(functionObject);
  }

  public DefineFunctionStatement(final NBTTagCompound tag) {
    this(
        tag.getString(NAME_KEY),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(FUNCTION_OBJECT_KEY))
    );
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    Object function = this.functionObject.evaluate(scope);
    if (!(function instanceof Function)) {
      throw new TypeException("expected function, got " + scope.getInterpreter().getTypeForValue(function));
    }
    scope.declareVariable(new Variable(this.name, false, false, false, true, function));

    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setString(NAME_KEY, this.name);
    tag.setTag(FUNCTION_OBJECT_KEY, this.functionObject.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    String s = this.functionObject.toString();
    int i = s.indexOf(' ');
    s = s.substring(0, i + 1) + this.name + s.substring(i + 1);
    return s;
  }
}
