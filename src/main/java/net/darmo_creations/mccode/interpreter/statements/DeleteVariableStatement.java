package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class DeleteVariableStatement extends Statement {
  public static final int ID = 20;

  private static final String NAME_KEY = "VariableName";

  private final String variableName;

  public DeleteVariableStatement(final String variableName) {
    this.variableName = Objects.requireNonNull(variableName);
  }

  public DeleteVariableStatement(final NBTTagCompound tag) {
    this(tag.getString(NAME_KEY));
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
    tag.setString(NAME_KEY, this.variableName);
    return tag;
  }

  @Override
  public String toString() {
    return String.format("del %s;", this.variableName);
  }
}
