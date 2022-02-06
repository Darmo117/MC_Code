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
class SetItemStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(SetItemStatement.ID_KEY, SetItemStatement.ID);
    tag.setTag(SetItemStatement.TARGET_KEY, new VariableNode("a").writeToNBT());
    tag.setTag(SetItemStatement.KEY_KEY, new IntLiteralNode(0).writeToNBT());
    tag.setString(SetItemStatement.OPERATOR_KEY, "+=");
    tag.setTag(SetItemStatement.VALUE_KEY, new IntLiteralNode(1).writeToNBT());
    assertEquals(tag, new SetItemStatement(new VariableNode("a"), new IntLiteralNode(0), AssigmentOperator.PLUS, new IntLiteralNode(1)).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(SetItemStatement.ID_KEY, SetItemStatement.ID);
    tag.setTag(SetItemStatement.TARGET_KEY, new VariableNode("a").writeToNBT());
    tag.setTag(SetItemStatement.KEY_KEY, new IntLiteralNode(0).writeToNBT());
    tag.setString(SetItemStatement.OPERATOR_KEY, "+=");
    tag.setTag(SetItemStatement.VALUE_KEY, new IntLiteralNode(1).writeToNBT());
    assertEquals(new SetItemStatement(new VariableNode("a"), new IntLiteralNode(0), AssigmentOperator.PLUS, new IntLiteralNode(1)), new SetItemStatement(tag));
  }

  @Test
  void execute() {
    MCList list = new MCList(Collections.singletonList(1L));
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, list));
    assertEquals(StatementAction.PROCEED, new SetItemStatement(new VariableNode("a"), new IntLiteralNode(0), AssigmentOperator.PLUS, new IntLiteralNode(1)).execute(this.p.getScope()));
    assertEquals(new MCList(Collections.singletonList(2L)), this.p.getScope().getVariable("a", false));
    assertSame(list, this.p.getScope().getVariable("a", false));
  }

  @Test
  void setUnsupportedError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, false, 1L));
    assertThrows(UnsupportedOperatorException.class, () -> new SetItemStatement(new VariableNode("a"), new IntLiteralNode(0), AssigmentOperator.PLUS, new IntLiteralNode(1)).execute(this.p.getScope()));
  }

  @Test
  void deleteUndefinedIndexError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, new MCList(Collections.singletonList(1L))));
    assertThrows(IndexOutOfBoundsException.class, () -> new SetItemStatement(new VariableNode("a"), new IntLiteralNode(1), AssigmentOperator.PLUS, new IntLiteralNode(1)).execute(this.p.getScope()));
  }

  @Test
  void deleteUndefinedKeyError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, new MCMap(Collections.singletonMap("a", 1L))));
    assertThrows(NoSuchKeyException.class, () -> new SetItemStatement(new VariableNode("a"), new StringLiteralNode("b"), AssigmentOperator.PLUS, new IntLiteralNode(1)).execute(this.p.getScope()));
  }

  @Test
  void nullTargetNodeError() {
    assertThrows(NullPointerException.class, () -> new SetItemStatement(null, new IntLiteralNode(0), AssigmentOperator.PLUS, new IntLiteralNode(1)));
  }

  @Test
  void nullKeyNodeError() {
    assertThrows(NullPointerException.class, () -> new SetItemStatement(new VariableNode("a"), null, AssigmentOperator.PLUS, new IntLiteralNode(1)));
  }

  @Test
  void nullOperatorError() {
    assertThrows(NullPointerException.class, () -> new SetItemStatement(new VariableNode("a"), new IntLiteralNode(0), null, new IntLiteralNode(1)));
  }

  @Test
  void nullValueNodeError() {
    assertThrows(NullPointerException.class, () -> new SetItemStatement(new VariableNode("a"), new IntLiteralNode(0), AssigmentOperator.PLUS, null));
  }

  @Test
  void testEquals() {
    assertEquals(new SetItemStatement(new VariableNode("a"), new IntLiteralNode(0), AssigmentOperator.PLUS, new IntLiteralNode(1)),
        new SetItemStatement(new VariableNode("a"), new IntLiteralNode(0), AssigmentOperator.PLUS, new IntLiteralNode(1)));
  }
}
