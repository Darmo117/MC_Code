package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.IndexOutOfBoundsException;
import net.darmo_creations.mccode.interpreter.exceptions.NoSuchKeyException;
import net.darmo_creations.mccode.interpreter.exceptions.UnsupportedOperatorException;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.StringLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.VariableNode;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.MCMap;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class DeleteItemStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(DeleteItemStatement.ID_KEY, DeleteItemStatement.ID);
    tag.setTag(DeleteItemStatement.TARGET_KEY, new VariableNode("a").writeToNBT());
    tag.setTag(DeleteItemStatement.KEY_KEY, new IntLiteralNode(0).writeToNBT());
    assertEquals(tag, new DeleteItemStatement(new VariableNode("a"), new IntLiteralNode(0)).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(DeleteItemStatement.ID_KEY, DeleteItemStatement.ID);
    tag.setTag(DeleteItemStatement.TARGET_KEY, new VariableNode("a").writeToNBT());
    tag.setTag(DeleteItemStatement.KEY_KEY, new IntLiteralNode(0).writeToNBT());
    assertEquals(new DeleteItemStatement(new VariableNode("a"), new IntLiteralNode(0)), new DeleteItemStatement(tag));
  }

  @Test
  void execute() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, new MCList(Collections.singletonList(1))));
    assertEquals(StatementAction.PROCEED, new DeleteItemStatement(new VariableNode("a"), new IntLiteralNode(0)).execute(this.p.getScope()));
    assertTrue(this.p.getScope().isVariableDefined("a"));
    assertEquals(new MCList(), this.p.getScope().getVariable("a", false));
  }

  @Test
  void deleteUnsupportedError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, false, 1));
    assertThrows(UnsupportedOperatorException.class, () -> new DeleteItemStatement(new VariableNode("a"), new IntLiteralNode(0)).execute(this.p.getScope()));
  }

  @Test
  void deleteUndefinedIndexError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, new MCList(Collections.singletonList(1))));
    assertThrows(IndexOutOfBoundsException.class, () -> new DeleteItemStatement(new VariableNode("a"), new IntLiteralNode(1)).execute(this.p.getScope()));
  }

  @Test
  void deleteUndefinedKeyError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, new MCMap(Collections.singletonMap("a", 1))));
    assertThrows(NoSuchKeyException.class, () -> new DeleteItemStatement(new VariableNode("a"), new StringLiteralNode("b")).execute(this.p.getScope()));
  }

  @Test
  void nullTargetNodeError() {
    assertThrows(NullPointerException.class, () -> new DeleteItemStatement(null, new IntLiteralNode(1)));
  }

  @Test
  void nullKeyNodeError() {
    assertThrows(NullPointerException.class, () -> new DeleteItemStatement(new VariableNode("a"), null));
  }

  @Test
  void testEquals() {
    assertEquals(new DeleteItemStatement(new VariableNode("a"), new IntLiteralNode(1)),
        new DeleteItemStatement(new VariableNode("a"), new IntLiteralNode(1)));
  }
}
