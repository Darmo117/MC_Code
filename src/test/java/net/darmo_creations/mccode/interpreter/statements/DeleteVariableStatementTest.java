package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class DeleteVariableStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(DeleteVariableStatement.ID_KEY, DeleteVariableStatement.ID);
    tag.setString(DeleteVariableStatement.VAR_NAME_KEY, "a");
    assertEquals(tag, new DeleteVariableStatement("a").writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(DeleteVariableStatement.ID_KEY, DeleteVariableStatement.ID);
    tag.setString(DeleteVariableStatement.VAR_NAME_KEY, "a");
    assertEquals(new DeleteVariableStatement("a"), new DeleteVariableStatement(tag));
  }

  @Test
  void execute() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, 1));
    assertEquals(StatementAction.PROCEED, new DeleteVariableStatement("a").execute(this.p.getScope()));
    assertFalse(this.p.getScope().isVariableDefined("a"));
  }

  @Test
  void deleteNonDeletableError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, false, 1));
    assertThrows(EvaluationException.class, () -> new DeleteVariableStatement("a").execute(this.p.getScope()));
  }

  @Test
  void deleteUndefinedError() {
    assertThrows(EvaluationException.class, () -> new DeleteVariableStatement("a").execute(this.p.getScope()));
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new DeleteVariableStatement((String) null));
  }

  @Test
  void testEquals() {
    assertEquals(new DeleteVariableStatement("a"), new DeleteVariableStatement("a"));
  }
}
