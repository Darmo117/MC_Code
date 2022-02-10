package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.MethodCallNode;
import net.darmo_creations.mccode.interpreter.nodes.VariableNode;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SetupProgramManager.class)
class ExpressionStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ExpressionStatement.ID_KEY, ExpressionStatement.ID);
    tag.setTag(ExpressionStatement.EXPRESSION_KEY, new IntLiteralNode(1, 0, 0).writeToNBT());
    assertEquals(tag, new ExpressionStatement(new IntLiteralNode(1, 0, 0), 0, 0).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ExpressionStatement.ID_KEY, ExpressionStatement.ID);
    tag.setTag(ExpressionStatement.EXPRESSION_KEY, new IntLiteralNode(1, 0, 0).writeToNBT());
    assertEquals(new ExpressionStatement(new IntLiteralNode(1, 0, 0), 0, 0), new ExpressionStatement(tag));
  }

  @Test
  void execute() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, new MCList(Collections.singletonList(1))));
    assertEquals(StatementAction.PROCEED, new ExpressionStatement(new MethodCallNode(new VariableNode("a", 0, 0), "clear", Collections.emptyList(), 0, 0), 0, 0).execute(this.p.getScope()));
    assertTrue(((MCList) this.p.getScope().getVariable("a", false)).isEmpty());
  }

  @Test
  void testEquals() {
    assertEquals(new ExpressionStatement(new IntLiteralNode(1, 0, 0), 0, 0), new ExpressionStatement(new IntLiteralNode(1, 0, 0), 0, 0));
  }
}
