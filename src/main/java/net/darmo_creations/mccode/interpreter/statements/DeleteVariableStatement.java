package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Statement that deletes a variable or function.
 */
public class DeleteVariableStatement extends Statement {
  public static final int ID = 20;

  public static final String VAR_NAME_KEY = "VariableName";

  private final String variableName;

  /**
   * Create a statement that deletes a variable or function.
   *
   * @param variableName Name of the variable or function to delete.
   */
  public DeleteVariableStatement(final String variableName) {
    this.variableName = Objects.requireNonNull(variableName);
  }

  /**
   * Create a statement that deletes a variable or function from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public DeleteVariableStatement(final NBTTagCompound tag) {
    this(tag.getString(VAR_NAME_KEY));
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    scope.deleteVariable(this.variableName, false);
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
    return tag;
  }

  @Override
  public String toString() {
    return String.format("del %s;", this.variableName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    DeleteVariableStatement that = (DeleteVariableStatement) o;
    return this.variableName.equals(that.variableName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.variableName);
  }
}
