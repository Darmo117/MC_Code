package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.Scope;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.darmo_creations.mccode.interpreter.nodes.NodeNBTHelper;
import net.darmo_creations.mccode.interpreter.type_wrappers.IntType;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Statement that pauses the execution of a program.
 */
public class WaitStatement extends Statement {
  public static final int ID = 50;

  private static final String TICKS_KEY = "Ticks";

  private final Node value;

  /**
   * Create a "wait" statement.
   *
   * @param value Amount of ticks to pause the program.
   *              Expression that evaluates to a positive integer.
   */
  public WaitStatement(final Node value) {
    this.value = Objects.requireNonNull(value);
  }

  /**
   * Create a "wait" statement from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public WaitStatement(final NBTTagCompound tag) {
    this(NodeNBTHelper.getNodeForTag(tag.getCompoundTag(TICKS_KEY)));
  }

  @Override
  public StatementAction execute(Scope scope) throws EvaluationException, ArithmeticException {
    int ticks = scope.getProgramManager().getTypeInstance(IntType.class)
        .implicitCast(scope, this.value.evaluate(scope));
    scope.getProgram().wait(scope, ticks);
    return StatementAction.WAIT;
  }

  @Override
  public int getID() {
    return ID;
  }

  @Override
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = super.writeToNBT();
    tag.setTag(TICKS_KEY, this.value.writeToNBT());
    return tag;
  }

  @Override
  public String toString() {
    return String.format("wait %s;", this.value);
  }
}
