package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.Node;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SetupProgramManager.class)
class WaitStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(WaitStatement.ID_KEY, WaitStatement.ID);
    tag.setTag(WaitStatement.TICKS_KEY, new IntLiteralNode(1).writeToNBT());
    assertEquals(tag, new WaitStatement(new IntLiteralNode(1)).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(WaitStatement.ID_KEY, WaitStatement.ID);
    tag.setTag(WaitStatement.TICKS_KEY, new IntLiteralNode(1).writeToNBT());
    assertEquals(new WaitStatement(new IntLiteralNode(1)), new WaitStatement(tag));
  }

  @Test
  void execute() {
    assertEquals(StatementAction.WAIT, new WaitStatement(new IntLiteralNode(1)).execute(this.p.getScope()));
    assertEquals(1, this.p.getScope().getProgram().getWaitTime());
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new WaitStatement((Node) null));
  }

  @Test
  void testEquals() {
    assertEquals(new WaitStatement(new IntLiteralNode(1)), new WaitStatement(new IntLiteralNode(1)));
  }
}
