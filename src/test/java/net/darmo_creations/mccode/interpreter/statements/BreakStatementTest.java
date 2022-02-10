package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SetupProgramManager.class)
class BreakStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(BreakStatement.ID_KEY, BreakStatement.ID);
    assertEquals(tag, new BreakStatement(0, 0).writeToNBT());
  }

  @Test
  void execute() {
    assertEquals(StatementAction.EXIT_LOOP, new BreakStatement(0, 0).execute(this.p.getScope()));
  }

  @Test
  void testEquals() {
    assertEquals(new BreakStatement(0, 0), new BreakStatement(0, 0));
  }
}
