package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SetupProgramManager.class)
class ContinueStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ContinueStatement.ID_KEY, ContinueStatement.ID);
    assertEquals(tag, new ContinueStatement().writeToNBT());
  }

  @Test
  void execute() {
    assertEquals(StatementAction.CONTINUE_LOOP, new ContinueStatement().execute(this.p.getScope()));
  }

  @Test
  void testEquals() {
    assertEquals(new ContinueStatement(), new ContinueStatement());
  }
}
