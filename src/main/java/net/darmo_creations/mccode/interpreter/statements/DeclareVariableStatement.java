package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Statement that declares a new variable.
 */
public class DeclareVariableStatement extends Statement {
  public static final int ID = 10;

  private static final String CONSTANT_KEY = "Constant";
  private static final String PUBLIC_KEY = "Public";
  private static final String EDITABLE_KEY = "Editable";
  private static final String VAR_NAME_KEY = "VariableName";
  private static final String VALUE_KEY = "Value";

  private final boolean publiclyVisible;
  private final boolean editableByCommands;
  private final boolean constant;
  private final String variableName;
  private final Node value;

  /**
   * Create a statement that declares a new variable.
   *
   * @param publiclyVisible    Whether the variable should be visible from in-game commands.
   * @param editableByCommands Whether the variable should be editable from in-game commands.
   * @param constant           Whether the variable should be a constant.
   * @param variableName       Variable’s name.
   * @param value              Variable’s value.
   */
  public DeclareVariableStatement(final boolean publiclyVisible, final boolean editableByCommands, final boolean constant,
                                  final String variableName, final Node value) {
    this.publiclyVisible = publiclyVisible;
    this.editableByCommands = editableByCommands;
    this.constant = constant;
    this.variableName = Objects.requireNonNull(variableName);
    this.value = Objects.requireNonNull(value);
  }

  /**
   * Create a statement that declares a new variable from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public DeclareVariableStatement(final NBTTagCompound tag) {
    this(
        tag.getBoolean(PUBLIC_KEY),
        tag.getBoolean(EDITABLE_KEY),
        tag.getBoolean(CONSTANT_KEY),
        tag.getString(VAR_NAME_KEY),
        NodeNBTHelper.getNodeForTag(tag.getCompoundTag(VALUE_KEY))
    );
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    Object value = this.value.evaluate(scope);
    scope.declareVariable(new Variable(this.variableName, this.publiclyVisible, this.editableByCommands, this.constant, true, value));

    return StatementAction.PROCEED;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setBoolean(PUBLIC_KEY, this.publiclyVisible);
    tag.setBoolean(EDITABLE_KEY, this.editableByCommands);
    tag.setBoolean(CONSTANT_KEY, this.constant);
    tag.setString(VAR_NAME_KEY, this.variableName);
    tag.setTag(VALUE_KEY, this.value.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    String s = String.format("%s := %s;", this.variableName, this.value);
    if (this.constant) {
      s = "const " + s;
    } else {
      s = "var " + s;
    }
    if (this.editableByCommands) {
      s = "editable " + s;
    }
    if (this.publiclyVisible) {
      s = "public " + s;
    }
    return s;
  }
}
