package net.darmo_creations.mccode.interpreter.statements;

import net.darmo_creations.mccode.interpreter.DummyObject;
import net.darmo_creations.mccode.interpreter.SetupProgramManager;
import net.darmo_creations.mccode.interpreter.Variable;
import net.darmo_creations.mccode.interpreter.exceptions.CastException;
import net.darmo_creations.mccode.interpreter.exceptions.EvaluationException;
import net.darmo_creations.mccode.interpreter.nodes.IntLiteralNode;
import net.darmo_creations.mccode.interpreter.nodes.VariableNode;
import net.darmo_creations.mccode.interpreter.types.MCList;
import net.darmo_creations.mccode.interpreter.types.Position;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SetupProgramManager.class)
class SetPropertyStatementTest extends StatementTest {
  @Test
  void writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(SetPropertyStatement.ID_KEY, SetPropertyStatement.ID);
    tag.setTag(SetPropertyStatement.TARGET_KEY, new VariableNode("a", 0, 0).writeToNBT());
    tag.setTag(SetPropertyStatement.PROPERTY_NAME_KEY, new NBTTagString("b"));
    tag.setString(SetPropertyStatement.OPERATOR_KEY, "+=");
    tag.setTag(SetPropertyStatement.VALUE_KEY, new IntLiteralNode(1, 0, 0).writeToNBT());
    assertEquals(tag, new SetPropertyStatement(new VariableNode("a", 0, 0), "b", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0).writeToNBT());
  }

  @Test
  void constructFromNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(SetPropertyStatement.ID_KEY, SetPropertyStatement.ID);
    tag.setTag(SetPropertyStatement.TARGET_KEY, new VariableNode("a", 0, 0).writeToNBT());
    tag.setTag(SetPropertyStatement.PROPERTY_NAME_KEY, new NBTTagString("b"));
    tag.setString(SetPropertyStatement.OPERATOR_KEY, "+=");
    tag.setTag(SetPropertyStatement.VALUE_KEY, new IntLiteralNode(1, 0, 0).writeToNBT());
    assertEquals(new SetPropertyStatement(new VariableNode("a", 0, 0), "b", AssigmentOperator.PLUS, new IntLiteralNode(1, 0, 0), 0, 0), new SetPropertyStatement(tag));
  }

  @Test
  void execute() {
    MCList list = new MCList();
    DummyObject o = new DummyObject();
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, o));
    this.p.getScope().declareVariable(new Variable("list", false, false, true, true, list));
    assertEquals(StatementAction.PROCEED,
        new SetPropertyStatement(new VariableNode("a", 0, 0), "property", AssigmentOperator.ASSIGN, new VariableNode("list", 0, 0), 0, 0).execute(this.p.getScope()));
    MCList value = ((DummyObject) this.p.getScope().getVariable("a", false)).value;
    assertEquals(list, value);
    assertNotSame(list, value);
  }

  @Test
  void noPropertyError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, false, 1));
    assertThrows(EvaluationException.class, () -> new SetPropertyStatement(new VariableNode("a", 0, 0), "b", AssigmentOperator.ASSIGN, new IntLiteralNode(1, 0, 0), 0, 0).execute(this.p.getScope()));
  }

  @Test
  void noPropertySetterError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, false, new Position(0, 0, 0)));
    assertThrows(EvaluationException.class, () -> new SetPropertyStatement(new VariableNode("a", 0, 0), "x", AssigmentOperator.ASSIGN, new IntLiteralNode(1, 0, 0), 0, 0).execute(this.p.getScope()));
  }

  @Test
  void wrongTypeError() {
    this.p.getScope().declareVariable(new Variable("a", false, false, true, true, new DummyObject()));
    assertThrows(CastException.class, () -> new SetPropertyStatement(new VariableNode("a", 0, 0), "property", AssigmentOperator.ASSIGN, new IntLiteralNode(1, 0, 0), 0, 0).execute(this.p.getScope()));
  }

  @Test
  void testEquals() {
    assertEquals(new SetItemStatement(new VariableNode("a", 0, 0), new IntLiteralNode(0, 0, 0), AssigmentOperator.ASSIGN, new IntLiteralNode(1, 0, 0), 0, 0),
        new SetItemStatement(new VariableNode("a", 0, 0), new IntLiteralNode(0, 0, 0), AssigmentOperator.ASSIGN, new IntLiteralNode(1, 0, 0), 0, 0));
  }
}
