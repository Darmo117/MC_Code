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
class ReturnStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ReturnStatement.ID_KEY, ReturnStatement.ID);
    tag.setTag(ReturnStatement.EXPR_KEY, new IntLiteralNode(1, 0, 0).writeToNBT());
    assertEquals(tag, new ReturnStatement(new IntLiteralNode(1, 0, 0), 0, 0).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ReturnStatement.ID_KEY, ReturnStatement.ID);
    tag.setTag(ReturnStatement.EXPR_KEY, new IntLiteralNode(1, 0, 0).writeToNBT());
    assertEquals(new ReturnStatement(new IntLiteralNode(1, 0, 0), 0, 0), new ReturnStatement(tag));
  }

  @Test
  void execute() {
    assertEquals(StatementAction.EXIT_FUNCTION, new ReturnStatement(new IntLiteralNode(1, 0, 0), 0, 0).execute(this.p.getScope()));
    assertEquals(1L, this.p.getScope().getVariable(ReturnStatement.RETURN_SPECIAL_VAR_NAME, false));
  }

  @Test
  void nullParameterError() {
    assertThrows(NullPointerException.class, () -> new ReturnStatement((Node) null, 0, 0));
  }

  @Test
  void testEquals() {
    assertEquals(new ReturnStatement(new IntLiteralNode(1, 0, 0), 0, 0), new ReturnStatement(new IntLiteralNode(1, 0, 0), 0, 0));
  }
}
